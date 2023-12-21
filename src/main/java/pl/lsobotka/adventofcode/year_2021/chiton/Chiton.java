package pl.lsobotka.adventofcode.year_2021.chiton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import pl.lsobotka.adventofcode.year_2021.Coordinate;

/*
 * https://adventofcode.com/2021/day/15
 * */
public class Chiton {
    private final Map<Coordinate, Integer> caveMap;
    private final Map<Coordinate, Integer> fullCaveMap;

    public Chiton(final List<String> input) {
        caveMap = initMap(input);
        fullCaveMap = initFullSizeMap(caveMap);
    }

    public long findLowestPath() {
        return findLowestPath(caveMap);
    }

    public long findLowestPathOnLargeMap() {
        return findLowestPath(fullCaveMap);
    }

    private long findLowestPath(final Map<Coordinate, Integer> caveMap) {
        final Queue<CavePath> paths = new PriorityQueue<>();
        final Set<Coordinate> visited = new HashSet<>();

        final int maxRow = caveMap.keySet().stream().map(Coordinate::row).max(Integer::compareTo).orElse(0);
        final int maxColumn = caveMap.keySet().stream().map(Coordinate::column).max(Integer::compareTo).orElse(0);
        final Coordinate start = new Coordinate(0, 0);
        final Coordinate end = new Coordinate(maxRow, maxColumn);

        paths.add(new CavePath(start, 0));
        while (!paths.peek().actual().equals(end)) {
            final CavePath path = paths.poll();
            path.actual()
                    .getAdjacent(maxRow, maxColumn)
                    .stream()
                    .filter(c -> !visited.contains(c))
                    .map(c -> new CavePath(c, path.currentValue() + caveMap.get(c)))
                    .forEach(s -> {
                        visited.add(s.actual());
                        paths.add(s);
                    });

        }
        return paths.peek().currentValue();
    }

    private Map<Coordinate, Integer> initMap(final List<String> input) {
        final Map<Coordinate, Integer> caveMap = new HashMap<>();
        for (int row = 0; row < input.size(); row++) {
            final String[] split = input.get(row).split("");
            for (int col = 0; col < split.length; col++) {
                caveMap.put(new Coordinate(row, col), Integer.parseInt(split[col]));
            }
        }
        return caveMap;
    }

    private Map<Coordinate, Integer> initFullSizeMap(final Map<Coordinate, Integer> map) {
        final int maxRow = caveMap.keySet().stream().map(Coordinate::row).max(Integer::compareTo).orElse(0);
        final int maxColumn = caveMap.keySet().stream().map(Coordinate::column).max(Integer::compareTo).orElse(0);

        final Map<Coordinate, Integer> fullSizeMap = new HashMap<>();

        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxColumn; col++) {
                final Coordinate actual = new Coordinate(row, col);
                int value = map.get(actual);
                for (int r = 0; r < 5; r++) {
                    final int newRowValue = value + r >= 10 ? ((value + r) % 10) + 1 : (value + r) % 10;
                    for (int c = 0; c < 5; c++) {
                        final int newRow = actual.row() + (maxRow + 1) * r;
                        final int newCol = actual.column() + (maxColumn + 1) * c;
                        final int newColumnValue =
                                newRowValue + c >= 10 ? ((newRowValue + c) % 10) + 1 : (newRowValue + c) % 10;
                        final Coordinate newCoordinate = new Coordinate(newRow, newCol);
                        fullSizeMap.put(newCoordinate, newColumnValue);

                    }
                }

            }
        }
        return fullSizeMap;
    }

}
