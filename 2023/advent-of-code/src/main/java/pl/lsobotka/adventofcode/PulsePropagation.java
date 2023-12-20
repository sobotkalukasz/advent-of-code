package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2023/day/20
 * */
public class PulsePropagation {

    private final Map<String, Module> modules;

    public PulsePropagation(final List<String> input) {
        this.modules = ModuleFactory.modules(input);
    }

    long pressButton(long times) {

        final Map<String, Module> state = new HashMap<>(modules);
        long lowCount = 0;
        long highCount = 0;

        while (times-- > 0) {
            final List<Pulse> pulses = new ArrayList<>(state.get(ModuleType.BUTTON.symbol).processPulse(null));

            while (!pulses.isEmpty()) {
                final Pulse actual = pulses.removeFirst();
                if (actual.type == PulseType.LOW) {
                    lowCount++;
                } else {
                    highCount++;
                }

                if (state.containsKey(actual.targetModule)) {
                    final List<Pulse> next = state.get(actual.targetModule).processPulse(actual);
                    pulses.addAll(next);
                }
            }
        }

        return lowCount * highCount;
    }

    abstract static class Module {

        protected final String name;
        protected final List<String> targetModules;

        protected Module(String name, List<String> targetModules) {
            this.name = name;
            this.targetModules = targetModules;
        }

        abstract List<Pulse> processPulse(Pulse pulse);

        abstract ModuleType getType();

        boolean hasAsTarget(final String target) {
            return targetModules.contains(target);
        }

        protected List<Pulse> emmitPulse(final PulseType type) {
            return targetModules.stream().map(t -> new Pulse(type, name, t)).toList();
        }

    }

    static class FlipFlop extends Module {

        private boolean state;

        public FlipFlop(final String name, final List<String> targetModules) {
            super(name, targetModules);
            state = false;
        }

        @Override
        List<Pulse> processPulse(final Pulse pulse) {
            final List<Pulse> pulses = new ArrayList<>();
            if (pulse.type == PulseType.LOW) {
                if (state) {
                    state = false;
                    pulses.addAll(emmitPulse(PulseType.LOW));
                } else {
                    state = true;
                    pulses.addAll(emmitPulse(PulseType.HIGH));
                }
            }
            return pulses;
        }

        @Override
        ModuleType getType() {
            return ModuleType.FLIP_FLOP;
        }

    }

    static class Conjunction extends Module {

        private final Map<String, PulseType> recentPulses;

        public Conjunction(String name, List<String> targetModules) {
            super(name, targetModules);
            this.recentPulses = new HashMap<>();
        }

        @Override
        List<Pulse> processPulse(Pulse pulse) {
            final List<Pulse> pulses = new ArrayList<>();
            this.recentPulses.put(pulse.source, pulse.type);

            if (recentPulses.values().stream().allMatch(PulseType.HIGH::equals)) {
                pulses.addAll(emmitPulse(PulseType.LOW));
            } else {
                pulses.addAll(emmitPulse(PulseType.HIGH));
            }

            return pulses;
        }

        @Override
        ModuleType getType() {
            return ModuleType.CONJUNCTION;
        }

        void setSources(final List<String> source) {
            source.forEach(s -> recentPulses.put(s, PulseType.LOW));
        }
    }

    static class Broadcaster extends Module {

        public Broadcaster(final String name, final List<String> targetModules) {
            super(name, targetModules);
        }

        @Override
        public List<Pulse> processPulse(final Pulse pulse) {
            return emmitPulse(pulse.type);
        }

        @Override
        public ModuleType getType() {
            return ModuleType.BROADCASTER;
        }
    }

    static class Button extends Module {

        public Button() {
            super(ModuleType.BUTTON.symbol, List.of(ModuleType.BROADCASTER.symbol));
        }

        @Override
        List<Pulse> processPulse(Pulse pulse) {
            return emmitPulse(PulseType.LOW);
        }

        @Override
        ModuleType getType() {
            return ModuleType.BUTTON;
        }
    }

    static class Output extends Module {

        public Output() {
            super(ModuleType.OUTPUT.symbol, Collections.emptyList());
        }

        @Override
        List<Pulse> processPulse(Pulse pulse) {
            return Collections.emptyList();
        }

        @Override
        ModuleType getType() {
            return ModuleType.OUTPUT;
        }
    }

    record Pulse(PulseType type, String source, String targetModule) {

    }

    enum PulseType {
        LOW, HIGH
    }

    enum ModuleType {
        FLIP_FLOP("%"), CONJUNCTION("&"), BROADCASTER("broadcaster"), BUTTON("button"), OUTPUT("output");

        final String symbol;

        ModuleType(String symbol) {
            this.symbol = symbol;
        }
    }

    private static class ModuleFactory {
        private static final Pattern linePattern = Pattern.compile("([^- ]+) -> ([a-z, ]+)");

        static Map<String, Module> modules(final List<String> input) {

            final Map<String, Module> modules = new HashMap<>();

            for (String s : input) {
                final Matcher matcher = linePattern.matcher(s);
                if (matcher.find()) {
                    final List<String> target = extractNames(matcher.group(2));
                    final String module = matcher.group(1);
                    if (module.startsWith(ModuleType.FLIP_FLOP.symbol)) {
                        final String moduleName = module.replaceAll(ModuleType.FLIP_FLOP.symbol, "");
                        modules.put(moduleName, new FlipFlop(moduleName, target));
                    } else if (module.startsWith(ModuleType.CONJUNCTION.symbol)) {
                        final String moduleName = module.replaceAll(ModuleType.CONJUNCTION.symbol, "");
                        modules.put(moduleName, new Conjunction(moduleName, target));
                    } else if (module.startsWith(ModuleType.BROADCASTER.symbol)) {
                        modules.put(module, new Broadcaster(module, target));
                    }
                }
            }

            modules.put(ModuleType.BUTTON.symbol, new Button());
            modules.put(ModuleType.OUTPUT.symbol, new Output());

            modules.values()
                    .stream()
                    .filter(m -> m.getType() == ModuleType.CONJUNCTION)
                    .map(Conjunction.class::cast)
                    .forEach(module -> {
                        final List<String> sources = modules.values()
                                .stream()
                                .filter(m -> m.hasAsTarget(module.name))
                                .map(m -> m.name)
                                .toList();
                        module.setSources(sources);
                    });
            return modules;
        }

        static List<String> extractNames(final String stringNames) {
            return List.of(stringNames.replace(" ", "").split(","));
        }
    }
}