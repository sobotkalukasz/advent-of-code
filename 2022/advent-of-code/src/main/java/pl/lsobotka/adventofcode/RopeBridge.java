package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RopeBridge {

    final List<Operation> operations;

    RopeBridge(final List<String> input) {
        operations = input.stream().map(Operation::from).flatMap(Collection::stream).collect(Collectors.toList());
    }

    int countUniqueTailPositions() {
        final Set<Coord> visitedByTail = new HashSet<>();
        Coord head = Coord.of(0, 0);
        Coord tail = Coord.of(0, 0);

        for (Operation op : operations) {
            head = op.move.apply(head);
            tail = tail.moveAdjacentTo(head);
            visitedByTail.add(tail);
        }

        return visitedByTail.size();
    }

    record Coord(int row, int column) {

        static Coord of(final int row, final int column) {
            return new Coord(row, column);
        }

        boolean isAdjacent(final Coord other) {
            final int rowLen = Math.abs(this.row - other.row);
            final int colLen = Math.abs(this.column - other.column);

            //System.out.println(this + " is adjacent to: " + other + ". " + (rowLen <= 1 && colLen <= 1));

            return rowLen <= 1 && colLen <= 1;
        }

        Coord moveAdjacentTo(final Coord other) {

            Coord newPos = this;
            if (!this.isAdjacent(other)) {
                newPos = adjustRow(newPos, other);
                newPos = adjustColumn(newPos, other);
            }
            //System.out.println(String.format("This:   %s\nother:  %s\nnewPos: %s\n\n", this, other, newPos));
            return newPos;
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
            //System.out.println("DOWN");
            return Coord.of(row + 1, column);
        }

        private Coord up() {
            //System.out.println("UP");
            return Coord.of(row - 1, column);
        }

        private Coord right() {
            //System.out.println("RIGHT");
            return Coord.of(row, column + 1);
        }

        private Coord left() {
            //System.out.println("LEFT");
            return Coord.of(row, column - 1);
        }
    }

    enum Operation {
        UP("U", Coord::up), DOWN("D", Coord::down), LEFT("L", Coord::left), RIGHT("R", Coord::right);

        final String value;
        final Function<Coord, Coord> move;

        Operation(String value, Function<Coord, Coord> move) {
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
