package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2022/day/14
 * */
public class RegolithReservoir {

    final Map<Coord, Type> caveMap;
    final Coord sandStartPos;
    final int maxRow;
    final int minCol;
    final int maxCol;

    public RegolithReservoir(final List<String> input) {

        caveMap = input.stream()
                .map(this::placeRocks)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Point::coord, Point::type, (a, b) -> a));

        maxRow = caveMap.keySet().stream().mapToInt(Coord::row).max().orElse(0);
        minCol = caveMap.keySet().stream().mapToInt(Coord::col).min().orElse(0);
        maxCol = caveMap.keySet().stream().mapToInt(Coord::col).max().orElse(0);

        sandStartPos = new Coord(0, 500);
    }

    private List<Point> placeRocks(final String rawRock) {
        final List<Point> rocks = new ArrayList<>();
        final String[] paths = rawRock.replaceAll(" ", "").split("->");

        for (int i = 1; i < paths.length; i++) {
            final String[] start = paths[i - 1].split(",");
            final int startRow = Integer.parseInt(start[1]);
            final int startCol = Integer.parseInt(start[0]);

            final String[] end = paths[i].split(",");
            final int endRow = Integer.parseInt(end[1]);
            final int endCol = Integer.parseInt(end[0]);

            if (startRow == endRow) {
                final int from = Math.min(startCol, endCol);
                final int to = Math.max(startCol, endCol);
                rocks.addAll(IntStream.range(from, to + 1)
                        .mapToObj(col -> new Point(new Coord(startRow, col), Type.ROCK))
                        .toList());
            } else {
                final int from = Math.min(startRow, endRow);
                final int to = Math.max(startRow, endRow);
                rocks.addAll(IntStream.range(from, to + 1)
                        .mapToObj(row -> new Point(new Coord(row, startCol), Type.ROCK))
                        .toList());
            }
        }
        return rocks;
    }

    long countSandUntilFallOf() {

        boolean fellOut = false;

        while (!fellOut) {
            Coord current = sandStartPos;
            Coord next = current;

            while (!fellOut && Objects.nonNull(next)) {
                final List<List<Coord>> possible = next.possibleMoves();

                next = null;
                for (int i = 0; i < possible.size() && Objects.isNull(next) && !fellOut; i++) {
                    final List<Coord> coords = possible.get(i);
                    for (int j = 0; j < coords.size() && !fellOut; j++) {
                        fellOut = checkIfFellOut(coords.get(j));
                        final boolean isEmpty = isEmpty(coords.get(j));
                        final boolean isLastElement = j == coords.size() - 1;
                        if (!fellOut && isEmpty && isLastElement) {
                            next = coords.get(j);
                        }
                    }
                }

                if (!fellOut && Objects.isNull(next)) {
                    caveMap.put(current, Type.SAND);
                } else {
                    current = next;
                }
            }
        }

        return caveMap.values().stream().filter(p -> p.equals(Type.SAND)).count();
    }

    private boolean checkIfFellOut(final Coord coord) {
        return coord.col < minCol || coord.col > maxCol || coord.row > maxRow;
    }

    private boolean isEmpty(final Coord coord) {
        return !caveMap.containsKey(coord);
    }

    long countSandUntilFull() {

        while (isEmpty(sandStartPos)) {
            Coord current = sandStartPos;
            Coord next = current;

            while (isEmpty(sandStartPos) && Objects.nonNull(next)) {
                final List<List<Coord>> positions = next.possibleMoves();

                next = null;
                for (int i = 0; i < positions.size() && Objects.isNull(next); i++) {
                    final List<Coord> coords = positions.get(i);
                    for (int j = 0; j < coords.size(); j++) {
                        final boolean canMove = isEmpty(coords.get(j)) && isNotBottom(coords.get(j));
                        final boolean isLastElement = j == coords.size() - 1;
                        if (canMove && isLastElement) {
                            next = coords.get(j);
                        }
                    }
                }

                if (Objects.isNull(next)) {
                    caveMap.put(current, Type.SAND);
                } else {
                    current = next;
                }
            }
        }

        return caveMap.values().stream().filter(p -> p.equals(Type.SAND)).count();
    }

    private boolean isNotBottom(final Coord coord) {
        return coord.row < maxRow + 2;
    }

    record Point(Coord coord, Type type) {
    }

    record Coord(int row, int col) {
        List<List<Coord>> possibleMoves() {
            final List<List<Coord>> moves = new ArrayList<>();
            moves.add(down());
            moves.add(left());
            moves.add(right());
            return moves;
        }

        private List<Coord> down() {
            return List.of(new Coord(row + 1, col));
        }

        private List<Coord> left() {
            return List.of(new Coord(row, col - 1), new Coord(row + 1, col - 1));
        }

        private List<Coord> right() {
            return List.of(new Coord(row, col + 1), new Coord(row + 1, col + 1));
        }

    }

    enum Type {
        ROCK, SAND
    }
}
