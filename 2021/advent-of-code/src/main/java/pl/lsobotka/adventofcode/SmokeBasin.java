package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/9
 * */
public class SmokeBasin {

    final CaveMap caveMap;

    public SmokeBasin(final List<String> inputRows) {
        this.caveMap = new CaveMap(inputRows);
    }

    public int getRiskLevel() {
        return caveMap.calculateRiskLevel();
    }

    public int getSizeOfLargestBasins(final int basinsToCount) {
        return caveMap.calculateSizeOfBasins(basinsToCount);
    }

    private static class CaveMap {

        private Map<Coordinate, Point> caveMap;
        private int rows;
        private int columns;

        public CaveMap(final List<String> inputRows) {
            init(inputRows);
            markLowPoints();
        }

        private int calculateRiskLevel() {
            return caveMap.values()
                    .stream()
                    .filter(Point::isLowest)
                    .map(Point::getValue)
                    .map(val -> val + 1)
                    .reduce(Integer::sum)
                    .orElse(0);
        }

        private int calculateSizeOfBasins(final int basinsToCount) {
            final Set<Coordinate> lowestCoordinates = caveMap.values()
                    .stream()
                    .filter(Point::isLowest)
                    .map(Point::getCoordinate)
                    .collect(Collectors.toSet());

            final Map<Coordinate, Integer> sizeOfBasins = lowestCoordinates.stream()
                    .collect(Collectors.toMap(Function.identity(), this::getSizeOfBasin));

            return sizeOfBasins.values()
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(basinsToCount)
                    .reduce((a, b) -> a * b)
                    .orElse(0);
        }

        private void init(final List<String> inputRows) {
            final List<List<Integer>> points = inputRows.stream()
                    .map(row -> row.split(""))
                    .map(Arrays::stream)
                    .map(str -> str.map(Integer::parseInt).collect(Collectors.toList()))
                    .collect(Collectors.toList());

            rows = points.size() - 1;
            columns = points.get(0).size() - 1;
            caveMap = new HashMap<>();

            for (int row = 0; row <= rows; row++) {
                for (int col = 0; col <= columns; col++) {
                    final int value = points.get(row).get(col);
                    final Coordinate coordinate = new Coordinate(row, col);
                    caveMap.put(coordinate, new Point(coordinate, value));
                }
            }
        }

        private void markLowPoints() {
            caveMap.forEach((coordinate, point) -> {
                final boolean isLowest = coordinate.getAdjacent(rows, columns)
                        .stream()
                        .map(caveMap::get)
                        .map(Point::getValue)
                        .allMatch(val -> val > point.getValue());
                point.setLowest(isLowest);
            });
        }

        private int getSizeOfBasin(final Coordinate coordinate) {
            final Set<Coordinate> adjacent = getAdjacentBasinCoordinates(coordinate);
            return adjacent.size();
        }

        private Set<Coordinate> getAdjacentBasinCoordinates(final Coordinate coordinate) {
            final Set<Coordinate> adjacent = coordinate.getAdjacent(rows, columns).stream().filter(cord -> {
                final Point point = caveMap.get(cord);
                return !point.isLowest() && !point.isBasin() && point.getValue() < 9;
            }).collect(Collectors.toSet());

            adjacent.forEach(coord -> caveMap.get(coord).setBasin(true));
            final Set<Coordinate> newAdjacent = adjacent.stream()
                    .map(this::getAdjacentBasinCoordinates)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
            adjacent.addAll(newAdjacent);
            adjacent.add(coordinate);
            return adjacent;
        }
    }

    private static class Point {

        private final Coordinate coordinate;
        private final int value;
        private boolean isLowest;
        private boolean isBasin;

        public Point(final Coordinate coordinate, final int value) {
            this.coordinate = coordinate;
            this.value = value;
        }

        public Coordinate getCoordinate() {
            return coordinate;
        }

        public int getValue() {
            return value;
        }

        public boolean isLowest() {
            return isLowest;
        }

        public void setLowest(boolean lowest) {
            isLowest = lowest;
        }

        public boolean isBasin() {
            return isBasin;
        }

        public void setBasin(boolean basin) {
            isBasin = basin;
        }
    }

    private record Coordinate(int row, int column) {

        public List<Coordinate> getAdjacent(final int maxRow, final int maxCol) {
            final List<Coordinate> adjacent = new ArrayList<>();
            if (row - 1 >= 0) {
                adjacent.add(new Coordinate(row - 1, column));
            }
            if (row + 1 <= maxRow) {
                adjacent.add(new Coordinate(row + 1, column));
            }
            if (column - 1 >= 0) {
                adjacent.add(new Coordinate(row, column - 1));
            }
            if (column + 1 <= maxCol) {
                adjacent.add(new Coordinate(row, column + 1));
            }
            return adjacent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) o;
            return row == that.row && column == that.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }
}
