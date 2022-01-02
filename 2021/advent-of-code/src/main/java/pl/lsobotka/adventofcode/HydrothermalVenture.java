package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2021/day/5
 * */
public class HydrothermalVenture {
    final List<Vent> vents;
    final Board board;

    public HydrothermalVenture(final List<String> ventsLines) {
        this.vents = ventsLines.stream().map(Vent::new).collect(Collectors.toList());
        final int maxColumn = vents.stream().map(Vent::getMaxColumn).reduce(Math::max).orElse(0);
        final int maxRow = vents.stream().map(Vent::getMaxRow).reduce(Math::max).orElse(0);
        board = new Board(new Coords(maxColumn, maxRow));
    }

    public long countOverlapsLines() {
        vents.forEach(board::markLines);
        return board.countOverlaps(2);
    }

    public long countOverlapsAll() {
        vents.forEach(board::markAll);
        return board.countOverlaps(2);
    }

    private static class Board {
        private final Field[][] fields;

        public Board(final Coords max) {
            fields = IntStream.rangeClosed(0, max.getRow())
                    .mapToObj(i -> initRow(max.getColumn()))
                    .toArray(Field[][]::new);
        }

        private Field[] initRow(final int columns) {
            return IntStream.rangeClosed(0, columns).mapToObj(i -> new Field()).toArray(Field[]::new);
        }

        public void markLines(final Vent vent) {
            mark(vent, false);
        }

        public void markAll(final Vent vent) {
            mark(vent, true);
        }

        private void mark(final Vent vent, final boolean diagonal) {
            if (vent.isHorizontal()) {
                final Field[] row = fields[vent.getMinRow()];
                for (int i = vent.getMinColumn(); i <= vent.getMaxColumn(); i++) {
                    row[i].mark();
                }
            } else if (vent.isVertical()) {
                final int column = vent.getMinColumn();
                for (int i = vent.getMinRow(); i <= vent.getMaxRow(); i++) {
                    fields[i][column].mark();
                }
            } else if (diagonal && vent.isDiagonal()) {
                final Coords startRow = vent.getDiagonalMinRow();
                final Coords finishRow = vent.getDiagonalMaxRow();
                final boolean isColumnIncreasing = startRow.getColumn() < finishRow.getColumn();
                if (isColumnIncreasing) {
                    for (int row = startRow.getRow(), col = startRow.getColumn();
                         row <= finishRow.getRow(); row++, col++) {
                        fields[row][col].mark();
                    }
                } else {
                    for (int row = startRow.getRow(), col = startRow.getColumn();
                         row <= finishRow.getRow(); row++, col--) {
                        fields[row][col].mark();
                    }
                }
            }
        }

        public long countOverlaps(final int value) {
            return Arrays.stream(fields).flatMap(Stream::of).filter(field -> field.getValue() >= value).count();
        }
    }

    private static class Vent {
        private final Coords a;
        private final Coords b;

        public Vent(final String coords) {
            final String[] split = coords.replaceAll(" ", "").split("->");
            a = new Coords(split[0]);
            b = new Coords(split[1]);
        }

        public boolean isDiagonal() {
            return !(isHorizontal() || isVertical());
        }

        public boolean isHorizontal() {
            return a.getRow() == b.getRow();
        }

        public boolean isVertical() {
            return a.getColumn() == b.getColumn();
        }

        public int getMaxColumn() {
            return Math.max(a.getColumn(), b.getColumn());
        }

        public int getMaxRow() {
            return Math.max(a.getRow(), b.getRow());
        }

        public int getMinColumn() {
            return Math.min(a.getColumn(), b.getColumn());
        }

        public int getMinRow() {
            return Math.min(a.getRow(), b.getRow());
        }

        public Coords getDiagonalMinRow() {
            return a.getRow() < b.getRow() ? a : b;
        }

        public Coords getDiagonalMaxRow() {
            return a.getRow() > b.getRow() ? a : b;
        }

    }

    private static class Coords {
        private final int x; //column
        private final int y; //row

        public Coords(final String coordStr) {
            final String[] split = coordStr.split(",");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
        }

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getColumn() {
            return x;
        }

        public int getRow() {
            return y;
        }
    }

    private static class Field {
        int value;

        public int getValue() {
            return value;
        }

        public void mark() {
            value++;
        }
    }

}
