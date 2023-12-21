package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/18
 * */
public class LavaductLagoon {

    private final List<String> input;

    public LavaductLagoon(final List<String> input) {
        this.input = input;
    }

    long lagoonSize() {
        final List<Instruction> instructions = Instruction.simpleInstructions(input);
        final DigPlan plan = DigPlan.from(instructions);
        return plan.digOut();
    }

    long extendedLagoonSize() {
        final List<Instruction> instructions = Instruction.hexDecimalInstructions(input);
        final DigPlan plan = DigPlan.from(instructions);
        return plan.digOut();
    }

    record Instruction(Dir dir, int length) {
        private static final Pattern DIG_PATTERN = Pattern.compile("([LRUD]) (\\d+) \\(([#a-f0-9]{7})\\)");

        static List<Instruction> simpleInstructions(final List<String> input) {
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

        static List<Instruction> hexDecimalInstructions(final List<String> input) {
            final List<Instruction> instructions = new ArrayList<>();
            for (String row : input) {
                final Matcher matcher = DIG_PATTERN.matcher(row);
                if (matcher.find()) {
                    final String group = matcher.group(3);
                    final Dir dir = Instruction.ofDir(group.substring(group.length() - 1));
                    final int length = Integer.parseInt(group.substring(1, group.length() - 1), 16);
                    instructions.add(new Instruction(dir, length));
                } else {
                    throw new IllegalArgumentException("Unknown instruction string: " + row);
                }
            }

            return instructions;
        }

        static Dir ofDir(final String symbol) {
            return switch (symbol) {
                case "L", "2" -> Dir.LEFT;
                case "R", "0" -> Dir.RIGHT;
                case "U", "3" -> Dir.UP;
                case "D", "1" -> Dir.DOWN;
                default -> throw new IllegalArgumentException("Unknown symbol: " + symbol);
            };
        }
    }

    record DigPlan(List<Coord> corners, long borderArea) {

        static DigPlan from(final List<Instruction> instructions) {
            final List<Coord> corners = new ArrayList<>();
            long borderArea = 0;

            Coord actual = Coord.of(0, 0);
            for (Instruction ins : instructions) {
                actual = actual.getAdjacent(ins.dir, ins.length);
                corners.add(actual);
                borderArea += ins.length;
            }

            return new DigPlan(corners, borderArea);
        }

        long digOut() {
            final long insideArea = calculateInsideArea();

            // Pick's theorem
            return insideArea + borderArea / 2 + 1;
        }

        // Shoelace formula
        private long calculateInsideArea() {
            long inside = 0;
            for (int i = 0; i < corners.size(); i++) {
                final Coord from;
                if (i == 0) {
                    from = corners.get(corners.size() - 1);
                } else {
                    from = corners.get(i - 1);
                }
                final Coord to = corners.get(i);
                inside += (long) from.col() * to.row() - (long) from.row() * to.col();
            }
            return inside / 2;
        }
    }

}
