package pl.lsobotka.adventofcode.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Board(Set<Coord> walls, Coord start, Coord end, Coord corner) {
    public static Board from(final List<String> input) {
        Set<Coord> walls = new HashSet<>();
        Coord start = Coord.of(0, 0);
        Coord end = Coord.of(0, 0);
        Coord corner = Coord.of(0, 0);

        for (int row = 0; row < input.size(); row++) {
            final String rowS = input.get(row);
            for (int col = 0; col < rowS.length(); col++) {
                switch (rowS.charAt(col)) {
                case '#' -> walls.add(Coord.of(row, col));
                case 'E' -> end = Coord.of(row, col);
                case 'S' -> start = Coord.of(row, col);
                default -> { // Do nothing
                }
                }
                corner = Coord.of(row, col);
            }
        }
        return new Board(walls, start, end, corner);
    }

    public boolean isWall(final Coord coord) {
        return walls.contains(coord);
    }

    public boolean isEnd(final Coord coord) {
        return end.equals(coord);
    }
}
