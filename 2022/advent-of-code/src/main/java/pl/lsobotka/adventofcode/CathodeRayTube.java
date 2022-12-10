package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/10
 * */
public class CathodeRayTube {

    private final List<Operation> operations;

    CathodeRayTube(final List<String> input) {
        operations = input.stream().map(Operation::from).collect(Collectors.toList());
    }

    long determineSignalStrength() {
        final CycleValueHolder holder = new CycleValueHolder(operations);
        return holder.valuePerCycle.entrySet().stream().mapToLong(e -> e.getKey() * e.getValue()).sum();
    }

    String printScreen() {
        final Screen screen = new Screen(operations);
        return screen.print();
    }

    private static class CycleValueHolder {

        private final static int START = 20;
        private final static int INCREASE_BY = 40;
        private final static int STOP = 220;
        private final Map<Integer, Integer> valuePerCycle;

        public CycleValueHolder(final List<Operation> operations) {
            this.valuePerCycle = new HashMap<>();
            apply(operations);
        }

        void apply(final List<Operation> operations) {

            int currentCycle = 0;
            int value = 1;

            for (Operation op : operations) {
                if (op.type == Type.NOOP) {
                    currentCycle = increaseCycle(currentCycle, value);
                } else if (op.type == Type.ADDX) {
                    final int cycle = op.type.cycle;
                    for (int i = 0; i < cycle; i++) {
                        currentCycle = increaseCycle(currentCycle, value);
                        if (i == cycle - 1) {
                            value += op.value;
                        }
                    }
                }
            }

        }

        private int increaseCycle(int cycle, int value) {
            cycle++;
            if (shouldHoldValue(cycle)) {
                valuePerCycle.put(cycle, value);
            }
            return cycle;
        }

        private boolean shouldHoldValue(final int cycle) {
            final boolean isStartCycle = cycle == START;
            final boolean isLessOrEqualMax = cycle <= STOP;
            final boolean isMultiplied = (cycle - START) % INCREASE_BY == 0;
            return isStartCycle || (isLessOrEqualMax && isMultiplied);
        }

    }

    private static class Screen {

        private final static int ROW_SIZE = 40;
        private final Map<Integer, List<String>> screen;

        Screen(final List<Operation> operations) {
            this.screen = drawScreen(operations);
        }

        private Map<Integer, List<String>> drawScreen(final List<Operation> operations) {
            final Map<Integer, List<String>> screen = new HashMap<>();
            List<String> tempRow = new ArrayList<>();
            int currentRow = 0;
            int spritePos = 0;
            int currentCycle = 0;

            for (Operation op : operations) {
                if (op.type == Type.NOOP) {
                    currentCycle = increaseCycle(tempRow, spritePos, currentCycle);
                } else if (op.type == Type.ADDX) {
                    final int cycle = op.type.cycle;
                    for (int i = 0; i < cycle; i++) {
                        currentCycle = increaseCycle(tempRow, spritePos, currentCycle);
                        if (i == cycle - 1) {
                            spritePos += op.value;
                        }
                    }
                }

                if (tempRow.size() == ROW_SIZE) {
                    screen.put(currentRow, new ArrayList<>(tempRow));
                    tempRow = new ArrayList<>();
                    currentRow++;
                } else if (tempRow.size() > ROW_SIZE) {
                    screen.put(currentRow, new ArrayList<>(tempRow.subList(0, ROW_SIZE)));
                    tempRow = tempRow.subList(ROW_SIZE, tempRow.size());
                    currentRow++;
                }
            }

            if (!tempRow.isEmpty()) {
                screen.put(currentRow, new ArrayList<>(tempRow));
            }
            return screen;
        }

        private int increaseCycle(final List<String> row, final int spritePos, final int globalCycle) {
            final int currentCycle = globalCycle % ROW_SIZE;
            if (currentCycle >= spritePos && currentCycle <= spritePos + 2) {
                row.add("#");
            } else {
                row.add(" ");
            }
            return globalCycle + 1;
        }

        String print() {
            final StringBuilder sb = new StringBuilder();
            final List<Integer> rows = screen.keySet().stream().sorted().toList();

            for (Integer row : rows) {
                sb.append(String.join(" ", screen.get(row)));
                sb.append("\n");
            }

            return sb.toString();
        }

    }

    private record Operation(Type type, int value) {

        static Operation from(final String raw) {
            final String[] split = raw.split(" ");
            final Type type = Type.from(split[0]);
            final int value;
            if (split.length == 2) {
                value = Integer.parseInt(split[1]);
            } else {
                value = 0;
            }
            return new Operation(type, value);
        }
    }

    private enum Type {
        NOOP(1), ADDX(2);

        private final int cycle;

        Type(int cycle) {
            this.cycle = cycle;
        }

        static Type from(final String val) {
            return Type.valueOf(val.toUpperCase());
        }
    }
}
