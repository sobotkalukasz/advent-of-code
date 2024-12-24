package pl.lsobotka.adventofcode.year_2024;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2024/day/24
 * */
class CrossedWires {

    private final Map<String, Boolean> wires;
    private final List<Connection> connections;

    CrossedWires(final List<String> input) {
        this.wires = new HashMap<>();
        this.connections = new ArrayList<>();

        for (String line : input) {
            if (line.contains(":")) {
                String[] split = line.split(":");
                wires.put(split[0], split[1].trim().equals("1"));
            } else if (line.contains("->")) {
                connections.add(Connection.from(line));
            }
        }
    }

    long determineNumber() {
        final Queue<Connection> con = new ArrayDeque<>(connections);

        while (!con.isEmpty()) {
            Connection connection = con.poll();
            if (connection.canSolve(wires)) {
                wires.put(connection.output(), connection.solve(wires));
            } else {
                con.add(connection);
            }
        }

        final String binary = wires.keySet()
                .stream()
                .filter(k -> k.startsWith("z"))
                .sorted(Comparator.reverseOrder())
                .map(k -> toIntString(wires.get(k)))
                .collect(Collectors.joining());

        return Long.parseLong(binary, 2);
    }

    String swapWires() {
        final List<Connection> incorrect = new ArrayList<>();

        for (final Connection c : connections) {
            if (!c.isCorrect(connections)) {
                incorrect.add(c);
            }
        }

        return incorrect.stream().map(Connection::output).sorted().collect(Collectors.joining(","));
    }

    String toIntString(final boolean value) {
        return value ? "1" : "0";
    }

    record Connection(String wire1, String wire2, Operation op, String output) {
        private static final Pattern PATTERN = Pattern.compile(
                "([a-z0-9]{3})\\s+(AND|OR|XOR)\\s+([a-z0-9]{3})\\s+->\\s+([a-z0-9]{3})");

        static Connection from(String line) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                String wireA = matcher.group(1);
                Operation op = Operation.valueOf(matcher.group(2));
                String wireB = matcher.group(3);
                String wireC = matcher.group(4);
                return new Connection(wireA, wireB, op, wireC);
            }
            throw new IllegalArgumentException("Unable to parse line: " + line);
        }

        boolean canSolve(final Map<String, Boolean> wires) {
            return wires.containsKey(wire1) && wires.containsKey(wire2);
        }

        boolean solve(final Map<String, Boolean> wires) {
            final boolean v1 = wires.get(wire1);
            final boolean v2 = wires.get(wire2);

            return switch (op) {
                case AND -> v1 && v2;
                case OR -> v1 || v2;
                case XOR -> v1 ^ v2;
            };
        }

        boolean isCorrect(final List<Connection> connections) {
            boolean correct = true;
            if (this.isEnd() && this.isNotLastBit() && !this.is(Operation.XOR)) {
                correct = false;
            } else if (this.isMiddle() && this.is(Operation.XOR)) {
                correct = false;
            } else if (this.isStart() && this.isNotFirstBit() && this.is(Operation.XOR)) {
                correct = false;
                for (final Connection other : connections) {
                    if (this.isNext(other) && other.is(Operation.XOR)) {
                        correct = true;
                        break;
                    }
                }
            } else if (this.isStart() && this.isNotFirstBit() && this.is(Operation.AND)) {
                correct = false;
                for (final Connection other : connections) {
                    if (this.isNext(other) && other.is(Operation.OR)) {
                        correct = true;
                        break;
                    }
                }
            }
            return correct;
        }

        private boolean isStart() {
            return wire1.startsWith("x") || wire2.startsWith("x") || wire1.startsWith("y") || wire2.startsWith("y");
        }

        private boolean isEnd() {
            return output.startsWith("z");
        }

        private boolean isMiddle() {
            return !isStart() && !isEnd();
        }

        private boolean isNotFirstBit() {
            return !wire1.endsWith("00") && !wire2.endsWith("00");
        }

        private boolean isNotLastBit() {
            return !output.endsWith("45");
        }

        private boolean is(Operation op) {
            return this.op == op;
        }

        private boolean isNext(final Connection other) {
            return output.equals(other.wire1) || output.equals(other.wire2);
        }
    }

    enum Operation {
        AND, OR, XOR
    }

}
