package pl.lsobotka.adventofcode.dumbooctopus;

import java.util.ArrayList;
import java.util.List;

record Coordinate(int row, int column) {

    List<Coordinate> getAdjacentWithDiagonalCoordinates(final int maxRow, final int maxColumn) {
        List<Coordinate> adjacent = new ArrayList<>();
        if (row > 0) {
            if (column > 0) {
                adjacent.add(new Coordinate(row - 1, column - 1));
            }
            adjacent.add(new Coordinate(row - 1, column));
            if (column < maxColumn) {
                adjacent.add(new Coordinate(row - 1, column + 1));
            }
        }

        if (column > 0) {
            adjacent.add(new Coordinate(row, column - 1));
        }
        if (column < maxColumn) {
            adjacent.add(new Coordinate(row, column + 1));
        }

        if (row < maxRow) {
            if (column > 0) {
                adjacent.add(new Coordinate(row + 1, column - 1));
            }
            adjacent.add(new Coordinate(row + 1, column));
            if (column < maxColumn) {
                adjacent.add(new Coordinate(row + 1, column + 1));
            }
        }

        return adjacent;
    }
}
