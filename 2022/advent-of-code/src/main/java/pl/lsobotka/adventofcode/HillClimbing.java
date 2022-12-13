package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/12
 * */
public class HillClimbing {

    final Map<Coord, Point> hillMap;

    public HillClimbing(List<String> input) {
        this.hillMap = initHillMap(input);
    }

    private Map<Coord, Point> initHillMap(List<String> input) {
        final Map<Coord, Point> hillMap = new HashMap<>();
        for (int row = 0; row < input.size(); row++) {
            final char[] splitRow = input.get(row).toCharArray();
            for (int col = 0; col < splitRow.length; col++) {
                final Coord coord = new Coord(row, col);
                hillMap.put(coord, Point.from(coord, splitRow[col]));
            }
        }
        return hillMap;
    }

    public int getBestStepsToTopFromStartPoint() {
        final Point start = getStart();
        return getBestStepsToTop(start);
    }

    public int getBestStepsToTopFromPossibleStartingPoints() {
        final Point start = getStart();
        final Set<Coord> lowestPossibleStartingPoints = getPossibleStartingPointsFrom(start, new HashSet<>());

        return lowestPossibleStartingPoints.stream()
                .map(this.hillMap::get)
                .mapToInt(this::getBestStepsToTop)
                .min()
                .orElse(0);

    }

    private Set<Coord> getPossibleStartingPointsFrom(final Point current, final Set<Coord> possibleStartingPoints) {
        final Set<Point> nextPoints = current.coord.getAdjacent()
                .stream()
                .filter(c -> hillMap.containsKey(c) && !possibleStartingPoints.contains(c))
                .map(this.hillMap::get)
                .filter(p -> p.elevation == current.elevation)
                .collect(Collectors.toSet());

        nextPoints.forEach(next -> {
            possibleStartingPoints.add(next.coord);
            getPossibleStartingPointsFrom(next, possibleStartingPoints);
        });

        return possibleStartingPoints;
    }

    private int getBestStepsToTop(final Point start) {
        final Point top = getTop();

        final Queue<ClimbingPath> paths = new PriorityQueue<>();
        Set<Coord> visited = new HashSet<>();

        paths.add(new ClimbingPath(start.coord, 0));
        visited.add(start.coord);

        while (!paths.isEmpty() && !paths.peek().pos.equals(top.coord)) {
            final ClimbingPath current = paths.poll();
            getPossibleMoves(current.pos).stream()
                    .filter(c -> !visited.contains(c))
                    .map(c -> new ClimbingPath(c, current.moves + 1))
                    .forEach(next -> {
                        visited.add(next.pos);
                        paths.add(next);
                    });
        }

        return Optional.ofNullable(paths.peek()).map(ClimbingPath::moves).orElse(0);
    }

    private List<Coord> getPossibleMoves(final Coord coord) {
        final Point actualPoint = hillMap.get(coord);
        return coord.getAdjacent()
                .stream()
                .filter(hillMap::containsKey)
                .filter(c -> actualPoint.canMoveTo(hillMap.get(c)))
                .collect(Collectors.toList());
    }

    private Point getStart() {
        return hillMap.values()
                .stream()
                .filter(Point::start)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Start point not present"));
    }

    private Point getTop() {
        return hillMap.values()
                .stream()
                .filter(Point::top)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Top point not present"));
    }

    private record Coord(int row, int column) {

        List<Coord> getAdjacent() {
            return List.of(new Coord(this.row + 1, this.column), //
                    new Coord(this.row - 1, this.column), //
                    new Coord(this.row, this.column + 1), //
                    new Coord(this.row, this.column - 1));
        }
    }

    private record Point(Coord coord, int elevation, boolean top, boolean start) {

        public boolean canMoveTo(final Point target) {
            return target.elevation - this.elevation <= 1;
        }

        static Point from(final Coord coord, final char c) {
            Point point;
            if (c == 'S') {
                point = Point.start(coord);
            } else if (c == 'E') {
                point = Point.top(coord);
            } else {
                point = Point.of(coord, c);
            }
            return point;
        }

        private static Point of(final Coord coord, final int elevation) {
            return new Point(coord, elevation, false, false);
        }

        private static Point start(final Coord coord) {
            return new Point(coord, 'a', false, true);
        }

        private static Point top(final Coord coord) {
            return new Point(coord, 'z', true, false);
        }
    }

    private record ClimbingPath(Coord pos, int moves) implements Comparable<ClimbingPath> {

        @Override
        public int compareTo(ClimbingPath o) {
            return Comparator.comparing(ClimbingPath::moves).compare(this, o);
        }
    }
}
