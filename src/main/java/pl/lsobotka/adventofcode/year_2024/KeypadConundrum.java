package pl.lsobotka.adventofcode.year_2024;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;
import pl.lsobotka.adventofcode.utils.Rotate;

/*
 * https://adventofcode.com/2024/day/21
 * */
class KeypadConundrum {

    private static final Pattern PATTERN = Pattern.compile("^0*(\\d+)[A-Za-z]$");

    private final List<String> numbers;
    private final Control control;

    KeypadConundrum(final List<String> numbers, final int controlQty) {
        this.numbers = numbers;
        this.control = Control.ofSize(controlQty);
    }

    long countPressedButtons() {
        final Map<String, Long> pressedButtons = new HashMap<>();
        for (String number : numbers) {
            final List<Character> buttonsToPress = number.chars().mapToObj(c -> (char) c).toList();
            final long buttonsSize = control.getButtonsSize(buttonsToPress);
            pressedButtons.put(number, buttonsSize);
        }
        return pressedButtons.entrySet().stream().mapToLong(e -> toNumber(e.getKey()) * e.getValue()).sum();
    }

    private long toNumber(final String number) {
        final Matcher matcher = PATTERN.matcher(number);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        return 0;
    }

    static class Control {
        private static final char CONFIRM = 'A';
        final Keypad keypad;
        final Map<CacheKey, Long> cache;
        Control next;
        char current;

        public Control(Keypad keypad) {
            this.keypad = keypad;
            this.current = CONFIRM;
            this.cache = new HashMap<>();
        }

        static Control ofSize(final int size) {
            final Keypad numeric = Keypad.numeric();
            final Keypad control = Keypad.control();

            final List<Control> controls = new ArrayList<>();

            for (int i = 0; i < size + 1; i++) {
                if (i == 0) {
                    controls.add(new Control(numeric));
                } else {
                    final Control next = new Control(control);
                    controls.add(next);
                    controls.get(i - 1).setNext(next);
                }
            }

            return controls.getFirst();
        }

        private void setNext(Control next) {
            this.next = next;
        }

        long getButtonsSize(final List<Character> toPress) {
            long size = 0;

            if (next == null) {
                size += toPress.size();
                current = toPress.getLast();
            } else {
                for (Character button : toPress) {
                    final CacheKey key = new CacheKey(current, button);
                    if (cache.containsKey(key)) {
                        size += cache.get(key);
                    } else {
                        final List<List<Character>> paths =
                                current != button ? keypad.getPath(current, button) : List.of(new ArrayList<>());
                        final Long nextSize = paths.stream().map(list -> {
                            list.add(CONFIRM);
                            return next.getButtonsSize(list);
                        }).reduce(Long::min).orElse(0L);
                        cache.put(key, nextSize);
                        size += nextSize;
                    }
                    current = button;
                }
            }

            return size;
        }

        record CacheKey(char from, char to) {
        }

    }

    record Keypad(List<Button> buttons, Map<Character, Map<Character, List<List<Character>>>> paths) {
        static Keypad numeric() {
            final List<Button> buttons = new ArrayList<>();
            buttons.add(new Button('7', Coord.of(0, 0)));
            buttons.add(new Button('8', Coord.of(0, 1)));
            buttons.add(new Button('9', Coord.of(0, 2)));
            buttons.add(new Button('4', Coord.of(1, 0)));
            buttons.add(new Button('5', Coord.of(1, 1)));
            buttons.add(new Button('6', Coord.of(1, 2)));
            buttons.add(new Button('1', Coord.of(2, 0)));
            buttons.add(new Button('2', Coord.of(2, 1)));
            buttons.add(new Button('3', Coord.of(2, 2)));
            buttons.add(new Button('0', Coord.of(3, 1)));
            buttons.add(new Button('A', Coord.of(3, 2)));
            return new Keypad(buttons, calculatePaths(buttons));
        }

        static Keypad control() {
            final List<Button> buttons = new ArrayList<>();
            buttons.add(new Button('^', Coord.of(0, 1)));
            buttons.add(new Button('A', Coord.of(0, 2)));
            buttons.add(new Button('v', Coord.of(1, 1)));
            buttons.add(new Button('<', Coord.of(1, 0)));
            buttons.add(new Button('>', Coord.of(1, 2)));
            return new Keypad(buttons, calculatePaths(buttons));
        }

        private static Map<Character, Map<Character, List<List<Character>>>> calculatePaths(
                final List<Button> buttons) {
            final Set<Coord> coords = buttons.stream().map(Button::coord).collect(Collectors.toSet());
            final Map<Character, Map<Character, List<List<Character>>>> shortest = new HashMap<>();

            for (int first = 0; first < buttons.size() - 1; first++) {
                for (int second = first + 1; second < buttons.size(); second++) {
                    final Button start = buttons.get(first);
                    final Button finish = buttons.get(second);

                    if (shortest.getOrDefault(start.code, Collections.emptyMap()).containsKey(finish.code)) {
                        continue;
                    }

                    final Queue<Path> paths = new PriorityQueue<>(Comparator.comparingInt(Path::changeCount));
                    paths.add(Path.empty(start.coord));

                    int bestChange = Integer.MAX_VALUE;
                    int minSize = Integer.MAX_VALUE;

                    while (!paths.isEmpty()) {
                        final Path current = paths.poll();
                        if (current.changeCount() > bestChange) {
                            continue;
                        }

                        if (current.coord.equals(finish.coord)) {
                            final Map<Character, List<List<Character>>> innerStart = shortest.computeIfAbsent(
                                    start.code, v -> new HashMap<>());
                            final Map<Character, List<List<Character>>> innerFinish = shortest.computeIfAbsent(
                                    finish.code, v -> new HashMap<>());

                            innerStart.computeIfAbsent(finish.code, v -> new ArrayList<>()).add(toChars(current.dirs));
                            innerFinish.computeIfAbsent(start.code, v -> new ArrayList<>())
                                    .add(toChars(reverseDirList(current.dirs)));
                            bestChange = current.changeCount();
                            minSize = Math.min(minSize, current.dirs.size());
                        } else {
                            for (Dir dir : Dir.values()) {
                                final Coord next = current.coord.getAdjacent(dir);
                                if (coords.contains(next)) {
                                    paths.add(current.next(next, dir));
                                }
                            }
                        }

                    }

                    final int bestSize = minSize;
                    final List<List<Character>> fromList = shortest.get(start.code).get(finish.code);
                    fromList.removeIf(l -> l.size() > bestSize);

                    final List<List<Character>> toList = shortest.get(finish.code).get(start.code);
                    toList.removeIf(l -> l.size() > bestSize);

                }
            }

            return shortest;
        }

        public List<List<Character>> getPath(char start, char finish) {
            final List<List<Character>> original = paths.getOrDefault(start, Collections.emptyMap())
                    .getOrDefault(finish, Collections.emptyList());

            List<List<Character>> copy = new ArrayList<>();
            for (List<Character> inner : original) {
                copy.add(new ArrayList<>(inner));
            }

            return copy;
        }

        private static List<Dir> reverseDirList(List<Dir> dirs) {
            final List<Dir> reversed = new ArrayList<>();
            for (Dir dir : dirs.reversed()) {
                reversed.add(dir.rotate(Rotate.L).rotate(Rotate.L));
            }

            return reversed;
        }

        private static List<Character> toChars(List<Dir> dirs) {
            final List<Character> chars = new ArrayList<>();
            for (Dir dir : dirs) {
                switch (dir) {
                case UP -> chars.add('^');
                case DOWN -> chars.add('v');
                case LEFT -> chars.add('<');
                case RIGHT -> chars.add('>');
                }
            }

            return chars;
        }

        record Path(Coord coord, List<Dir> dirs) {
            static Path empty(Coord start) {
                return new Path(start, new ArrayList<>());
            }

            Path next(Coord coord, Dir dir) {
                final List<Dir> next = new ArrayList<>(dirs);
                next.add(dir);
                return new Path(coord, next);
            }

            int changeCount() {
                int change = 0;
                if (!dirs.isEmpty()) {
                    for (int i = 1; i < dirs.size(); i++) {
                        if (dirs.get(i - 1) != dirs.get(i)) {
                            change++;
                        }
                    }
                }
                return change;
            }
        }

        record Button(char code, Coord coord) {
        }

    }

}
