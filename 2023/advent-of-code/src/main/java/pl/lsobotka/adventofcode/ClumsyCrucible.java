package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Predicate;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/17
 * */
public class ClumsyCrucible {
    private final Map<Coord, Integer> heatMap;
    private final Coord finish;

    public ClumsyCrucible(final List<String> input) {
        this.heatMap = new HashMap<>();
        int maxRow = input.size() - 1;
        int maxCol = input.get(0).length() - 1;
        this.finish = Coord.of(maxRow, maxCol);

        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            for (int col = 0; col < rowString.length(); col++) {
                final Coord coord = Coord.of(row, col);
                heatMap.put(coord, Integer.parseInt(String.valueOf(rowString.charAt(col))));
            }
        }
    }

    long minimalHeatLost() {
        return calculateMinimalHeatLost(path -> path.dirCount < 3, path -> true);
    }

    long minimalHeatLostWithComplexDirections() {
        return calculateMinimalHeatLost(path -> path.dirCount < 10, path -> path.dirCount >= 4);
    }

    long calculateMinimalHeatLost(final Predicate<LavaPath> forwardPredicate, final Predicate<LavaPath> turnPredicate) {
        final Coord start = Coord.of(0, 0);

        final Queue<LavaPath> paths = new PriorityQueue<>();
        paths.add(new LavaPath(start, Dir.RIGHT, 1, 0));

        final Map<Coord, Map<Integer, Long>> visited = new HashMap<>();
        visited.put(start, new HashMap<>());

        long best = Long.MAX_VALUE;

        while (!paths.isEmpty()) {
            final LavaPath current = paths.poll();
            if (current.actual.equals(finish)) {
                if (turnPredicate.test(current) && current.heatLost < best) {
                    best = current.heatLost;
                }
                continue;
            }

            for (Dir nextDir : getDirections(current, forwardPredicate, turnPredicate)) {
                if (nextDir != current.lastDir || forwardPredicate.test(current)) {
                    final Coord nextPos = current.actual.getAdjacent(nextDir);
                    if (heatMap.containsKey(nextPos)) {
                        final int nextDirCount = nextDir == current.lastDir ? current.dirCount + 1 : 1;
                        final long nextHeatLost = current.heatLost + heatMap.get(nextPos);
                        final int nextDirHash = nextDir.hashCode() + nextDirCount;

                        if (!visited.containsKey(nextPos) || !visited.get(nextPos).containsKey(nextDirHash)
                                || visited.get(nextPos).get(nextDirHash) > nextHeatLost) {
                            paths.add(new LavaPath(nextPos, nextDir, nextDirCount, nextHeatLost));
                            final Map<Integer, Long> dirMap = visited.getOrDefault(nextPos, new HashMap<>());
                            dirMap.put(nextDirHash, nextHeatLost);
                            visited.put(nextPos, dirMap);
                        }
                    }
                }
            }
        }

        return best;
    }

    List<Dir> getDirections(final LavaPath path, final Predicate<LavaPath> forwardPredicate,
            final Predicate<LavaPath> turnPredicate) {
        final Dir lastDir = path.lastDir;
        final List<Dir> dirs = new ArrayList<>();
        if (turnPredicate.test(path)) {
            switch (lastDir) {
            case LEFT, RIGHT -> dirs.addAll(List.of(Dir.UP, Dir.DOWN));
            case UP, DOWN -> dirs.addAll(List.of(Dir.LEFT, Dir.RIGHT));
            }
        }
        if (forwardPredicate.test(path)) {
            dirs.add(path.lastDir);
        }
        return dirs;
    }

    record LavaPath(Coord actual, Dir lastDir, int dirCount, long heatLost) implements Comparable<LavaPath> {

        @Override
        public int compareTo(LavaPath o) {
            return Comparator.comparing(LavaPath::heatLost).compare(this, o);
        }
    }
}
