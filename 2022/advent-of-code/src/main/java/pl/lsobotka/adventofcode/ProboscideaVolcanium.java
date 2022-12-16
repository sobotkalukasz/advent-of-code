package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProboscideaVolcanium {

    private final Map<String, Valve> valves;
    private final List<String> workingValves;
    private final Map<String, Map<String, Integer>> shortestPaths;

    private final String start;

    ProboscideaVolcanium(final List<String> input) {
        start = "AA";
        valves = initValves(input);
        workingValves = getWorkingValves();
        shortestPaths = initMoves();
    }

    public int determineFlowRate() {
        final int time = 30;

        final Queue<ValveOpenPath> paths = new PriorityQueue<>();
        paths.add(new ValveOpenPath(start, time, 0, new ArrayList<>()));

        AtomicInteger flowRate = new AtomicInteger(0);

        while (!paths.isEmpty()) {
            final ValveOpenPath current = paths.poll();
            if (current.opened.size() == workingValves.size() && flowRate.intValue() < current.rate) {
                flowRate.set(current.rate);
            }
            workingValves.stream() //
                    .filter(v -> !current.opened.contains(v)) //
                    .map(v -> {
                        final int moves = shortestPaths.get(current.current).get(v);
                        final int timeLeft = current.timeLeft - moves - 1;
                        if (timeLeft < 0) {
                            if (flowRate.intValue() < current.rate) {
                                flowRate.set(current.rate);
                            }
                        }
                        final int rate = current.rate + timeLeft * valves.get(v).rate;
                        final List<String> opened = new ArrayList<>(current.opened);
                        opened.add(v);
                        return new ValveOpenPath(v, timeLeft, rate, opened);
                    }).filter(path -> path.timeLeft >= 0) //
                    .forEach(paths::add);
        }

        return flowRate.intValue();
    }

    private Map<String, Valve> initValves(List<String> input) {
        Map<String, Valve> valves = new HashMap<>();
        for (String raw : input) {
            final String[] parts = raw.split(";");

            final String[] valve = parts[0].replace("Valve", "")
                    .replace("has flow rate", "")
                    .replaceAll(" ", "")
                    .split("=");

            final String name = valve[0];
            final int rate = Integer.parseInt(valve[1]);

            final List<String> other = Arrays.stream(parts[1].replace("tunnels lead to valves", "")
                    .replace("tunnel leads to valve", "")
                    .replaceAll(" ", "")
                    .split(",")).collect(Collectors.toList());

            valves.put(name, new Valve(name, rate, other));
        }
        return valves;
    }

    private List<String> getWorkingValves() {
        return valves.entrySet()
                .stream()
                .filter(e -> e.getValue().rate > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private Map<String, Map<String, Integer>> initMoves() {
        Map<String, Map<String, Integer>> shortestPaths = new HashMap<>();

        final List<String> valvesForPath = new ArrayList<>(workingValves);
        valvesForPath.add(start);

        for (String from : valvesForPath) {
            Map<String, Integer> moves = new HashMap<>();
            shortestPaths.put(from, moves);
            for (String to : valvesForPath) {
                final int lessMoves = getMoves(from, to);
                moves.put(to, lessMoves);
            }
        }
        return shortestPaths;
    }

    private int getMoves(final String from, final String to) {
        final Queue<ValvePath> paths = new PriorityQueue<>();
        final Set<String> visited = new HashSet<>();

        paths.add(new ValvePath(from, 0));
        visited.add(from);

        while (!paths.isEmpty() && !paths.peek().current.equals(to)) {
            final ValvePath current = paths.poll();
            valves.get(current.current).valves.stream()
                    .filter(v -> !visited.contains(v))
                    .map(v -> new ValvePath(v, current.moves + 1))
                    .forEach(next -> {
                        visited.add(next.current);
                        paths.add(next);
                    });
        }

        return Optional.ofNullable(paths.peek()).map(ValvePath::moves).orElse(0);
    }

    private record Valve(String name, int rate, List<String> valves) {

    }

    private record ValveOpenPath(String current, int timeLeft, int rate, List<String> opened)
            implements Comparable<ValveOpenPath> {

        @Override
        public int compareTo(ValveOpenPath o) {
            return Comparator.comparing(ValveOpenPath::rate).compare(o, this);
        }
    }

    private record ValvePath(String current, int moves) implements Comparable<ValvePath> {
        @Override
        public int compareTo(ValvePath o) {
            return Comparator.comparing(ValvePath::moves).compare(this, o);
        }
    }

}

