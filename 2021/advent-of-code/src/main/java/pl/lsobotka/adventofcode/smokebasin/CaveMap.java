package pl.lsobotka.adventofcode.smokebasin;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.Coordinate;

class CaveMap {

    private Map<Coordinate, Point> caveMap;
    private int rows;
    private int columns;

    public CaveMap(final List<String> inputRows) {
        init(inputRows);
        markLowPoints();
    }

    public int calculateRiskLevel() {
        return caveMap.values()
                .stream()
                .filter(Point::isLowest)
                .map(Point::getValue)
                .map(val -> val + 1)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public int calculateSizeOfBasins(final int basinsToCount) {
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
