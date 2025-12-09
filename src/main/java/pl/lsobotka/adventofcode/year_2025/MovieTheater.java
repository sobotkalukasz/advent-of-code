package pl.lsobotka.adventofcode.year_2025;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        final Set<Rectangle> rectangles = new HashSet<>();
        for (int i = 0; i < tiles.size(); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                rectangles.add(Rectangle.of(tiles.get(i), tiles.get(j)));
            }
        }

        return rectangles.stream()
                .mapToLong(Rectangle::area)
                .max()
                .orElse(0L);
    }

    record Rectangle(Coord topLeft, Coord bottomRight) {

        static Rectangle of(final Coord a, final Coord b) {
            int cmp = Comparator.comparingInt(Coord::row).thenComparingInt(Coord::col).compare(a, b);
            return (cmp <= 0) ? new Rectangle(a, b) : new Rectangle(b, a);
        }

        long area() {
            return (Math.abs((long)bottomRight.row() - topLeft.row()) + 1) * (Math.abs(bottomRight.col() - topLeft.col())
                    + 1);
        }

    }

}
