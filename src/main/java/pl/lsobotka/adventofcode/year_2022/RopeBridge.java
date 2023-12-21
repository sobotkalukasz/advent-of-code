package pl.lsobotka.adventofcode.year_2022;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/9
 * */
public class RopeBridge {

    final List<Operation> operations;

    RopeBridge(final List<String> input) {
        operations = input.stream().map(Operation::from).flatMap(Collection::stream).collect(Collectors.toList());
    }

    int countUniqueTailPositionsOf(final int ropeSize) {
        final Set<Coord> visitedByTail = new HashSet<>();
        final Knot rope = Knot.ropeOfSize(ropeSize);

        for (Operation op : operations) {
            rope.applyOperation(op);
            visitedByTail.add(rope.getTailPosition());
        }

        return visitedByTail.size();
    }

    private static class Knot {
        Coord coord;
        String name;
        Knot parent;
        Knot child;

        private Knot(final String name, final Knot parent) {
            this.coord = Coord.of(0, 0);
            this.name = name;
            this.parent = parent;
            if (Objects.nonNull(parent)) {
                parent.child = this;
            }
        }

        Coord getTailPosition() {
            if (isTail()) {
                return coord;
            } else {
                return child.getTailPosition();
            }
        }

        void applyOperation(final Operation op) {
            if (isHead()) {
                coord = op.move.apply(coord);
                adjust();
            }
        }

        private void adjust() {
            if (!isHead()) {
                coord = coord.moveAdjacentTo(parent.coord);
            }
            if (!isTail()) {
                child.adjust();
            }
        }

        private boolean isHead() {
            return Objects.isNull(parent);
        }

        private boolean isTail() {
            return Objects.isNull(child);
        }

        static Knot ropeOfSize(int size) {
            final Knot head = new Knot("H", null);

            Knot actual = head;
            for (int i = 1; i < size - 1; i++) {
                actual = new Knot(String.valueOf(i), actual);
            }
            new Knot("T", actual);

            return head;
        }
    }

    private record Coord(int row, int column) {

        static Coord of(final int row, final int column) {
            return new Coord(row, column);
        }

        Coord moveAdjacentTo(final Coord other) {

            Coord newPos = this;
            if (!this.isAdjacent(other)) {
                newPos = adjustRow(newPos, other);
                newPos = adjustColumn(newPos, other);
            }
            return newPos;
        }

        private boolean isAdjacent(final Coord other) {
            final int rowLen = Math.abs(this.row - other.row);
            final int colLen = Math.abs(this.column - other.column);
            return rowLen <= 1 && colLen <= 1;
        }

        private Coord adjustRow(final Coord coord, final Coord other) {
            final Coord newPos;
            if (coord.row == other.row) {
                newPos = coord;
            } else if (coord.row < other.row) {
                newPos = coord.down();
            } else {
                newPos = coord.up();
            }
            return newPos;
        }

        private Coord adjustColumn(final Coord coord, final Coord other) {
            final Coord newPos;
            if (coord.column == other.column) {
                newPos = coord;
            } else if (coord.column < other.column) {
                newPos = coord.right();
            } else {
                newPos = coord.left();
            }
            return newPos;
        }

        private Coord down() {
            return Coord.of(row + 1, column);
        }

        private Coord up() {
            return Coord.of(row - 1, column);
        }

        private Coord right() {
            return Coord.of(row, column + 1);
        }

        private Coord left() {
            return Coord.of(row, column - 1);
        }
    }

    enum Operation {
        UP("U", Coord::up), DOWN("D", Coord::down), LEFT("L", Coord::left), RIGHT("R", Coord::right);

        final String value;
        final Function<Coord, Coord> move;

        Operation(final String value, final Function<Coord, Coord> move) {
            this.value = value;
            this.move = move;
        }

        static List<Operation> from(final String raw) {
            final String[] split = raw.split(" ");
            final int times = Integer.parseInt(split[1]);
            final String operationCode = split[0];

            final Operation operation = Arrays.stream(Operation.values())
                    .filter(o -> o.value.equals(operationCode))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown operation type: " + operationCode));
            List<Operation> operations = new ArrayList<>();

            for (int i = 0; i < times; i++) {
                operations.add(operation);
            }

            return operations;
        }
    }
}
