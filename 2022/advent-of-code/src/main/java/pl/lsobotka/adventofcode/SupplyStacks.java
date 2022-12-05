package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/5
 * */
public class SupplyStacks {

    private final TreeMap<Integer, Stack> stacks;
    private final List<Operation> operations;

    public SupplyStacks(final List<String> input) {
        this.stacks = parseStacks(input);
        this.operations = parseOperations(input);
    }

    private TreeMap<Integer, Stack> parseStacks(final List<String> input) {
        int idRowIndex = determineIdRowIndex(input);
        final TreeMap<Integer, Stack> stacks = new TreeMap<>();
        final char[] chars = input.get(idRowIndex).toCharArray();
        for (int col = 0; col < chars.length; col++) {
            if (Character.isDigit(chars[col])) {
                final Stack stack = Stack.from(input, idRowIndex, col);
                stacks.put(stack.id, stack);
            }
        }
        return stacks;
    }

    private int determineIdRowIndex(final List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            final char firsChar = input.get(i).replaceAll("\\s+", "").charAt(0);
            if (Character.isDigit(firsChar)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Not able to determine id row index");
    }

    private List<Operation> parseOperations(final List<String> input) {
        int opRowIndex = determineOperationRowIndex(input);
        final List<Operation> operations = new ArrayList<>();

        for (int row = opRowIndex; row < input.size(); row++) {
            operations.add(Operation.from(input.get(row)));
        }
        return operations;
    }

    private int determineOperationRowIndex(final List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).startsWith("move")) {
                return i;
            }
        }
        throw new IllegalArgumentException("Not able to determine operation start index");
    }

    public String applyOperationsAndGetFirstCargos() {
        operations.forEach(op -> {
            for (int i = 0; i < op.move; i++) {
                final String cargo = stacks.get(op.from).pool();
                stacks.get(op.to).put(cargo);
            }
        });
        return stacks.values().stream().map(Stack::getFirst).collect(Collectors.joining());

    }

    static class Stack {
        private final int id;
        private final LinkedList<String> cargos;

        private Stack(final int id, final LinkedList<String> cargos) {
            this.id = id;
            this.cargos = cargos;
        }

        static Stack from(final List<String> input, final int idRow, final int column) {

            final int id = Integer.parseInt(String.valueOf(input.get(idRow).charAt(column)));
            final LinkedList<String> cargos = new LinkedList<>();

            for (int row = idRow - 1; row >= 0; row--) {
                if (input.get(row).length() <= column) {
                    break;
                }
                final String cargo = String.valueOf(input.get(row).charAt(column));
                if (cargo.isBlank()) {
                    break;
                }
                cargos.addFirst(cargo);
            }

            return new Stack(id, cargos);
        }

        String pool() {
            return cargos.pollFirst();
        }

        void put(final String cargo) {
            cargos.addFirst(cargo);
        }

        String getFirst() {
            return cargos.getFirst();
        }
    }

    static class Operation {
        final int move;
        final int from;
        final int to;

        private Operation(final int move, final int from, final int to) {
            this.move = move;
            this.from = from;
            this.to = to;
        }

        static Operation from(final String raw) {
            final String[] split = raw.split("\\s+");
            return new Operation(Integer.parseInt(split[1]), Integer.parseInt(split[3]), Integer.parseInt(split[5]));
        }
    }
}
