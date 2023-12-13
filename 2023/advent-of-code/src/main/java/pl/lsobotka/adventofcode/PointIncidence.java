package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.lsobotka.adventofcode.utils.Coord;

public class PointIncidence {

    final List<Pattern> patterns;

    public PointIncidence(final List<String> input) {
        this.patterns = Pattern.from(input);
    }

    long findReflections() {
        return patterns.stream().map(Pattern::findReflection).reduce(0, Integer::sum);
    }

    record Pattern(List<Coord> rocks) {

        static List<Pattern> from(final List<String> input) {
            final List<Pattern> patterns = new ArrayList<>();

            final List<Coord> coords = new ArrayList<>();
            int rowCounter = 0;

            for (String row : input) {
                if (row.isEmpty()) {
                    patterns.add(new Pattern(new ArrayList<>(coords)));
                    coords.clear();
                    rowCounter = 0;
                } else {
                    for (int col = 0; col < row.length(); col++) {
                        if (row.charAt(col) == '#') {
                            coords.add(Coord.of(rowCounter, col));
                        }
                    }
                    rowCounter++;
                }
            }

            patterns.add(new Pattern(new ArrayList<>(coords)));
            return patterns;
        }

        public int findReflection() {
            final int maxRow = rocks.stream().map(Coord::row).reduce(0, Integer::max);
            final Map<Integer, List<Integer>> rowMap = rowMap(maxRow);

            final int row = reflectionLine(maxRow, rowMap);

            final int maxColumn = rocks.stream().map(Coord::col).reduce(0, Integer::max);
            final Map<Integer, List<Integer>> columnMap = columnMap(maxColumn);

            final int col = reflectionLine(maxColumn, columnMap);

            return row * 100 + col;
        }

        int reflectionLine(final int max, final Map<Integer, List<Integer>> map) {
            for (int actual = 1; actual <= max; actual++) {
                for (int offset = 0; offset <= max - actual || offset < actual - 1; offset++) {
                    final int from = actual + offset;
                    final int to = actual - offset - 1;
                    if (map.getOrDefault(from, Collections.emptyList())
                            .equals(map.getOrDefault(to, Collections.emptyList()))) {
                        if (from == max || to == 0) {
                            return actual;
                        }
                    } else {
                        break;
                    }
                }
            }
            return 0;
        }

        private Map<Integer, List<Integer>> rowMap(final int maxRow) {
            final Map<Integer, List<Integer>> rowMap = new HashMap<>();
            for (int i = 0; i <= maxRow; i++) {
                final int index = i;
                final List<Integer> columns = rocks.stream()
                        .filter(c -> c.row() == index)
                        .map(Coord::col)
                        .sorted()
                        .toList();
                rowMap.put(index, columns);
            }
            return rowMap;
        }

        private Map<Integer, List<Integer>> columnMap(final int maxColumn) {
            final Map<Integer, List<Integer>> columnMap = new HashMap<>();
            for (int i = 0; i <= maxColumn; i++) {
                final int index = i;
                final List<Integer> rows = rocks.stream()
                        .filter(c -> c.col() == index)
                        .map(Coord::row)
                        .sorted()
                        .toList();
                columnMap.put(index, rows);
            }
            return columnMap;
        }
    }
}
