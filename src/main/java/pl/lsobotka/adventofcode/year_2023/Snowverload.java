package pl.lsobotka.adventofcode.year_2023;

import java.util.*;
import java.util.function.Predicate;

/*
 * https://adventofcode.com/2023/day/25
 * */
public class Snowverload {

    private final Map<String, Set<String>> connectionGraph;
    private final Map<Set<String>, Integer> edgeFrequency;

    public Snowverload(final List<String> input) {
        this.connectionGraph = new HashMap<>();

        for (String row : input) {
            final String[] split = row.split(": ");
            final String[] to = split[1].split(" ");

            for (String next : to) {
                connectionGraph.computeIfAbsent(split[0], k -> new HashSet<>()).add(next);
                connectionGraph.computeIfAbsent(next, k -> new HashSet<>()).add(split[0]);
            }
        }

        final Map<String, Set<String>> notFoundCache = new HashMap<>();
        this.edgeFrequency = new HashMap<>();
        final List<String> components = new ArrayList<>(connectionGraph.keySet());
        for (int i = 0; i < components.size() - 1; ++i) {
            final String from = components.get(i);
            for (int j = i + 1; j < components.size(); ++j) {
                final String to = components.get(j);
                if(!notFoundCache.getOrDefault(to, Set.of()).contains(from)){
                    calculateEdgeFreq(from, to, notFoundCache);
                }
            }
        }
    }

    int disconnectThreeWires() {
        final HashMap<String, Set<String>> graph = new HashMap<>(this.connectionGraph);

        edgeFrequency.entrySet()
                .stream()
                .sorted(Comparator.<Map.Entry<Set<String>, Integer>>comparingInt(Map.Entry::getValue).reversed())
                .limit(3)
                .forEach(e ->
                    cutEdge(graph, new ArrayList<>(e.getKey()))
                );

        final int size = connectionSize(new ArrayList<>(graph.keySet()).getLast(), graph);
        return (this.connectionGraph.keySet().size() - size) * size;
    }

    private int connectionSize(final String start, final Map<String, Set<String>> graph) {
        final Queue<String> queue = new LinkedList<>();
        final Set<String> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            final String actual = queue.poll();

            final Set<String> nextConnections = new HashSet<>(graph.getOrDefault(actual, Set.of()));
            for (String next : nextConnections) {
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }

        }
        return visited.size();
    }

    private void cutEdge(Map<String, Set<String>> graph, List<String> edge) {
        graph.getOrDefault(edge.getFirst(), new HashSet<>()).remove(edge.getLast());
        graph.getOrDefault(edge.getLast(), new HashSet<>()).remove(edge.getFirst());
    }

    private void calculateEdgeFreq(final String start, final String to, final Map<String, Set<String>> notFoundCache) {

        final Queue<Path> paths = new LinkedList<>();
        final Set<String> visited = new HashSet<>();

        paths.add(new Path(start, Collections.emptyList()));
        visited.add(start);

        while (!paths.isEmpty()) {
            final Path actual = paths.poll();
            if (actual.actual.equals(to)) {
                actual.edges.forEach(edge -> {
                    int freq = edgeFrequency.getOrDefault(edge, 0);
                    edgeFrequency.put(edge, freq + 1);
                });
                return;
            }
            connectionGraph.getOrDefault(actual.actual, Collections.emptySet())
                    .stream()
                    .filter(Predicate.not(visited::contains))
                    .forEach(c -> {
                        final List<Set<String>> nextEdges = new ArrayList<>(actual.edges);
                        nextEdges.add(new HashSet<>(Set.of(actual.actual, c)));
                        paths.add(new Path(c, nextEdges));
                        visited.add(c);
                    });
        }
        notFoundCache.getOrDefault(to, new HashSet<>()).addAll(visited);
    }

    record Path(String actual, List<Set<String>> edges) {
    }

}
