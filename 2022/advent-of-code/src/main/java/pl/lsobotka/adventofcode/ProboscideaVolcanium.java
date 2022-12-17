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

    /*
    * A suboptimal solution - it takes around 3 minutes to determine right answer
    * */
    public int determineFlowRateWithHelp() {
        final int time = 26;

        final Queue<ValveOpenPathWithHelp> paths = new PriorityQueue<>();
        ValveOpenPathWithHelp bestPath = new ValveOpenPathWithHelp(start, 0, start, 0, time, 0, new ArrayList<>(workingValves));
        paths.add(bestPath);

        while (!paths.isEmpty()) {
            final ValveOpenPathWithHelp path = paths.poll();

            if (path.currentMoveLeft == path.elephantMoveLeft) {
                for (String me : path.toOpen) {
                    for (String elephant : path.toOpen) {
                        if (me.equals(elephant)) {
                            continue;
                        }

                        final int movesMe = shortestPaths.get(path.current).get(me);
                        final int movesElephant = shortestPaths.get(path.elephant).get(elephant);

                        final int timeLeft = path.timeLeft - 1 - path.currentMoveLeft;

                        final int oldRate = path.rate;
                        final int rateOpenNextValve = (timeLeft - movesMe) * valves.get(me).rate;
                        final int rateOpenNextElephantValve = (timeLeft - movesElephant) * valves.get(elephant).rate;
                        final int newRate = oldRate + rateOpenNextValve + rateOpenNextElephantValve;

                        final List<String> toOpen = new ArrayList<>(path.toOpen);
                        toOpen.remove(me);
                        toOpen.remove(elephant);

                        final int moveTime = Math.min(movesMe, movesElephant);

                        if (timeLeft - moveTime >= 0 && !(timeLeft - moveTime <= 13 && newRate < bestPath.rate()/2)) {
                            final ValveOpenPathWithHelp nextPath = new ValveOpenPathWithHelp(me, movesMe - moveTime,
                                    elephant, movesElephant - moveTime, timeLeft - moveTime, newRate, toOpen);
                            if(timeLeft - moveTime == 0){
                                if (bestPath.rate < path.rate) {
                                    bestPath = path;
                                }
                            } else {
                                bestPath = bestPath.rate > nextPath.rate ? bestPath : nextPath;
                                paths.add(nextPath);
                            }
                        }
                    }
                }

            } else if (path.currentMoveLeft < path.elephantMoveLeft) {
                for (String me : path.toOpen) {
                    final int nextMove = shortestPaths.get(path.current).get(me);
                    final int nextElephantMove = path.elephantMoveLeft - path.currentMoveLeft - 1;

                    final int timeLeft = path.timeLeft - 1 - path.currentMoveLeft;
                    final int newRate = path.rate + (timeLeft - nextMove) * valves.get(me).rate;

                    final List<String> toOpen = new ArrayList<>(path.toOpen);
                    toOpen.remove(me);

                    final int moveTime = Math.min(nextMove, nextElephantMove);

                    if (timeLeft - moveTime >= 0 && !(timeLeft - moveTime <= 13 && newRate < bestPath.rate()/2)) {
                        final ValveOpenPathWithHelp nextPath = new ValveOpenPathWithHelp(me, nextMove - moveTime,
                                path.elephant, nextElephantMove - moveTime, timeLeft - moveTime, newRate, toOpen);
                        if(timeLeft - moveTime == 0){
                            if (bestPath.rate < path.rate) {
                                bestPath = path;
                            }
                        } else {
                            bestPath = bestPath.rate > nextPath.rate ? bestPath : nextPath;
                            paths.add(nextPath);
                        }
                    }
                }

            } else {
                for (String elephant : path.toOpen) {
                    final int nextMove = path.currentMoveLeft - path.elephantMoveLeft - 1;
                    final int nextElephantMove = shortestPaths.get(path.elephant).get(elephant);

                    final int timeLeft = path.timeLeft - 1 - path.elephantMoveLeft;
                    final int newRate = path.rate + (timeLeft - nextElephantMove) * valves.get(elephant).rate;

                    final List<String> toOpen = new ArrayList<>(path.toOpen);
                    toOpen.remove(elephant);

                    final int moveTime = Math.min(nextMove, nextElephantMove);

                    if (timeLeft - moveTime >= 0 && !(timeLeft - moveTime <= 13 && newRate < bestPath.rate()/2)) {
                        final ValveOpenPathWithHelp nextPath = new ValveOpenPathWithHelp(path.current, nextMove - moveTime,
                                elephant, nextElephantMove - moveTime, timeLeft - moveTime, newRate, toOpen);
                        if(timeLeft - moveTime == 0){
                            if (bestPath.rate < path.rate) {
                                bestPath = path;
                            }
                        } else {
                            bestPath = bestPath.rate > nextPath.rate ? bestPath : nextPath;
                            paths.add(nextPath);
                        }
                    }
                }
            }
        }

        return bestPath.rate();
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

    private record ValveOpenPathWithHelp(String current, int currentMoveLeft, String elephant, int elephantMoveLeft,
                                         int timeLeft, int rate, List<String> toOpen)
            implements Comparable<ValveOpenPathWithHelp> {

        @Override
        public int compareTo(ValveOpenPathWithHelp o) {
            return Comparator.comparing(ValveOpenPathWithHelp::rate).compare(o, this);
        }
    }

    private record ValvePath(String current, int moves) implements Comparable<ValvePath> {
        @Override
        public int compareTo(ValvePath o) {
            return Comparator.comparing(ValvePath::moves).compare(this, o);
        }
    }

}

