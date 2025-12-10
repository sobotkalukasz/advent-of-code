package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2025/day/10
 * */
public class Factory {

    final List<Machine> machines;

    Factory(final List<String> input) {
        machines = input.stream().map(Machine::of).toList();
    }

    long countButtonPress() {
        return machines.stream().mapToLong(this::countButtonPress).sum();
    }

    private long countButtonPress(final Machine machine) {
        final HashMap<Integer, Integer> visited = new HashMap<>();

        final Queue<State> states = new PriorityQueue<>();
        states.addAll(machine.initialStates());

        int best = Integer.MAX_VALUE;

        while (!states.isEmpty()) {
            final State actual = states.poll();

            if (actual.steps >= best) {
                continue;
            }

            final int hash = actual.stateHash();
            if (visited.getOrDefault(hash, Integer.MAX_VALUE) <= actual.steps) {
                continue;
            }
            visited.put(hash, actual.steps);

            if (Arrays.equals(actual.lights, machine.lights)) {
                best = actual.steps;
            }

            for (List<Integer> button : machine.buttons) {
                if (!actual.toPress.equals(button)) {
                    states.add(actual.press(button));
                }
            }

        }
        return best;
    }

    record State(int[] lights, List<Integer> toPress, int steps) implements Comparable<State> {

        State press(final List<Integer> next) {
            final int[] copy = Arrays.copyOf(lights, lights.length);
            toPress.forEach(i -> copy[i] = 1 - copy[i]);
            return new State(copy, next, steps + 1);
        }

        int stateHash() {
            int h = Arrays.hashCode(lights);
            h = 31 * h + toPress.hashCode();
            return h;
        }

        @Override
        public int compareTo(State o) {
            return Comparator.comparingInt(State::steps).compare(this, o);
        }
    }

    record Machine(int[] lights, List<List<Integer>> buttons, List<Integer> requirements) {

        private static final Pattern LIGHTS_PATTERN = Pattern.compile("\\[([.#]+)\\]");
        private static final Pattern REQUIREMENTS_PATTERN = Pattern.compile(
                "\\{\\s*([0-9]+(?:\\s*,\\s*[0-9]+)*)\\s*\\}");
        private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((\\d+(?:\\s*,\\s*\\d+)*)\\)");

        static Machine of(final String input) {

            Matcher lm = LIGHTS_PATTERN.matcher(input);
            if (!lm.find()) {
                throw new IllegalArgumentException("No lights [] in: " + input);
            }
            String lightsStr = lm.group(1);
            final int[] lights = lightsStr.chars().map(ch -> ch == '#' ? 1 : 0).toArray();

            final List<List<Integer>> buttons = new ArrayList<>();
            Matcher bm = BUTTON_PATTERN.matcher(input);
            while (bm.find()) {
                String buttonStr = bm.group(1);
                final List<Integer> nums = Arrays.stream(buttonStr.split(","))
                        .map(String::trim)
                        .filter(t -> !t.isEmpty())
                        .map(Integer::parseInt)
                        .toList();
                buttons.add(nums);
            }

            Matcher rm = REQUIREMENTS_PATTERN.matcher(input);
            if (!rm.find()) {
                throw new IllegalArgumentException("No requirements [] in: " + input);
            }
            String reqStr = rm.group(1);
            List<Integer> requirements = Arrays.stream(reqStr.split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .map(Integer::parseInt)
                    .toList();

            return new Machine(lights, buttons, requirements);
        }

        List<State> initialStates() {
            return buttons.stream().map(b -> new State(new int[lights.length], b, 0)).toList();
        }

    }
}
