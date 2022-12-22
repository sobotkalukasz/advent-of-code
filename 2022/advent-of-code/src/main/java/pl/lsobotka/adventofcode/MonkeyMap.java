package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonkeyMap {

    final Board board;
    List<Operation> operations;

    MonkeyMap(final List<String> input) {
        this.board = Board.from(input);
        this.operations = initOperations(input.get(input.size() - 1));
    }

    public long determinePassword() {
        final Player player = board.apply(operations);

        final int row = (player.coord.row + 1) * 1000;
        final int col = (player.coord.col + 1) * 4;
        final int points = player.dir.points;

        return row + col + points;
    }

    record Board(Map<Coord, Tile> map, int maxRow, int maxCol) {

        Player apply(final List<Operation> operations) {
            Player player = initPlayer();

            for (Operation op : operations) {
                if (op instanceof Rotate r) {
                    player = new Player(player.coord, player.dir.change(r));
                } else if (op instanceof Move m) {
                    final Coord coord = newCoord(player.coord, player.dir, m);
                    player = new Player(coord, player.dir);
                }
            }

            return player;
        }

        Coord newCoord(final Coord from, final Direction dir, final Move m) {
            Coord tmp = from;
            int left = m.value;

            while (left != 0) {
                Coord newCoord = tmp.apply(dir);
                if (!map.containsKey(newCoord)) {
                    newCoord = switch (dir) {
                        case LEFT -> new Coord(tmp.row(), maxColOf(tmp.row));
                        case UP -> new Coord(maxRowOf(tmp.col), tmp.col);
                        case RIGHT -> new Coord(tmp.row(), minColOf(tmp.row));
                        case DOWN -> new Coord(minRowOf(tmp.col), tmp.col);
                    };
                }
                if (map.containsKey(newCoord)) {
                    if (map.get(newCoord) == Tile.EMPTY) {
                        tmp = newCoord;
                    } else if (map.get(newCoord) == Tile.WALL) {
                        break;
                    }
                } else {
                    throw new IllegalStateException("Something is wrong :(");
                }
                left--;
            }
            return tmp;
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

        private Player initPlayer() {
            for (int col = 0; col < maxCol; col++) {
                final Coord coord = new Coord(0, col);
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
                    case '.' -> board.put(new Coord(row, col), Tile.EMPTY);
                    case '#' -> board.put(new Coord(row, col), Tile.WALL);
                    }
                }
            }

            final int maxRow = board.keySet().stream().mapToInt(Coord::row).max().orElse(0);
            final int maxCol = board.keySet().stream().mapToInt(Coord::col).max().orElse(0);

            return new Board(board, maxRow, maxCol);
        }
    }

    record Player(Coord coord, Direction dir) {

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
}
