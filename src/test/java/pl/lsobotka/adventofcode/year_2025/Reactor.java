package pl.lsobotka.adventofcode.year_2025;

import java.util.*;
import pl.lsobotka.adventofcode.utils.Graph;

/*
 * https://adventofcode.com/2025/day/11
 * */
public class Reactor {

    private static final String END = "out";

    private final Graph<String> graph;

    Reactor(final List<String> input) {
        graph = new Graph<>(new HashMap<>());

        for (String row : input) {
            final String[] split = row.split(":");
            final String from = split[0].trim();

            final String[] toSplit = split[1].split(" ");
            for (String to : toSplit) {
                if (to.isBlank()) {
                    continue;
                }
                graph.addEdge(from, to.trim());
            }
        }
    }

    long countPaths() {
        final List<Set<String>> result = new ArrayList<>();
        final Queue<Path> paths = new PriorityQueue<>();
        paths.add(new Path("you", new HashSet<>(Set.of("you"))));

        while (!paths.isEmpty()) {
            final Path path = paths.poll();

            if (path.actual.equals(END)) {
                result.add(path.visited);
                continue;
            }
            for (String neighbor : graph.getNeighbors(path.actual)) {
                if (!path.visited.contains(neighbor)) {
                    paths.add(path.next(neighbor));
                }
            }
        }

        return result.size();
    }

    record Path(String actual, Set<String> visited) implements Comparable<Path> {

        Path next(final String next) {
            final HashSet<String> strings = new HashSet<>(visited);
            strings.add(next);
            return new Path(next, strings);

        }

        @Override
        public int compareTo(Path o) {
            return Integer.compare(o.visited.size(), this.visited.size());
        }
    }

    long countPathsDag() {
        final List<String> nodes = new ArrayList<>(graph.getNodes());
        int n = nodes.size();
        Map<String, Integer> id = new HashMap<>();
        for (int i = 0; i < n; i++) {
            id.put(nodes.get(i), i);
        }

        int startId = id.get("svr");
        int endId = id.get(END);

        final List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++)
            adj[i] = new ArrayList<>();
        int[] indeg = new int[n];
        for (String from : nodes) {
            int fi = id.get(from);
            for (String to : graph.getNeighbors(from)) {
                int ti = id.get(to);
                adj[fi].add(ti);
                indeg[ti]++;
            }
        }

        int[] topo = new int[n];
        int tpos = 0;
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
            }
        }
        while (!q.isEmpty()) {
            int v = q.removeFirst();
            topo[tpos++] = v;
            for (int u : adj[v]) {
                if (--indeg[u] == 0) {
                    q.add(u);
                }
            }
        }
        if (tpos != n) {
            throw new IllegalStateException("Graph is not a DAG (has cycles) – DP won't work.");
        }

        final Set<String> shouldVisit = Set.of("dac", "fft");
        Map<String, Integer> reqIndex = new HashMap<>();
        int idx = 0;
        for (String s : shouldVisit) {
            reqIndex.put(s, idx++);
        }
        int k = shouldVisit.size();
        int masks = (k == 0 ? 1 : (1 << k));
        int fullMask = (k == 0 ? 0 : (1 << k) - 1);

        long[][] dp = new long[n][masks];

        int endBit = 0;
        Integer endReqIdx = reqIndex.get(nodes.get(endId));
        if (endReqIdx != null) {
            endBit = 1 << endReqIdx;
        }
        if (k == 0) {
            dp[endId][0] = 1;
        } else {
            dp[endId][endBit] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            int v = topo[i];
            if (v == endId) {
                continue;
            }

            int vBit = 0;
            Integer vReqIdx = reqIndex.get(nodes.get(v));
            if (vReqIdx != null) {
                vBit = 1 << vReqIdx;
            }

            for (int u : adj[v]) {
                for (int mask = 0; mask < masks; mask++) {
                    long ways = dp[u][mask];
                    if (ways == 0) {
                        continue;
                    }
                    int newMask = mask | vBit;
                    dp[v][newMask] += ways;
                }
            }
        }

        return dp[startId][fullMask];
    }

}
