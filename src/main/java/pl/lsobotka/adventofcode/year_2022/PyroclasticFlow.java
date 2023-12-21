package pl.lsobotka.adventofcode.year_2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/17
 * */
public class PyroclasticFlow {

    final String[] jetPattern;

    PyroclasticFlow(final String raw) {
        jetPattern = raw.split("");
    }

    long rockFall(final long rounds) {

        final Set<Coord> rocks = new HashSet<>();
        final Type[] blockType = Type.values();
        long actualHeight = 0;
        long jetMoveCounter = 0;

        final Map<Set<Coord>, Cached> cache = new HashMap<>();
        long rowHeightToCache = 0;
        long cycleRepeatHeight = 0;

        for (long round = 0; round < rounds; round++) {
            final Type block = blockType[(int) (round % blockType.length)];
            Shape current = Shape.of(block, Coord.of(actualHeight + block.rows, 4));

            while (Objects.nonNull(current)) {
                final Shape moved = applyMove(rocks, jetMoveCounter, current);
                current = applyMoveDown(rocks, moved);

                jetMoveCounter++;
                if (Objects.isNull(current)) {
                    rocks.addAll(moved.coords);
                }
            }

            actualHeight = rocks.stream().mapToLong(Coord::row).max().orElse(0);

            if (cycleRepeatHeight == 0) {
                if (rowHeightToCache == 0) {
                    if (round == blockType.length * 4L - 1) {
                        rowHeightToCache = actualHeight;
                    }
                } else {
                    final long maxRow = actualHeight;
                    final long heightOfLastRows = rowHeightToCache;
                    final Set<Coord> key = rocks.stream()
                            .filter(c -> maxRow - c.row <= heightOfLastRows)
                            .map(c -> Coord.of(maxRow - c.row, c.col))
                            .collect(Collectors.toSet());

                    if (cache.containsKey(key)) {
                        final Cached cached = cache.get(key);
                        long length = round - cached.round;
                        long heightChange = actualHeight - cached.height;
                        long numberOfCycles = (rounds - round) / length;

                        cycleRepeatHeight = heightChange * numberOfCycles;
                        round += numberOfCycles * length;
                    } else {
                        cache.put(key, new Cached(round, actualHeight));
                    }
                }
            }
        }

        return actualHeight + cycleRepeatHeight;
    }

    private Shape applyMove(final Set<Coord> rocks, long jetMoveCounter, final Shape from) {
        final String move = jetPattern[(int) (jetMoveCounter % jetPattern.length)];

        final Shape temp;
        if (move.equals("<")) {
            temp = from.left();
        } else {
            temp = from.right();
        }

        final boolean notHitWall = temp.coords.stream().noneMatch(this::isWall);
        final boolean noOtherBlock = temp.coords.stream().noneMatch(rocks::contains);
        return notHitWall && noOtherBlock ? temp : from;
    }

    private Shape applyMoveDown(final Set<Coord> rocks, final Shape from) {
        final Shape down = from.down();
        final boolean notHitWall = down.coords.stream().noneMatch(this::isWall);
        final boolean noOtherBlock = down.coords.stream().noneMatch(rocks::contains);
        return notHitWall && noOtherBlock ? down : null;
    }

    private boolean isWall(final Coord coord) {
        int colLeft = 0;
        int colRight = 8;
        int bottomRow = 0;

        return coord.col == colLeft || coord.col == colRight || coord.row == bottomRow;
    }

    static class Shape {

        private final Type type;
        private final List<Coord> coords;

        public Shape(final Type type, final List<Coord> coords) {
            this.type = type;
            this.coords = coords;
        }

        Shape down() {
            return move(Coord::down);
        }

        Shape left() {
            return move(Coord::left);
        }

        Shape right() {
            return move(Coord::right);
        }

        static Shape of(final Type type, final Coord start) {
            List<Coord> coords = switch (type) {
                case LINE -> {
                    final List<Coord> temp = new ArrayList<>();
                    temp.add(start);
                    temp.add(start.left());
                    temp.add(start.right());
                    temp.add(start.right().right());
                    yield temp;
                }
                case PLUS -> {
                    final List<Coord> temp = new ArrayList<>();
                    temp.add(start);
                    temp.add(start.down());
                    temp.add(start.down().left());
                    temp.add(start.down().right());
                    temp.add(start.down().down());
                    yield temp;
                }
                case REVERSE_L -> {
                    final List<Coord> temp = new ArrayList<>();
                    temp.add(start.right());
                    temp.add(start.right().down());
                    temp.add(start.right().down().down());
                    temp.add(start.down().down());
                    temp.add(start.down().down().left());
                    yield temp;
                }
                case PIPE -> {
                    final List<Coord> temp = new ArrayList<>();
                    temp.add(start.left());
                    temp.add(start.left().down());
                    temp.add(start.left().down().down());
                    temp.add(start.left().down().down().down());
                    yield temp;
                }
                case CUBE -> {
                    final List<Coord> temp = new ArrayList<>();
                    temp.add(start);
                    temp.add(start.down());
                    temp.add(start.left());
                    temp.add(start.left().down());
                    yield temp;
                }
            };

            return new Shape(type, coords);
        }

        private Shape move(final Function<Coord, Coord> move) {
            final List<Coord> moved = coords.stream().map(move).collect(Collectors.toList());
            return new Shape(this.type, moved);
        }
    }

    record Coord(long row, int col) {

        static Coord of(final long row, final int col) {
            return new Coord(row, col);
        }

        Coord down() {
            return Coord.of(this.row - 1, this.col);
        }

        Coord left() {
            return Coord.of(this.row, this.col + 1);
        }

        Coord right() {
            return Coord.of(this.row, this.col - 1);
        }
    }

    enum Type {
        LINE(4), PLUS(6), REVERSE_L(6), PIPE(7), CUBE(5);

        final int rows;

        Type(final int rows) {
            this.rows = rows;
        }
    }

    record Cached(long round, long height) {

    }

}
