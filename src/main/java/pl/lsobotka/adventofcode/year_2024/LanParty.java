package pl.lsobotka.adventofcode.year_2024;

import java.util.*;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2024/day/23
 * */
public class LanParty {

    private final Map<String, Set<String>> connection;

    public LanParty(List<String> lines) {
        this.connection = new HashMap<>();

        for (String line : lines) {
            String[] split = line.split("-");
            connection.computeIfAbsent(split[0], k -> new HashSet<>()).add(split[1]);
            connection.computeIfAbsent(split[1], k -> new HashSet<>()).add(split[0]);
        }
    }

    long countConnected() {
        final Set<Set<String>> connected = new HashSet<>();
        for (String first : connection.keySet()) {
            final Set<String> connectedToFirst = connection.get(first);
            for (String second : connectedToFirst) {
                final Set<String> connectedToSecond = connection.get(second);
                for (String third : connectedToSecond) {
                    final Set<String> connectedToThird = connection.get(third);
                    if (connectedToThird.contains(first)) {
                        connected.add(Set.of(first, second, third));
                    }
                }
            }
        }
        return connected.stream().filter(set -> set.stream().anyMatch(s -> s.startsWith("t"))).count();
    }

    String getPassword() {
        final Set<Set<String>> connected = exploreConnections();
        final Set<String> biggestLanParty = connected.stream()
                .reduce((s1, s2) -> s1.size() > s2.size() ? s1 : s2)
                .orElseGet(Collections::emptySet);
        return biggestLanParty.stream().sorted().collect(Collectors.joining(","));
    }

    private Set<Set<String>> exploreConnections() {
        final Set<Set<String>> connected = new HashSet<>();
        for (String start : connection.keySet()) {
            final Queue<Path> paths = new PriorityQueue<>(
                    Comparator.comparingInt(p -> ((Path) p).visited().size()).reversed());
            paths.add(Path.from(start));

            while (!paths.isEmpty()) {
                final Path path = paths.poll();
                final HashSet<String> previous = new HashSet<>(path.visited());
                final HashSet<String> actual = new HashSet<>(path.visited());
                actual.add(path.current());

                if (path.visited.size() >= 2 && connected.stream().anyMatch(set -> set.containsAll(actual))) {
                    continue;
                }

                final Set<String> nextPaths = new HashSet<>(connection.getOrDefault(path.current(), Set.of()));
                if (nextPaths.containsAll(previous)) {
                    nextPaths.removeAll(previous);
                    if (nextPaths.isEmpty()) {
                        connected.add(actual);
                    }
                    for (String next : nextPaths) {
                        paths.add(path.next(next));
                    }

                } else {
                    connected.add(previous);
                }
            }
        }

        return connected;
    }

    record Path(String current, Set<String> visited) {
        static Path from(String current) {
            return new Path(current, new HashSet<>());
        }

        Path next(String next) {
            final Set<String> nextVisited = new HashSet<>(visited);
            nextVisited.add(current);
            return new Path(next, nextVisited);
        }
    }

}
