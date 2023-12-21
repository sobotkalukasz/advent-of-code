package pl.lsobotka.adventofcode.year_2019;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/*
 * https://adventofcode.com/2019/day/18
 * */
public class ManyWorldsInterpretation {

    private final Map<Coord, Character> doors;
    private final Map<Coord, Character> keys;
    private final Set<Coord> walls;

    private Coord start;

    public ManyWorldsInterpretation(final List<String> input) {

        this.doors = new HashMap<>();
        this.keys = new HashMap<>();
        this.walls = new HashSet<>();

        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            for (int col = 0; col < rowString.length(); col++) {
                final char at = rowString.charAt(col);
                if (at == '#') {
                    walls.add(Coord.of(row, col));
                } else if (at == '@') {
                    this.start = Coord.of(row, col);
                } else if (at != '.') {
                    if (Character.isUpperCase(at)) {
                        doors.put(Coord.of(row, col), at);
                    } else if (Character.isLowerCase(at)) {
                        keys.put(Coord.of(row, col), at);
                    }
                }
            }
        }
    }

    public int shortestPathToCollectAllKeys() {
        final Map<Coord, Map<Set<Character>, Integer>> visitedCache = new HashMap<>();

        final Queue<Path> paths = new PriorityQueue<>();
        paths.add(new Path(start, 0, new HashSet<>()));

        int actualBest = Integer.MAX_VALUE;

        while (!paths.isEmpty()) {
            final Path actual = paths.poll();
            if (actual.keys().size() == this.keys.size()) {
                actualBest = Math.min(actualBest, actual.moves);
                continue;
            }

            if (visitedCache.containsKey(actual.actual)) {
                final Map<Set<Character>, Integer> map = visitedCache.get(actual.actual);
                if (!map.containsKey(actual.keys) || map.get(actual.keys) > actual.moves + 1) {
                    map.put(actual.keys, actual.moves + 1);
                } else {
                    continue;
                }
            } else {
                visitedCache.put(actual.actual, new HashMap<>(Map.of(actual.keys, actual.moves + 1)));
            }

            for (Coord next : actual.actual.getAdjacent()) {
                if (doors.containsKey(next)) {
                    final Character door = doors.get(next);
                    if (actual.keys().contains(Character.toLowerCase(door))) {
                        paths.add(new Path(next, actual.moves + 1, actual.keys));
                    }
                } else if (keys.containsKey(next) && !actual.keys.contains(keys.get(next))) {
                    final Set<Character> nextKeys = new HashSet<>(actual.keys);
                    nextKeys.add(keys.get(next));
                    paths.add(new Path(next, actual.moves + 1, nextKeys));
                } else if (!walls.contains(next)) {
                    paths.add(new Path(next, actual.moves + 1, actual.keys));
                }
            }
        }

        return actualBest;
    }

    private record Coord(int row, int col) {
        static Coord of(final int row, final int col) {
            return new Coord(row, col);
        }

        private Set<Coord> getAdjacent() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(this.row + 1, this.col));
            adjacent.add(Coord.of(this.row - 1, this.col));
            adjacent.add(Coord.of(this.row, this.col + 1));
            adjacent.add(Coord.of(this.row, this.col - 1));
            return adjacent;
        }
    }

    private record Path(Coord actual, int moves, Set<Character> keys) implements Comparable<Path> {

        @Override
        public int compareTo(Path o) {
            return Comparator.comparingInt(Path::moves).compare(this, o);
        }
    }

}
