package pl.lsobotka.adventofcode.year_2023;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

public class LongWalk {
    private final ForestMap forestMap;

    public LongWalk(final List<String> input) {
        this.forestMap = ForestMap.from(input);
    }

    long longestPathWithSlopes() {
        return forestMap.longestPathWithSlopes();
    }

    long longestWalk() {
        return forestMap.longestPath();
    }

    record ForestMap(Set<Coord> forest, Map<Coord, Slop> slops, Coord start, Coord finish, Coord max) {

        int longestPathWithSlopes() {
            final Map<Coord, Integer> visitedCountCache = new HashMap<>();

            final Queue<Path> paths = new PriorityQueue<>();
            paths.add(new Path(start, 0, Set.of(start)));

            int actualBest = 0;

            while (!paths.isEmpty()) {
                final Path actual = paths.poll();

                if (actual.pos.equals(finish)) {
                    actualBest = Math.max(actualBest, actual.moves);
                    continue;
                }

                if (validateCache(actual, visitedCountCache)) {
                    for (Dir dir : Dir.values()) {
                        final Coord nextPos = actual.pos.getAdjacent(dir);
                        processNextPos(actual, nextPos, dir, paths, 1);
                    }
                }
            }

            return actualBest;
        }

        int longestPath() {
            final Set<Coord> junctions = junctions();
            final Map<Coord, Junction> pathToJunctions = junctions.stream()
                    .collect(Collectors.toMap(Function.identity(), j -> junctionPathsFrom(j, junctions)));

            final Queue<Path> paths = new PriorityQueue<>();
            paths.add(new Path(start, 0, Set.of(start)));

            int actualBest = 0;

            while (!paths.isEmpty()) {
                final Path actual = paths.poll();

                if (actual.pos.equals(finish)) {
                    actualBest = Math.max(actualBest, actual.moves);
                    continue;
                }

                pathToJunctions.get(actual.pos).pathsToJunctions.forEach((next, path) -> {
                    if (!actual.visited.contains(next)) {
                        final Set<Coord> nextVisited = new HashSet<>(actual.visited);
                        nextVisited.add(next);
                        paths.add(new Path(next, actual.moves + path.size(), nextVisited));
                    }
                });

            }

            return actualBest;
        }

        private void processNextPos(final Path actual, final Coord nextPos, final Dir dir, final Queue<Path> paths,
                int offset) {
            if (isInsideMap(nextPos) && !actual.visited.contains(nextPos) && !forest.contains(nextPos)) {
                if (slops.containsKey(nextPos)) {
                    if (slops.get(nextPos).canMove(dir)) {
                        final Coord another = nextPos.getAdjacent(dir);
                        processNextPos(actual, another, dir, paths, offset + 1);
                    }
                } else {
                    paths.add(actual.next(nextPos, offset));
                }
            }
        }

        private boolean validateCache(final Path path, final Map<Coord, Integer> visitedCountCache) {
            boolean valid = true;
            if (visitedCountCache.containsKey(path.pos)) {
                if (visitedCountCache.get(path.pos) >= path.moves) {
                    valid = false;
                } else {
                    visitedCountCache.put(path.pos, path.moves);
                }
            } else {
                visitedCountCache.put(path.pos, path.moves);
            }
            return valid;
        }

        private boolean isInsideMap(final Coord pos) {
            return 0 <= pos.row() && pos.row() <= max().row() && 0 <= pos.col() && pos.col() <= max().col();
        }

        private Set<Coord> junctions() {
            final Predicate<Coord> isPath = c -> !forest.contains(c) && isInsideMap(c);
            final Set<Coord> junctions = new HashSet<>();

            for (int row = 0; row <= max.row(); row++) {
                for (int col = 0; col <= max.col(); col++) {
                    final Coord actual = Coord.of(row, col);
                    if (isPath.test(actual)) {
                        final boolean isJunction = actual.getDirectAdjacent().stream().filter(isPath).count() > 2;
                        if (isJunction) {
                            junctions.add(actual);
                        }
                    }
                }
            }
            junctions.add(start);
            junctions.add(finish);
            return junctions;
        }

        Junction junctionPathsFrom(final Coord start, final Set<Coord> junctions) {
            final Predicate<Coord> isPath = c -> !forest.contains(c) && isInsideMap(c);

            final Queue<Path> paths = new PriorityQueue<>();
            paths.add(new Path(start, 0, new HashSet<>(List.of(start))));

            final Map<Coord, Set<Coord>> junctionPaths = new HashMap<>();

            while (!paths.isEmpty()) {
                final Path actual = paths.poll();
                if (!actual.pos.equals(start) && junctions.contains(actual.pos)) {
                    actual.visited.remove(actual.pos);
                    junctionPaths.put(actual.pos, actual.visited);
                    continue;
                }

                for (Dir dir : Dir.values()) {
                    final Coord nextPos = actual.pos.getAdjacent(dir);
                    if (!nextPos.equals(start) && isPath.test(nextPos) && !actual.visited.contains(nextPos)) {
                        paths.add(actual.next(nextPos, 0));
                    }
                }
            }

            return new Junction(start, junctionPaths);
        }

        static ForestMap from(final List<String> input) {
            final Set<Coord> forest = new HashSet<>();
            final Map<Coord, Slop> slops = new HashMap<>();
            Coord start = null;
            Coord finish = null;
            Coord actual = null;

            for (int row = 0; row < input.size(); row++) {
                final String rowString = input.get(row);
                for (int col = 0; col < rowString.length(); col++) {
                    actual = Coord.of(row, col);
                    final char symbol = rowString.charAt(col);
                    if (symbol == '#') {
                        forest.add(actual);
                    } else if (symbol == '.') {
                        if (start == null) {
                            start = actual;
                        } else {
                            finish = actual;
                        }
                    } else {
                        slops.put(actual, Slop.from(symbol));
                    }

                }
            }
            return new ForestMap(forest, slops, start, finish, actual);
        }
    }

    enum Slop {
        UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

        private final char symbol;

        Slop(char symbol) {
            this.symbol = symbol;
        }

        static Slop from(final char c) {
            return Arrays.stream(Slop.values())
                    .filter(s -> s.symbol == c)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find slop for: " + c));
        }

        boolean canMove(final Dir dir) {
            return this.name().equals(dir.name());
        }
    }

    record Path(Coord pos, int moves, Set<Coord> visited) implements Comparable<Path> {

        Path next(final Coord nextPos, int offset) {
            final Set<Coord> nextVisited = new HashSet<>(visited);
            nextVisited.add(nextPos);
            return new Path(nextPos, moves + offset, nextVisited);
        }

        @Override
        public int compareTo(Path o) {
            return Comparator.comparingInt(Path::moves).reversed().compare(this, o);
        }
    }

    record Junction(Coord pos, Map<Coord, Set<Coord>> pathsToJunctions) {
    }

}
