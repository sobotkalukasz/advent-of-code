package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * https://adventofcode.com/2021/day/25
 * */
public class SeaCucumber {

    final Map<Coordinate, Type> seabed;
    final int rowCount;
    final int columnCount;

    public SeaCucumber(List<String> input) {
        seabed = new HashMap<>();
        rowCount = input.size() - 1;
        columnCount = input.get(0).length() - 1;

        for (int row = 0; row < input.size(); row++) {
            final char[] chars = input.get(row).toCharArray();
            for (int col = 0; col < chars.length; col++) {
                final Type type = Type.getByValue(chars[col]);
                if (!type.isEmpty()) {
                    seabed.put(new Coordinate(row, col), type);
                }
            }
        }
    }

    public int countMoves() {
        Map<Coordinate, Type> seabed = this.seabed;
        boolean changed = true;
        int count = 0;

        while (changed) {
            MoveResult right = applyMove(seabed, e -> e.getValue().isRight(), c -> c.getNextRight(columnCount));
            MoveResult down = applyMove(right.seabed, e -> e.getValue().isDown(), c -> c.getNextDown(rowCount));
            seabed = down.seabed;
            changed = right.changed() || down.changed();
            count++;
        }

        return count;
    }

    public MoveResult applyMove(final Map<Coordinate, Type> seabed, final Predicate<Map.Entry<Coordinate, Type>> filter,
            final Function<Coordinate, Coordinate> nextCoordinate) {
        final Map<Coordinate, Type> move = new HashMap<>();

        seabed.entrySet().forEach(e -> {
            if (filter.test(e)) {
                final Coordinate next = nextCoordinate.apply(e.getKey());
                if (!seabed.containsKey(next)) {
                    move.put(next, e.getValue());
                } else {
                    move.put(e.getKey(), e.getValue());
                }
            } else {
                move.put(e.getKey(), e.getValue());
            }
        });

        final boolean isChanged = !seabed.equals(move);
        return new MoveResult(move, isChanged);
    }

    enum Type {
        RIGHT('>'), DOWN('v'), EMPTY('.');

        Type(char value) {
            this.value = value;
        }

        final char value;

        public boolean isEmpty() {
            return this.equals(EMPTY);
        }

        public boolean isRight() {
            return this.equals(RIGHT);
        }

        public boolean isDown() {
            return this.equals(DOWN);
        }

        public static Type getByValue(final char value) {
            return switch (value) {
                case '>' -> RIGHT;
                case 'v' -> DOWN;
                default -> EMPTY;
            };
        }
    }

    record MoveResult(Map<Coordinate, Type> seabed, boolean changed) {
    }
}
