package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChronospatialComputer {
    private final State initialState;
    private int[] instructions;

    public ChronospatialComputer(final List<String> lines) {
        int a = 0;
        int b = 0;
        int c = 0;

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("Register A:")) {
                a = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("Register B:")) {
                b = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("Register C:")) {
                c = Integer.parseInt(line.split(":")[1].trim());
            } else if (line.startsWith("Program:")) {
                String[] parts = line.split(":")[1].trim().split(",");
                this.instructions = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    this.instructions[i] = Integer.parseInt(parts[i]);
                }
            }
        }

        this.initialState = new State(a, b, c);
    }

    String run() {
        final Computer computer = new Computer(instructions, initialState);
        return computer.calculateResult();
    }

    static class Computer {
        private int pointer = 0;
        private final int[] instruction;
        private final List<State> register;

        Computer(final int[] instruction, final State initial) {
            this.instruction = instruction;
            this.register = new ArrayList<>();
            this.register.add(initial);
        }

        String calculateResult() {

            final List<Integer> output = new ArrayList<>();
            while (pointer < instruction.length) {

                final State state = register.getLast();
                final OpCode opCode = OpCode.of(instruction[pointer++]);
                final int operand = instruction[pointer++];
                final int combo = determineOperand(operand, state);

                switch (opCode) {
                case ADV -> {
                    final long v = (long) (state.a() / Math.pow(2, combo));
                    register.add(state.ofA((int) v));
                }
                case BXL -> {
                    final int v = state.b() ^ operand;
                    register.add(state.ofB(v));
                }
                case BST -> {
                    final int v = combo % 8;
                    register.add(state.ofB(v));
                }
                case JNZ -> {
                    if (state.a() != 0) {
                        pointer = operand;
                    }
                }
                case BXC -> {
                    final int v = state.b() ^ state.c();
                    register.add(state.ofB(v));
                }
                case OUT -> output.add(combo % 8);
                case BDV -> {
                    final int v = (int) (state.a() / Math.pow(2, combo));
                    register.add(state.ofB(v));
                }
                case CDV -> {
                    final int v = (int) (state.a() / Math.pow(2, combo));
                    register.add(state.ofC(v));
                }
                }
            }
            resetState();
            return output.stream().map(String::valueOf).collect(Collectors.joining(","));

        }

        private void resetState() {
            pointer = 0;
            final State initialState = register.getFirst();
            register.clear();
            register.add(initialState);

        }

        private int determineOperand(final int operand, final State state) {
            int value = -1;
            if (operand <= 3) {
                value = operand;
            }
            if (operand == 4) {
                value = state.a();
            }
            if (operand == 5) {
                value = state.b();
            }
            if (operand == 6) {
                value = state.c();
            }
            if (value == -1) {
                throw new IllegalArgumentException("Invalid operand: " + operand);
            }

            return value;
        }
    }

    record State(int a, int b, int c) {
        State ofA(int newA) {
            return new State(newA, b, c);
        }

        State ofB(int newB) {
            return new State(a, newB, c);
        }

        State ofC(int newC) {
            return new State(a, b, newC);
        }
    }

    enum OpCode {
        ADV(0), // podziel A przez 2 do potęgi operand and store to A
        BXL(1), // bitwise XOR of registry B with operand
        BST(2), // modulo 8 od operand and save to registry B
        JNZ(3), // if A != 0 jump to operand value
        BXC(4), // bitwise XOR of registry B and C and stores result in B
        OUT(5), // modulo 8 of operand and output it
        BDV(6), // podziel B przez 2 do potęgi operand and store to B
        CDV(7); // podziel C przez 2 do potęgi operand and store to C

        private int value;

        OpCode(int value) {
            this.value = value;
        }

        static OpCode of(int value) {
            return Arrays.stream(OpCode.values())
                    .filter(o -> o.value == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid opcode: " + value));
        }

    }
}
