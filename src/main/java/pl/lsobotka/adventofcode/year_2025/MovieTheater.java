package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2025/day/9
 * */
public class MovieTheater {

    private final List<Coord> tiles;

    MovieTheater(final List<String> input) {
        tiles = input.stream().map(Coord::of).toList();
    }

    long largestArea() {
        return getRectangles().stream().mapToLong(Rectangle::area).max().orElse(0L);
    }

    private Set<Rectangle> getRectangles() {
        final Set<Rectangle> rectangles = new HashSet<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                rectangles.add(Rectangle.of(tiles.get(i), tiles.get(j)));
            }
        }
        return rectangles;
    }

    long largesAreaInside() {
        final List<Coord> ordered = orderCorners(tiles);
        final Set<Coord> edges = fillEdges(ordered);
        final Test test = Test.of(new HashSet<>(edges));

        return getRectangles().parallelStream().filter(test::isInside).mapToLong(Rectangle::area).max().orElse(0L);
    }

    private List<Coord> orderCorners(final List<Coord> unordered) {
        final Map<Coord, List<Coord>> adjecentMap = new HashMap<>();
        unordered.forEach(c -> adjecentMap.put(c, new ArrayList<>()));

        final Map<Integer, List<Coord>> byRow = unordered.stream().collect(Collectors.groupingBy(Coord::row));
        for (var entry : byRow.entrySet()) {
            final List<Coord> col = entry.getValue();
            col.sort(Comparator.comparingInt(Coord::col));
            for (int i = 0; i < col.size(); i += 2) {
                connect(adjecentMap, col.get(i), col.get(i + 1));
            }
        }

        final Map<Integer, List<Coord>> byCol = unordered.stream().collect(Collectors.groupingBy(Coord::col));
        for (var entry : byCol.entrySet()) {
            final List<Coord> row = entry.getValue();
            row.sort(Comparator.comparingInt(Coord::row));
            for (int i = 0; i < row.size(); i += 2) {
                connect(adjecentMap, row.get(i), row.get(i + 1));
            }
        }

        final Coord start = unordered.getFirst();
        final List<Coord> ordered = new ArrayList<>();
        ordered.add(start);

        Coord prev = null;
        Coord curr = start;

        while (true) {
            final List<Coord> neigh = adjecentMap.get(curr);
            final Coord next = (neigh.getFirst().equals(prev)) ? neigh.get(1) : neigh.getFirst();

            if (next.equals(start)) {
                break;
            }

            ordered.add(next);
            prev = curr;
            curr = next;
        }

        return ordered;
    }

    private Set<Coord> fillEdges(final List<Coord> corners) {
        final Set<Coord> result = new HashSet<>();
        final int size = corners.size();

        for (int i = 0; i < size; i++) {
            final Coord a = corners.get(i);
            final Coord b = corners.get((i + 1) % size);

            int row1 = a.row();
            int col1 = a.col();
            int row2 = b.row();
            int col2 = b.col();

            if (row1 == row2) {
                int step = Integer.compare(col2, col1);
                for (int y = col1; y != col2; y += step) {
                    result.add(new Coord(row1, y));
                }
            } else if (col1 == col2) {
                int step = Integer.compare(row2, row1);
                for (int x = row1; x != row2; x += step) {
                    result.add(new Coord(x, col1));
                }
            }
        }

        result.add(corners.getFirst());

        return result;
    }

    private void connect(final Map<Coord, List<Coord>> adjecentMap, final Coord a, final Coord b) {
        adjecentMap.get(a).add(b);
        adjecentMap.get(b).add(a);
    }

    record Rectangle(Coord a, Coord b) {

        static Rectangle of(final Coord a, final Coord b) {
            int cmp = Comparator.comparingInt(Coord::row).thenComparingInt(Coord::col).compare(a, b);
            return (cmp <= 0) ? new Rectangle(a, b) : new Rectangle(b, a);
        }

        long area() {
            return (Math.abs((long) a.row() - b.row()) + 1) * (Math.abs(a.col() - b.col()) + 1);
        }

        List<Coord> corners() {
            Coord p1 = new Coord(minRow(), minCol());
            Coord p2 = new Coord(minRow(), maxCol());
            Coord p3 = new Coord(maxRow(), minCol());
            Coord p4 = new Coord(maxRow(), maxCol());

            return List.of(p1, p2, p3, p4);
        }

        int minRow() {
            return Math.min(a.row(), b.row());
        }

        int maxRow() {
            return Math.max(a.row(), b.row());
        }

        int minCol() {
            return Math.min(a.col(), b.col());
        }

        int maxCol() {
            return Math.max(a.col(), b.col());
        }

    }

    record Test(Set<Coord> edges, Map<Integer, int[]> crossingsByRow, List<Segment> rowSegments,
                List<Segment> colSegments) {
        static Test of(final Set<Coord> edges) {
            final Map<Integer, int[]> crossingsByRow = buildCrossingsByRow(edges);
            final List<Segment> rowSegments = buildRowSegments(edges);
            final List<Segment> colSegments = buildColSegments(edges);
            return new Test(edges, crossingsByRow, rowSegments, colSegments);
        }

        private static Map<Integer, int[]> buildCrossingsByRow(Set<Coord> edgeSet) {
            final Map<Integer, List<Integer>> tmp = new HashMap<>();

            for (Coord p : edgeSet) {
                Coord next = new Coord(p.row() + 1, p.col());
                if (edgeSet.contains(next)) {
                    tmp.computeIfAbsent(p.row(), r -> new ArrayList<>()).add(p.col());
                }
            }

            final Map<Integer, int[]> res = new HashMap<>();
            for (var e : tmp.entrySet()) {
                List<Integer> cols = e.getValue();
                cols.sort(Integer::compare);
                res.put(e.getKey(), cols.stream().mapToInt(i -> i).toArray());
            }
            return res;
        }

        private static List<Segment> buildRowSegments(final Set<Coord> edgeSet) {
            final Map<Integer, List<Integer>> byRow = new HashMap<>();
            for (Coord p : edgeSet) {
                byRow.computeIfAbsent(p.row(), r -> new ArrayList<>()).add(p.col());
            }

            final List<Segment> segments = new ArrayList<>();
            for (var e : byRow.entrySet()) {
                int row = e.getKey();
                final List<Integer> cols = e.getValue();
                cols.sort(Integer::compare);

                int start = cols.getFirst(), prev = start;
                for (int i = 1; i < cols.size(); i++) {
                    int cur = cols.get(i);
                    if (cur == prev + 1) {
                        prev = cur;
                    } else {
                        segments.add(new Segment(row, start, prev));
                        start = prev = cur;
                    }
                }
                segments.add(new Segment(row, start, prev));
            }
            return segments;
        }

        private static List<Segment> buildColSegments(final Set<Coord> edgeSet) {
            final Map<Integer, List<Integer>> byCol = new HashMap<>();
            for (Coord p : edgeSet) {
                byCol.computeIfAbsent(p.col(), c -> new ArrayList<>()).add(p.row());
            }

            final List<Segment> segments = new ArrayList<>();
            for (var e : byCol.entrySet()) {
                int col = e.getKey();
                final List<Integer> rows = e.getValue();
                rows.sort(Integer::compare);

                int start = rows.getFirst(), prev = start;
                for (int i = 1; i < rows.size(); i++) {
                    int cur = rows.get(i);
                    if (cur == prev + 1) {
                        prev = cur;
                    } else {
                        segments.add(new Segment(col, start, prev));
                        start = prev = cur;
                    }
                }
                segments.add(new Segment(col, start, prev));
            }
            return segments;
        }

        boolean isInside(final Rectangle rect) {
            if(rect.corners().stream().anyMatch(this::isOutside)){
                return false;
            }

            return !crossEdges(rect.minRow(), rect.maxRow(), rect.minCol(), rect.maxCol());
        }

        boolean isOutside(Coord p) {
            if (edges.contains(p)) {
                return false;
            }

            int[] crossings = crossingsByRow.get(p.row());
            if (crossings == null || crossings.length == 0) {
                return true;
            }

            int col = p.col();
            int idx = Arrays.binarySearch(crossings, col + 1);
            if (idx < 0) {
                idx = -idx - 1;
            }

            int countRight = crossings.length - idx;
            return (countRight % 2) == 0;
        }

        private boolean crossEdges(final int minRow, final int maxRow, final int colMin, final int colMax) {
            for (Segment col : colSegments) {
                int x = col.idx();
                if (x <= colMin || x >= colMax) {
                    continue;
                }
                if (col.from() < minRow && minRow < col.to()) {
                    return true;
                }
                if (col.from() < maxRow && maxRow < col.to()) {
                    return true;
                }
            }
            for (Segment row : rowSegments) {
                int y = row.idx();
                if (y <= minRow || y >= maxRow) {
                    continue;
                }
                if (row.from() < colMin && colMin < row.to()) {
                    return true;
                }
                if (row.from() < colMax && colMax < row.to()) {
                    return true;
                }
            }

            return false;
        }
    }

    record Segment(int idx, int from, int to) {
    }

}
