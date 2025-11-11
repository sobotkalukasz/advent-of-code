package pl.lsobotka.adventofcode.utils;

import java.util.HashSet;
import java.util.List;
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

    public Coord getAdjacent(final Dir dir, int diff) {
        return switch (dir) {
            case UP -> Coord.of(this.row - diff, this.col);
            case DOWN -> Coord.of(this.row + diff, this.col);
            case LEFT -> Coord.of(this.row, this.col - diff);
            case RIGHT -> Coord.of(this.row, this.col + diff);
        };
    }

    public Coord getAdjacent(final AllDir dir) {
        return switch (dir) {
            case UP -> Coord.of(this.row - 1, this.col);
            case DOWN -> Coord.of(this.row + 1, this.col);
            case LEFT -> Coord.of(this.row, this.col - 1);
            case RIGHT -> Coord.of(this.row, this.col + 1);
            case UP_LEFT -> Coord.of(this.row - 1, this.col - 1);
            case UP_RIGHT -> Coord.of(this.row - 1, this.col + 1);
            case DOWN_LEFT -> Coord.of(this.row + 1, this.col - 1);
            case DOWN_RIGHT -> Coord.of(this.row + 1, this.col + 1);
        };
    }

    public List<Coord> getAdjacent(final List<Dir> directions) {
        return directions.stream().map(this::getAdjacent).toList();
    }

    public Coord calculateDiff(final Coord o) {
        return new Coord(row - o.row, col - o.col);
    }

    public Coord moveBy(final Coord o) {
        return new Coord(row + o.row, col + o.col);
    }

    public int distance(final Coord other) {
        return Math.abs(this.row - other.row()) + Math.abs(this.col - other.col());
    }
}