package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;

public record Coordinate(int row, int column) {

    public List<Coordinate> getAdjacent(final int maxRow, final int maxCol) {
        final List<Coordinate> adjacent = new ArrayList<>();
        if (row > 0) {
            adjacent.add(new Coordinate(row - 1, column));
        }
        if (row < maxRow) {
            adjacent.add(new Coordinate(row + 1, column));
        }
        if (column > 0) {
            adjacent.add(new Coordinate(row, column - 1));
        }
        if (column < maxCol) {
            adjacent.add(new Coordinate(row, column + 1));
        }
        return adjacent;
    }

    public List<Coordinate> getAdjacentWithDiagonal(final int maxRow, final int maxColumn) {
        final List<Coordinate> adjacent = getAdjacent(maxRow, maxColumn);
        adjacent.addAll(getAdjacentDiagonal(maxRow, maxColumn));
        return adjacent;
    }

    private List<Coordinate> getAdjacentDiagonal(final int maxRow, final int maxColumn) {
        final List<Coordinate> adjacent = new ArrayList<>();
        if (row > 0) {
            if (column > 0) {
                adjacent.add(new Coordinate(row - 1, column - 1));
            }
            if (column < maxColumn) {
                adjacent.add(new Coordinate(row - 1, column + 1));
            }
        }
        if (row < maxRow) {
            if (column > 0) {
                adjacent.add(new Coordinate(row + 1, column - 1));
            }
            if (column < maxColumn) {
                adjacent.add(new Coordinate(row + 1, column + 1));
            }
        }
        return adjacent;
    }

}
