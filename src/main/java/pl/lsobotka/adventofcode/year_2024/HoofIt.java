package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/10
 * */
public class HoofIt {

    final Map<Coord, Point> hillMap;
    final List<Coord> trailHeads;

    HoofIt(final List<String> lines) {
        this.hillMap = new HashMap<>();
        this.trailHeads = new ArrayList<>();

        for (int row = 0; row < lines.size(); row++) {
            final String rowStr = lines.get(row);
            for (int col = 0; col < rowStr.length(); col++) {
                final int hill = Integer.parseInt(String.valueOf(rowStr.charAt(col)));
                final Coord coord = Coord.of(row, col);
                hillMap.put(coord, new Point(coord, hill));
                if (hill == 0) {
                    trailHeads.add(coord);
                }
            }
        }
    }

    long sumOfTrailHeadScores() {
        return trailHeads.stream()
                .map(start -> countPossible(start, list -> list.stream().distinct().count()))
                .reduce(0L, Long::sum);
    }

    long sumOfTrailHeadRatings() {
        return trailHeads.stream()
                .map(start -> countPossible(start, list -> (long) list.size()))
                .reduce(0L, Long::sum);
    }

    private long countPossible(final Coord start, final ToLongFunction<List<Coord>> countFunction) {
        final List<Coord> possible = new ArrayList<>();

        final Queue<Path> paths = new PriorityQueue<>();
        paths.add(new Path(start, 0));

        while (!paths.isEmpty()) {
            final Path current = paths.poll();
            if (hillMap.get(current.pos).elevation == 9) {
                possible.add(current.pos);
            } else {
                getPossibleMoves(current.pos).stream().map(c -> new Path(c, current.moves + 1)).forEach(paths::add);
            }
        }

        return countFunction.applyAsLong(possible);
    }

    private Set<Coord> getPossibleMoves(final Coord coord) {
        final Point actualPoint = hillMap.get(coord);
        return coord.getDirectAdjacent()
                .stream()
                .filter(hillMap::containsKey)
                .filter(c -> actualPoint.canMoveTo(hillMap.get(c)))
                .collect(Collectors.toSet());
    }

    private record Point(Coord coord, int elevation) {
        public boolean canMoveTo(final Point target) {
            return target.elevation - this.elevation == 1;
        }

    }

    private record Path(Coord pos, int moves) implements Comparable<Path> {

        @Override
        public int compareTo(Path o) {
            return Comparator.comparing(Path::moves).compare(this, o);
        }
    }
}
