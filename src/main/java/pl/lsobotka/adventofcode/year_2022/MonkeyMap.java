package pl.lsobotka.adventofcode.year_2022;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/*
 * https://adventofcode.com/2022/day/22
 * */
public class MonkeyMap {

    final Board board;
    final List<Operation> operations;

    MonkeyMap(final List<String> input) {
        this.board = Board.from(input);
        this.operations = initOperations(input.get(input.size() - 1));
    }

    public long determinePasswordOnUnfoldedMap() {
        return board.applyOnUnfoldedMap(operations).decodePassword();
    }

    public long determinePasswordOnFoldedMap() {
        return board.applyOnFoldedMap(operations).decodePassword();
    }

    record Board(Map<Coord, Tile> map, int maxRow, int maxCol) {

        Player applyOnUnfoldedMap(final List<Operation> operations) {
            return apply(operations, UnfoldedMapEdgeStrategy());
        }

        Player applyOnFoldedMap(final List<Operation> operations) {
            return apply(operations, foldedMapEdgeStrategy(Mappings.of(this)));
        }

        private Player apply(final List<Operation> operations, final Function<Player, Player> edgeStrategy) {
            Player player = initPlayer();

            for (Operation op : operations) {
                if (op instanceof Rotate r) {
                    player = new Player(player.coord, player.dir.change(r));
                } else if (op instanceof Move m) {
                    player = applyMove(player, m, edgeStrategy);
                }
            }

            return player;
        }

        Player applyMove(final Player player, final Move m, final Function<Player, Player> edgeStrategy) {
            Player tmp = player;
            int left = m.value;

            while (left != 0) {
                Player newPlayer = new Player(tmp.coord.apply(tmp.dir), tmp.dir);
                if (!map.containsKey(newPlayer.coord)) {
                    newPlayer = edgeStrategy.apply(tmp);
                }
                if (map.containsKey(newPlayer.coord)) {
                    if (map.get(newPlayer.coord) == Tile.EMPTY) {
                        tmp = newPlayer;
                    } else if (map.get(newPlayer.coord) == Tile.WALL) {
                        break;
                    }
                } else {
                    throw new IllegalStateException("Something went wrong :(");
                }
                left--;
            }
            return tmp;
        }

        private Function<Player, Player> UnfoldedMapEdgeStrategy() {
            return (p) -> {
                Coord coord = switch (p.dir) {
                    case LEFT -> new Coord(p.coord.row(), maxColOf(p.coord.row));
                    case UP -> new Coord(maxRowOf(p.coord.col), p.coord.col);
                    case RIGHT -> new Coord(p.coord.row(), minColOf(p.coord.row));
                    case DOWN -> new Coord(minRowOf(p.coord.col), p.coord.col);
                };
                return new Player(coord, p.dir);
            };

        }

        private int maxColOf(final int row) {
            return map.keySet().stream().filter(c -> c.row == row).mapToInt(Coord::col).max().orElse(0);
        }

        private int minColOf(final int row) {
            return map.keySet().stream().filter(c -> c.row == row).mapToInt(Coord::col).min().orElse(0);
        }

        private int maxRowOf(final int col) {
            return map.keySet().stream().filter(c -> c.col == col).mapToInt(Coord::row).max().orElse(0);
        }

        private int minRowOf(final int col) {
            return map.keySet().stream().filter(c -> c.col == col).mapToInt(Coord::row).min().orElse(0);
        }

        private Function<Player, Player> foldedMapEdgeStrategy(final Mappings mappings) {
            return p -> {
                final Coord coord = mappings.coordAfterSwitch(p.coord);
                final Direction dir = mappings.directionAfterSwitch(p.coord);
                return new Player(coord, dir);
            };
        }

        private Player initPlayer() {
            for (int col = 1; col < maxCol; col++) {
                final Coord coord = new Coord(1, col);
                if (map.containsKey(coord) && map.get(coord) == Tile.EMPTY) {
                    return new Player(coord, Direction.RIGHT);
                }
            }
            throw new IllegalStateException("Can't init player");
        }

        static Board from(List<String> input) {
            final Map<Coord, Tile> board = new HashMap<>();
            for (int row = 0; row < input.size(); row++) {
                final String rowString = input.get(row);
                if (rowString.isEmpty()) {
                    break;
                }
                for (int col = 0; col < rowString.length(); col++) {
                    switch (rowString.charAt(col)) {
                    case '.' -> board.put(new Coord(row + 1, col + 1), Tile.EMPTY);
                    case '#' -> board.put(new Coord(row + 1, col + 1), Tile.WALL);
                    }
                }
            }

            final int maxRow = board.keySet().stream().mapToInt(Coord::row).max().orElse(0);
            final int maxCol = board.keySet().stream().mapToInt(Coord::col).max().orElse(0);

            return new Board(board, maxRow, maxCol);
        }
    }

    record Player(Coord coord, Direction dir) {
        int decodePassword() {
            final int row = coord.row * 1000;
            final int col = coord.col * 4;
            final int points = dir.points;

            return row + col + points;
        }

    }

    record Coord(int row, int col) {

        Coord apply(final Direction direction) {
            return switch (direction) {
                case LEFT -> new Coord(this.row, this.col - 1);
                case UP -> new Coord(this.row - 1, this.col);
                case RIGHT -> new Coord(this.row, this.col + 1);
                case DOWN -> new Coord(this.row + 1, this.col);
            };
        }

    }

    enum Tile {
        EMPTY, WALL
    }

    enum Direction {
        LEFT(2), UP(3), RIGHT(0), DOWN(1);

        final int points;

        Direction(int points) {
            this.points = points;
        }

        Direction change(final Rotate rotate) {
            return switch (this) {
                case LEFT -> rotate == Rotate.LEFT ? DOWN : UP;
                case UP -> rotate == Rotate.LEFT ? LEFT : RIGHT;
                case RIGHT -> rotate == Rotate.LEFT ? UP : DOWN;
                case DOWN -> rotate == Rotate.LEFT ? RIGHT : LEFT;
            };
        }
    }

    interface Operation {

    }

    record Move(int value) implements Operation {

    }

    enum Rotate implements Operation {
        LEFT, RIGHT

    }

    private List<Operation> initOperations(String operationRow) {
        final List<Operation> operations = new ArrayList<>();

        for (int i = 0; i < operationRow.length(); i++) {
            if (Character.isAlphabetic(operationRow.charAt(i))) {
                switch (operationRow.charAt(i)) {
                case 'L' -> operations.add(Rotate.LEFT);
                case 'R' -> operations.add(Rotate.RIGHT);
                }
            } else {
                if (i == operationRow.length() - 1) {
                    operations.add(new Move(Integer.parseInt(operationRow.substring(i, i + 1))));
                }
                for (int j = i; j < operationRow.length(); j++) {
                    if (Character.isAlphabetic(operationRow.charAt(j))) {
                        operations.add(new Move(Integer.parseInt(operationRow.substring(i, j))));
                        i = j - 1;
                        break;
                    }
                }
            }
        }
        return operations;
    }

    record Mappings(List<EdgeMapping> mappings) {

        static Mappings of(final Board board) {
            if (board.maxRow < 20) {
                return hardcodedExampleMapping();
            } else {
                return hardcodedInputMapping();
            }
        }

        static Mappings hardcodedExampleMapping() {
            final List<EdgeMapping> mappings = new ArrayList<>();

            final Edge oneTwo = new Edge(1, 1, 9, 12);
            final Edge twoOne = new Edge(5, 5, 1, 4);
            final Direction fromOneTwo = Direction.DOWN;
            final Direction fromTwoOne = Direction.DOWN;
            final MappingType oneTwoMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(oneTwo, twoOne, oneTwoMapping, fromOneTwo, fromTwoOne));

            final Edge oneThree = new Edge(1, 4, 9, 9);
            final Edge threeOne = new Edge(5, 5, 5, 8);
            final Direction fromOneThree = Direction.DOWN;
            final Direction fromThreeOne = Direction.LEFT;
            final MappingType oneThreeMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(oneThree, threeOne, oneThreeMapping, fromOneThree, fromThreeOne));

            final Edge oneSix = new Edge(1, 4, 12, 12);
            final Edge sixOne = new Edge(9, 9, 13, 16);
            final Direction fromOneSix = Direction.LEFT;
            final Direction fromSixOne = Direction.LEFT;
            final MappingType oneSixMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(oneSix, sixOne, oneSixMapping, fromOneSix, fromSixOne));

            final Edge twoSix = new Edge(5, 8, 1, 1);
            final Edge sixTwo = new Edge(12, 12, 13, 16);
            final Direction fromTwoSix = Direction.UP;
            final Direction fromSixTwo = Direction.RIGHT;
            final MappingType twoSixMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(twoSix, sixTwo, twoSixMapping, fromTwoSix, fromSixTwo));

            final Edge twoFive = new Edge(8, 8, 1, 4);
            final Edge fiveTwo = new Edge(12, 12, 9, 12);
            final Direction fromTwoFive = Direction.UP;
            final Direction fromFiveTwo = Direction.UP;
            final MappingType twoFiveMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(twoFive, fiveTwo, twoFiveMapping, fromTwoFive, fromFiveTwo));

            final Edge threeFive = new Edge(8, 8, 5, 8);
            final Edge fiveThree = new Edge(9, 12, 8, 8);
            final Direction fromThreeFive = Direction.RIGHT;
            final Direction fromFiveThree = Direction.UP;
            final MappingType threeFiveMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(threeFive, fiveThree, threeFiveMapping, fromThreeFive, fromFiveThree));

            final Edge fourSix = new Edge(5, 8, 12, 12);
            final Edge sixFour = new Edge(9, 9, 13, 16);
            final Direction fromFourSix = Direction.DOWN;
            final Direction fromSixFour = Direction.LEFT;
            final MappingType fourSixMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(fourSix, sixFour, fourSixMapping, fromFourSix, fromSixFour));

            return new Mappings(mappings);
        }

        static Mappings hardcodedInputMapping() {
            final List<EdgeMapping> mappings = new ArrayList<>();

            final Edge oneSix = new Edge(1, 1, 51, 100);
            final Edge sixOne = new Edge(151, 200, 1, 1);
            final Direction fromOneSix = Direction.RIGHT;
            final Direction fromSixOne = Direction.DOWN;
            final MappingType oneSixMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(oneSix, sixOne, oneSixMapping, fromOneSix, fromSixOne));

            final Edge oneFive = new Edge(1, 50, 51, 51);
            final Edge fiveOne = new Edge(101, 150, 1, 1);
            final Direction fromOneFive = Direction.RIGHT;
            final Direction fromFiveOne = Direction.RIGHT;
            final MappingType oneFiveMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(oneFive, fiveOne, oneFiveMapping, fromOneFive, fromFiveOne));

            final Edge twoSix = new Edge(1, 1, 101, 150);
            final Edge sixTwo = new Edge(200, 200, 1, 50);
            final Direction fromTwoSix = Direction.UP;
            final Direction fromSixTwo = Direction.DOWN;
            final MappingType twoSixMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(twoSix, sixTwo, twoSixMapping, fromTwoSix, fromSixTwo));

            final Edge twoFour = new Edge(1, 50, 150, 150);
            final Edge fourTwo = new Edge(101, 150, 100, 100);
            final Direction fromTwoFour = Direction.LEFT;
            final Direction fromFourTwo = Direction.LEFT;
            final MappingType twoFourMapping = MappingType.DECREASE;
            mappings.add(new EdgeMapping(twoFour, fourTwo, twoFourMapping, fromTwoFour, fromFourTwo));

            final Edge twoThree = new Edge(50, 50, 101, 150);
            final Edge threeTwo = new Edge(51, 100, 100, 100);
            final Direction fromTwoThree = Direction.LEFT;
            final Direction fromThreeTwo = Direction.UP;
            final MappingType twoThreeMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(twoThree, threeTwo, twoThreeMapping, fromTwoThree, fromThreeTwo));

            final Edge threeFive = new Edge(51, 100, 51, 51);
            final Edge fiveThree = new Edge(101, 101, 1, 50);
            final Direction fromThreeFive = Direction.DOWN;
            final Direction fromFiveThree = Direction.RIGHT;
            final MappingType threeFiveMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(threeFive, fiveThree, threeFiveMapping, fromThreeFive, fromFiveThree));

            final Edge fourSix = new Edge(150, 150, 51, 100);
            final Edge sixFour = new Edge(151, 200, 50, 50);
            final Direction fromFourSix = Direction.LEFT;
            final Direction fromSixFour = Direction.UP;
            final MappingType fourSixMapping = MappingType.INCREASE;
            mappings.add(new EdgeMapping(fourSix, sixFour, fourSixMapping, fromFourSix, fromSixFour));

            return new Mappings(mappings);
        }

        Coord coordAfterSwitch(final Coord coord) {
            return mappings.stream()
                    .filter(m -> m.contains(coord))
                    .findFirst()
                    .map(m -> m.coordFrom(coord))
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find mapping"));
        }

        Direction directionAfterSwitch(final Coord coord) {
            return mappings.stream()
                    .filter(m -> m.contains(coord))
                    .findFirst()
                    .map(m -> m.dirFrom(coord))
                    .orElseThrow(() -> new IllegalArgumentException("Cannot find mapping"));
        }
    }

    record EdgeMapping(Edge a, Edge b, MappingType type, Direction fromA, Direction fromB) {

        boolean contains(final Coord coord) {
            return a.contains(coord) || b.contains(coord);
        }

        Direction dirFrom(final Coord coord) {
            return a.contains(coord) ? fromA : fromB;
        }

        Coord coordFrom(final Coord c) {
            final Coord result;
            if (a.contains(c)) {
                result = getCoord(c, a, b);
            } else {
                result = getCoord(c, b, a);
            }
            return result;
        }

        private Coord getCoord(final Coord c, final Edge from, final Edge to) {
            Coord result;
            final int diff = from.getDiff(c);
            if (type == MappingType.INCREASE) {
                result = to.increase(diff);
            } else {
                result = to.decrease(diff);
            }
            return result;
        }
    }

    enum MappingType {
        INCREASE, DECREASE
    }

    record Edge(int rowFrom, int rowTo, int colFrom, int colTo) {

        Coord increase(final int offset) {
            if (rowFrom == rowTo) {
                return new Coord(rowFrom, colFrom + offset);
            }
            return new Coord(rowFrom + offset, colFrom);
        }

        Coord decrease(final int offset) {
            if (rowFrom == rowTo) {
                return new Coord(rowTo, colTo - offset);
            }
            return new Coord(rowTo - offset, colTo);
        }

        int getDiff(final Coord c) {
            final int rowDiff = c.row - rowFrom;
            final int colDiff = c.col - colFrom;
            return Math.max(rowDiff, colDiff);
        }

        boolean contains(final Coord coord) {
            return rowFrom <= coord.row && coord.row <= rowTo && colFrom <= coord.col && coord.col <= colTo;
        }
    }
}
