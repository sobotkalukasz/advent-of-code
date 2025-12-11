package pl.lsobotka.adventofcode.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record Graph<T>(Map<T, Set<T>> graph) {

    public void addNode(final T node) {
        graph.putIfAbsent(node, new HashSet<>());
    }

    public Set<T> getNodes() {
        return graph.keySet();
    }

    public void addEdge(final T from, final T to) {
        addNode(from);
        addNode(to);
        graph.get(from).add(to);
    }

    public void addUndirectedEdge(final T a, final T b) {
        addNode(a);
        addNode(b);
        graph.get(a).add(b);
        graph.get(b).add(a);
    }

    public Set<T> getNeighbors(final T node) {
        return graph.getOrDefault(node, Set.of());
    }
}
