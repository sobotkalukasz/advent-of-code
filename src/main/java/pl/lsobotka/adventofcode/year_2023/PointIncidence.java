package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2023/day/13
 * */
public class PointIncidence {

    final List<Pattern> patterns;

    public PointIncidence(final List<String> input) {
        this.patterns = Pattern.from(input);
    }

    long findReflections() {
        return patterns.stream().map(Pattern::findReflection).reduce(0, Integer::sum);
    }

    long findReflectionsWithSmudge() {
        return patterns.stream().map(Pattern::findReflectionWithSmudge).reduce(0, Integer::sum);
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
            final Map<Integer, Set<Integer>> rowMap = rowMap(maxRow);
            final int row = reflectionLine(maxRow, rowMap, 0);

            final int maxColumn = rocks.stream().map(Coord::col).reduce(0, Integer::max);
            final Map<Integer, Set<Integer>> columnMap = columnMap(maxColumn);
            final int col = reflectionLine(maxColumn, columnMap, 0);

            return row * 100 + col;
        }

        public int findReflectionWithSmudge() {
            final int maxRow = rocks.stream().map(Coord::row).reduce(0, Integer::max);
            final Map<Integer, Set<Integer>> rowMap = rowMap(maxRow);
            final int rowToExclude = reflectionLine(maxRow, rowMap, 0);
            final int row = reflectionLineWithSludge(maxRow, rowMap, rowToExclude);

            final int maxColumn = rocks.stream().map(Coord::col).reduce(0, Integer::max);
            final Map<Integer, Set<Integer>> columnMap = columnMap(maxColumn);
            final int colToExclude = reflectionLine(maxColumn, columnMap, 0);
            final int col = reflectionLineWithSludge(maxColumn, columnMap, colToExclude);

            return row * 100 + col;
        }

        int reflectionLine(final int max, final Map<Integer, Set<Integer>> map, final int exclude) {
            for (int actual = 1; actual <= max; actual++) {
                if (actual != exclude) {
                    for (int offset = 0; offset <= max - actual || offset < actual - 1; offset++) {
                        final int from = actual + offset;
                        final int to = actual - offset - 1;
                        if (map.getOrDefault(from, Collections.emptySet())
                                .equals(map.getOrDefault(to, Collections.emptySet()))) {
                            if (from == max || to == 0) {
                                return actual;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
            return 0;
        }

        private int reflectionLineWithSludge(final int max, final Map<Integer, Set<Integer>> map, final int exclude) {
            int value = 0;
            for (int actual = 0; actual < max && value == 0; actual++) {
                if (exclude > 0 && actual == exclude) {
                    continue;
                }
                for (int next = actual + 1; next <= max && value == 0; next++) {
                    if (next == exclude) {
                        continue;
                    }
                    final Set<Integer> actualList = map.getOrDefault(actual, Collections.emptySet());
                    final Set<Integer> nextList = map.getOrDefault(next, Collections.emptySet());

                    if (Math.abs(actualList.size() - nextList.size()) == 1) {
                        final Set<Integer> left = new HashSet<>(actualList);
                        final Set<Integer> right = new HashSet<>(nextList);

                        final Set<Integer> intersection = new HashSet<>(left);
                        intersection.retainAll(right);

                        left.removeAll(intersection);
                        right.removeAll(intersection);

                        if ((left.isEmpty() && right.size() == 1) || (left.size() == 1 && right.isEmpty())) {
                            final Map<Integer, Set<Integer>> leftMap = new HashMap<>(map);
                            leftMap.put(actual, nextList);
                            value = reflectionLine(max, leftMap, exclude);
                            if (value == 0) {
                                final Map<Integer, Set<Integer>> rightMap = new HashMap<>(map);
                                rightMap.put(next, actualList);
                                value = reflectionLine(max, rightMap, exclude);
                            }
                        }
                    }
                }
            }
            return value;
        }

        private Map<Integer, Set<Integer>> rowMap(final int maxRow) {
            final Map<Integer, Set<Integer>> rowMap = new HashMap<>();
            for (int i = 0; i <= maxRow; i++) {
                final int index = i;
                final Set<Integer> columns = rocks.stream()
                        .filter(c -> c.row() == index)
                        .map(Coord::col)
                        .collect(Collectors.toSet());
                rowMap.put(index, columns);
            }
            return rowMap;
        }

        private Map<Integer, Set<Integer>> columnMap(final int maxColumn) {
            final Map<Integer, Set<Integer>> columnMap = new HashMap<>();
            for (int i = 0; i <= maxColumn; i++) {
                final int index = i;
                final Set<Integer> rows = rocks.stream()
                        .filter(c -> c.col() == index)
                        .map(Coord::row)
                        .collect(Collectors.toSet());
                columnMap.put(index, rows);
            }
            return columnMap;
        }
    }
}
