package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/24
 * */
public class BlizzardBasin {

    final Set<Coord> wall;
    final Set<Wind> wind;
    final Coord start;
    final Coord end;
    final Coord bottomRight;

    BlizzardBasin(final List<String> input) {
        this.wall = new HashSet<>();
        this.wind = new HashSet<>();

        Coord start = null;
        Coord end = null;

        int windCounter = 0;
        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            for (int col = 0; col < rowString.length(); col++) {
                final char c = rowString.charAt(col);
                if (c == '#') {
                    wall.add(Coord.of(row, col));
                } else if (Wind.isWind(c)) {
                    wind.add(Wind.of(windCounter++, c, Coord.of(row, col)));
                } else if (c == '.') {
                    if (row == 0 && start == null) {
                        start = Coord.of(row, col);
                    }
                    if (row == input.size() - 1 && end == null) {
                        end = Coord.of(row, col);
                    }
                }
            }
        }

        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
        this.bottomRight = Coord.of(input.size() - 1, input.get(input.size() - 1).length() - 1);
    }

    int oneWay() {
        final Map<Integer, WindPerMove> windPerMove = new HashMap<>();
        windPerMove.put(0, WindPerMove.of(wind));

        final BasinPath path = lessMoves(windPerMove, start, end, 0);

        return path.moves;
    }

    int goOnceAgain() {
        final Map<Integer, WindPerMove> windPerMove = new HashMap<>();
        windPerMove.put(0, WindPerMove.of(wind));

        final BasinPath first = lessMoves(windPerMove, start, end, 0);
        final BasinPath goBack = lessMoves(windPerMove, end, start, first.moves);
        final BasinPath onceAgain = lessMoves(windPerMove, start, end, goBack.moves);

        return onceAgain.moves;
    }

    private BasinPath lessMoves(final Map<Integer, WindPerMove> windPerMove, final Coord start, final Coord end,
            final int moveCount) {

        final Queue<BasinPath> paths = new PriorityQueue<>();
        paths.add(new BasinPath(start, moveCount));

        final Set<BasinPath> visited = new HashSet<>();
        visited.add(new BasinPath(start, moveCount));

        while (!paths.isEmpty() && !paths.peek().coord.equals(end)) {
            final BasinPath current = paths.poll();
            final int nextMoveCount = current.moves + 1;

            final WindPerMove nextMoveWind;
            if (windPerMove.containsKey(nextMoveCount)) {
                nextMoveWind = windPerMove.get(nextMoveCount);
            } else {
                final Set<Wind> newWind = new HashSet<>();
                for (Wind wind : windPerMove.get(current.moves).wind) {
                    final Wind next = wind.next(wall, bottomRight);
                    newWind.add(next);
                }
                nextMoveWind = WindPerMove.of(newWind);
                windPerMove.put(nextMoveCount, nextMoveWind);
            }

            current.nextPossible(nextMoveWind.coords, wall, bottomRight).forEach(next -> {
                if (!visited.contains(next)) {
                    visited.add(next);
                    paths.add(next);
                }
            });
        }

        return paths.peek();
    }

    record Coord(int row, int col) {
        static Coord of(final int row, final int col) {
            return new Coord(row, col);
        }

        Coord move(final Dir dir) {
            return switch (dir) {
                case UP -> Coord.of(this.row - 1, this.col);
                case DOWN -> Coord.of(this.row + 1, this.col);
                case LEFT -> Coord.of(this.row, this.col - 1);
                case RIGHT -> Coord.of(this.row, this.col + 1);
            };
        }

        Set<Coord> getAdjacentWithCurrent() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(row - 1, col));
            adjacent.add(Coord.of(row + 1, col));
            adjacent.add(Coord.of(row, col - 1));
            adjacent.add(Coord.of(row, col + 1));
            adjacent.add(this);
            return adjacent;
        }
    }

    record Wind(int id, Dir dir, Coord coord) {

        static boolean isWind(final char c) {
            return Dir.symbols.contains(c);
        }

        static Wind of(final int id, final char c, final Coord coord) {
            return new Wind(id, Dir.of(c), coord);
        }

        Wind next(final Set<Coord> walls, final Coord bottomRight) {
            Coord maybe = this.coord.move(this.dir);
            if (walls.contains(maybe)) {
                maybe = switch (this.dir) {
                    case UP -> Coord.of(bottomRight.row - 1, maybe.col);
                    case DOWN -> Coord.of(1, maybe.col);
                    case LEFT -> Coord.of(maybe.row, bottomRight.col - 1);
                    case RIGHT -> Coord.of(maybe.row, 1);
                };
            }
            return new Wind(this.id, this.dir, maybe);
        }

    }

    enum Dir {
        UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');
        private static final Set<Character> symbols = Set.of('^', 'v', '<', '>');
        final char symbol;

        Dir(char symbol) {
            this.symbol = symbol;
        }

        static Dir of(final char c) {
            return Arrays.stream(Dir.values())
                    .filter(w -> w.symbol == c)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Unknown Dir symbol: " + c));
        }

    }

    record BasinPath(Coord coord, int moves) implements Comparable<BasinPath> {

        @Override
        public int compareTo(BasinPath o) {
            return Comparator.comparing(BasinPath::moves).compare(this, o);
        }

        Set<BasinPath> nextPossible(final Set<Coord> wind, final Set<Coord> wall, Coord bottomRight) {
            return coord.getAdjacentWithCurrent()
                    .stream()
                    .filter(c -> c.row > -1 && c.row < bottomRight.row + 1)
                    .filter(c -> !wind.contains(c) && !wall.contains(c))
                    .map(c -> new BasinPath(c, moves + 1))
                    .collect(Collectors.toSet());
        }
    }

    record WindPerMove(Set<Wind> wind, Set<Coord> coords) {

        static WindPerMove of(final Set<Wind> wind) {
            final Set<Coord> coords = wind.stream().map(Wind::coord).collect(Collectors.toSet());
            return new WindPerMove(wind, coords);
        }

    }

}
