package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class CrossedWires {

    private final Map<String, Boolean> wires;
    private final Queue<Connection> connections;

    CrossedWires(final List<String> input) {
        this.wires = new HashMap<>();
        this.connections = new ArrayDeque<>();

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

        while (!connections.isEmpty()) {
            Connection connection = connections.poll();
            if (connection.canSolve(wires)) {
                wires.put(connection.output(), connection.solve(wires));
            } else {
                connections.add(connection);
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
    }

    enum Operation {
        AND, OR, XOR
    }
}
