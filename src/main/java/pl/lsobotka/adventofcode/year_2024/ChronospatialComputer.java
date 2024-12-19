package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2024/day/17
 * */
public class ChronospatialComputer {
    private int[] instructions;
    private long a;
    private long b;
    private long c;

    public ChronospatialComputer(final List<String> lines) {
        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Register A:")) {
                a = Long.parseLong(line.split(":")[1].trim());
            } else if (line.startsWith("Register B:")) {
                b = Long.parseLong(line.split(":")[1].trim());
            } else if (line.startsWith("Register C:")) {
                c = Long.parseLong(line.split(":")[1].trim());
            } else if (line.startsWith("Program:")) {
                String[] parts = line.split(":")[1].trim().split(",");
                this.instructions = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    this.instructions[i] = Integer.parseInt(parts[i]);
                }
            }
        }
    }

    String run() {
        final Computer computer = new Computer(instructions, a, b, c);
        final List<Integer> output = computer.calculateResult();
        return output.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /*
     * 2,4,1,1,7,5,4,4,1,4,0,3,5,5,3,0
     * 2,4 - a = a % 8
     * 1,1 - b = b ^ 1
     * 7,5 - c = a >> b
     * 4,4 - b = b ^ c
     * 1,4 - b = b ^ 4
     * 0,3 - a = a >> 3
     * 5,5 - out b % 8
     * 3,0 - (a != 0) jump
     *
     *  out((b ^ ((a % 8) >> (b ^ 1))) ^ 4)
     *  a >> 3
     *
     * */
    long findLowestInitialState() {
        return reverseSearch(0, 0, Long.MAX_VALUE);
    }

    private long reverseSearch(long cur, int pos, long min) {
        final List<Integer> subInstruction = Arrays.stream(instructions)
                .boxed()
                .toList()
                .subList(instructions.length - pos - 1, instructions.length);

        for (int i = 0; i < 8; i++) {
            long next = (cur << 3) + i;
            final Computer computer = new Computer(instructions, next, 0, 0);
            final List<Integer> output = computer.calculateResult();
            if (output.equals(subInstruction)) {
                if (pos == instructions.length - 1) {
                    min = next;
                    break;
                }
                min = Math.min(min, reverseSearch(next, pos + 1, min));

            }
        }
        return min;
    }

    static class Computer {
        private int pointer = 0;
        private final int[] instruction;
        long a;
        long b;
        long c;

        Computer(final int[] instruction, long a, long b, long c) {
            this.instruction = instruction;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        List<Integer> calculateResult() {
            final List<Integer> output = new ArrayList<>();
            while (pointer < instruction.length) {

                final int opCode = instruction[pointer++];
                final int operand = instruction[pointer++];
                final long combo = determineCombo(operand);

                switch (opCode) {
                case 0 -> a = (long) (a / Math.pow(2, combo));
                case 1 -> b = b ^ operand;
                case 2 -> b = combo % 8;
                case 3 -> pointer = a != 0 ? operand : pointer;
                case 4 -> b = b ^ c;
                case 5 -> output.add((int) (combo % 8));
                case 6 -> b = (long) (a / Math.pow(2, combo));
                case 7 -> c = (long) (a / Math.pow(2, combo));
                default -> throw new IllegalStateException("Unexpected value: " + opCode);
                }
            }

            return output;
        }

        private long determineCombo(final int operand) {
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> throw new IllegalStateException("Unexpected value: " + operand);
            };
        }

    }

}
