package pl.lsobotka.adventofcode.year_2021;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/20
 * */
public class TrenchMap {
    private final static int ALGORITHM_INDEX = 0;
    private final static int EMPTY_INDEX = 1;

    final String[] algorithm;
    final Set<Coordinate> initialCoordinates;
    final int initialRowSize;
    final int initialColumnSize;

    public TrenchMap(final List<String> input) {
        algorithm = input.get(ALGORITHM_INDEX).split("");
        final List<String> map = input.subList(EMPTY_INDEX + 1, input.size());
        initialRowSize = map.size();
        initialColumnSize = map.get(0).length();

        initialCoordinates = new HashSet<>();
        for (int row = 0; row < initialRowSize; row++) {
            final String rowString = map.get(row);
            for (int col = 0; col < initialColumnSize; col++) {
                if (rowString.charAt(col) == '#') {
                    initialCoordinates.add(new Coordinate(row, col));
                }
            }
        }
    }

    public int enhancePictureAndCountLightPixels(int times) {
        Set<Coordinate> enhancedMap = new HashSet<>(initialCoordinates);

        while (times-- > 0) {
            enhancedMap = apply(enhancedMap, times);
        }

        return enhancedMap.size();
    }

    private Set<Coordinate> apply(final Set<Coordinate> map, final int time) {
        final Set<Coordinate> coordinates = adjustCoordinates(map);

        final List<Coordinate> sortedRows = coordinates.stream().sorted().collect(Collectors.toList());
        final int minRow = sortedRows.get(0).row();
        final int maxRow = sortedRows.get(sortedRows.size() - 1).row();
        final int minColumn = coordinates.stream()
                .reduce((c, o) -> c.column() < o.column() ? c : o)
                .orElseThrow(IllegalArgumentException::new)
                .column();
        final int maxColumn = coordinates.stream()
                .reduce((c, o) -> c.column() > o.column() ? c : o)
                .orElseThrow(IllegalArgumentException::new)
                .column();

        fillNewCoordinates(coordinates, time, minRow, maxRow, minColumn, maxColumn);

        final Set<Coordinate> tempMap = new HashSet<>();

        for (int row = minRow - 1; row <= maxRow + 1; row++) {
            for (int col = minColumn - 1; col <= maxColumn + 1; col++) {
                final Coordinate actual = new Coordinate(row, col);
                final List<Coordinate> adjacent = actual.getAdjacentWithDiagonal(maxRow + 2, maxColumn + 2);
                adjacent.add(actual);
                Collections.sort(adjacent);
                if (adjacent.size() == 9) {
                    Collections.sort(adjacent);
                    final String binary = adjacent.stream()
                            .map(c -> coordinates.contains(c) ? "1" : "0")
                            .collect(Collectors.joining());
                    final int code = Integer.parseInt(binary, 2);
                    if (algorithm[code].equals("#")) {
                        tempMap.add(actual);
                    }
                }
            }
        }

        return tempMap;
    }

    private Set<Coordinate> adjustCoordinates(final Set<Coordinate> coordinates) {
        final Coordinate toAdd = new Coordinate(2, 2);
        return coordinates.stream().map(c -> c.add(toAdd)).collect(Collectors.toSet());
    }

    private void fillNewCoordinates(final Set<Coordinate> coordinates, final int times, final int minRow,
            final int maxRow, final int minColumn, final int maxColumn) {
        if (algorithm[0].equals("#") && times % 2 == 0) {
            for (int row = minRow - 2; row <= maxRow + 2; row++) {
                if (row < minRow || row > maxRow) {
                    for (int col = minColumn - 2; col <= maxColumn + 2; col++) {
                        coordinates.add(new Coordinate(row, col));
                    }
                } else {
                    coordinates.add(new Coordinate(row, minColumn - 2));
                    coordinates.add(new Coordinate(row, minColumn - 1));
                    coordinates.add(new Coordinate(row, maxColumn + 1));
                    coordinates.add(new Coordinate(row, maxColumn + 2));
                }
            }
        }
    }
}
