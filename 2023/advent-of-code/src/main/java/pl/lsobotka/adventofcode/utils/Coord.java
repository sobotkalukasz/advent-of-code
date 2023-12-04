package pl.lsobotka.adventofcode.utils;

import java.util.HashSet;
import java.util.Set;

public record Coord(int row, int col) {
    public static Coord of(final int row, final int col) {
        return new Coord(row, col);
    }

    public Set<Coord> getDirectAdjacent() {
        final Set<Coord> adjacent = new HashSet<>();
        adjacent.add(Coord.of(row - 1, col));
        adjacent.add(Coord.of(row + 1, col));
        adjacent.add(Coord.of(row, col - 1));
        adjacent.add(Coord.of(row, col + 1));
        return adjacent;
    }

    public Set<Coord> getAllAdjacent() {
        final Set<Coord> adjacent = getDirectAdjacent();
        adjacent.add(Coord.of(row + 1, col + 1));
        adjacent.add(Coord.of(row + 1, col - 1));
        adjacent.add(Coord.of(row - 1, col - 1));
        adjacent.add(Coord.of(row - 1, col + 1));
        return adjacent;
    }

    public Coord getAdjacent(final Dir dir) {
        return switch (dir) {
            case UP -> Coord.of(this.row - 1, this.col);
            case DOWN -> Coord.of(this.row + 1, this.col);
            case LEFT -> Coord.of(this.row, this.col - 1);
            case RIGHT -> Coord.of(this.row, this.col + 1);
        };
    }
}