package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/18
 * */
public class LavaductLagoon {

    private final List<Instruction> instructions;

    public LavaductLagoon(final List<String> input) {
        this.instructions = Instruction.instructionsFrom(input);
    }

    long lagoonSize() {
        final DigPlan plan = DigPlan.from(instructions);
        return plan.digOut();
    }

    record Instruction(Dir dir, int length) {
        private static final Pattern DIG_PATTERN = Pattern.compile("([LRUD]) (\\d+) \\(([#a-f0-9]{7})\\)");

        static List<Instruction> instructionsFrom(final List<String> input) {
            final List<Instruction> instructions = new ArrayList<>();
            for (String row : input) {
                final Matcher matcher = DIG_PATTERN.matcher(row);
                if (matcher.find()) {
                    final Dir dir = Instruction.ofDir(matcher.group(1));
                    final int length = Integer.parseInt(matcher.group(2));
                    instructions.add(new Instruction(dir, length));
                } else {
                    throw new IllegalArgumentException("Unknown instruction string: " + row);
                }
            }

            return instructions;
        }

        static Dir ofDir(final String symbol) {
            return switch (symbol) {
                case "L" -> Dir.LEFT;
                case "R" -> Dir.RIGHT;
                case "U" -> Dir.UP;
                case "D" -> Dir.DOWN;
                default -> throw new IllegalArgumentException("Unknown symbol: " + symbol);
            };
        }
    }

    record DigPlan(Set<Coord> trench) {

        static DigPlan from(final List<Instruction> instructions) {
            final Set<Coord> trench = new HashSet<>();

            Coord actual = Coord.of(0, 0);
            for (Instruction ins : instructions) {
                for (int i = 0; i < ins.length; i++) {
                    final Coord next = actual.getAdjacent(ins.dir);
                    trench.add(next);
                    actual = next;
                }
            }

            return new DigPlan(trench);
        }

        long digOut() {
            final int minRow = trench.stream().map(Coord::row).reduce(Integer.MAX_VALUE, Integer::min);
            final int maxRow = trench.stream().map(Coord::row).reduce(Integer.MIN_VALUE, Integer::max);
            final int minCol = trench.stream().map(Coord::col).reduce(Integer.MAX_VALUE, Integer::min);
            final int maxCol = trench.stream().map(Coord::col).reduce(Integer.MIN_VALUE, Integer::max);

            final Coord min = Coord.of(minRow, minCol);
            final Coord max = Coord.of(maxRow, maxCol);

            final Set<Coord> inside = new HashSet<>();
            final Set<Coord> outside = new HashSet<>();

            for (int row = minRow; row <= maxRow && inside.isEmpty(); row++) {
                for (int col = minCol; col <= maxCol && inside.isEmpty(); col++) {
                    final Coord start = Coord.of(row, col);
                    if (trench.contains(start) || outside.contains(start)) {
                        continue;
                    }

                    final Deque<Coord> queue = new LinkedList<>(List.of(start));
                    final Set<Coord> visited = new HashSet<>();

                    while (!queue.isEmpty()) {
                        final Coord actual = queue.removeLast();
                        if (outside.contains(actual)) {
                            outside.addAll(visited);
                            outside.addAll(queue);
                            visited.clear();
                            queue.clear();
                        } else {
                            final Set<Coord> adjacent = actual.getDirectAdjacent();
                            adjacent.removeIf(trench::contains);
                            adjacent.removeIf(visited::contains);

                            final boolean allInsideMap = adjacent.stream().allMatch(c -> c.isInside(min, max));
                            if (allInsideMap) {
                                visited.add(actual);
                                queue.addAll(adjacent);
                            } else {
                                outside.addAll(visited);
                                outside.addAll(queue);
                                visited.clear();
                                queue.clear();
                            }

                        }
                    }
                    inside.addAll(visited);
                }
            }
            return (long) trench().size() + (long) inside.size();
        }
    }

}
