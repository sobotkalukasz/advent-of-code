package pl.lsobotka.adventofcode.amphipod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Board {
    private static final int corridorIndex = 0;
    private static final int corridorLength = 11;
    private static final int aRow = 8;
    private static final int bRow = 6;
    private static final int cRow = 4;
    private static final int dRow = 2;

    private final int howManyOfEachType;
    final char[][] board;

    public Board(final List<String> input) {
        howManyOfEachType = input.size() - 3;
        board = initEmptyBoard();

        for (int i = 2, p = 1; p <= howManyOfEachType; i++, p++) {
            final char[] firstRow = input.get(i).replace(" ", "").replace("#", "").toCharArray();
            board[aRow][p] = firstRow[0];
            board[bRow][p] = firstRow[1];
            board[cRow][p] = firstRow[2];
            board[dRow][p] = firstRow[3];
        }
    }

    private Board(int howManyOfEachType, char[][] board) {
        this.howManyOfEachType = howManyOfEachType;
        this.board = board;
    }

    private char[][] initEmptyBoard() {
        final char[][] board = new char[corridorLength][];
        for (int i = 0; i < corridorLength; i++) {
            if (i == aRow || i == bRow || i == cRow || i == dRow) {
                board[i] = new char[1 + howManyOfEachType];
            } else {
                board[i] = new char[1];
            }
        }
        return board;
    }

    private int determineCost(final char c, final int moves) {
        return switch (c) {
            case 'A' -> 1 * moves;
            case 'B' -> 10 * moves;
            case 'C' -> 100 * moves;
            case 'D' -> 1000 * moves;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    public void applyMove(final Move move) {
        final Pos from = move.from();
        final Pos to = move.to();

        final char value = board[from.row][from.column];
        board[to.row][to.column] = value;
        board[from.row][from.column] = '\u0000';
    }

    public boolean isCompleted() {
        boolean completed = true;
        for (int i = 0; i < board.length; i++) {
            if (isRoomRow(i) && completed) {
                for (int j = 1; j < board[i].length; j++) {
                    final char c = board[i][j];
                    if (i == aRow && completed) {
                        completed = c == 'A';
                    } else if (i == bRow && completed) {
                        completed = c == 'B';
                    } else if (i == cRow && completed) {
                        completed = c == 'C';
                    } else if (i == dRow && completed) {
                        completed = c == 'D';
                    }
                }
            }
            if (!completed) {
                break;
            }
        }
        return completed;
    }

    public Board copy() {
        final char[][] copy = new char[corridorLength][];
        for (int i = 0; i < corridorLength; i++) {
            copy[i] = Arrays.copyOf(board[i], board[i].length);
        }

        return new Board(howManyOfEachType, copy);
    }

    public String print() {
        final StringBuilder builder = new StringBuilder();
        for (char[] chars : board) {
            builder.append(chars);
        }
        return builder.toString();
    }

    public List<Move> findAllValidMoves() {
        final List<Move> validMoves = new ArrayList<>();

        for (int row = 0; row < corridorLength; row++) {
            for (int col = corridorIndex; col < board[row].length; col++) {
                final char c = board[row][col];
                if (!isEmpty(row, col)) {
                    final Pos actual = Pos.init(row, col);
                    final int roomRow = getRoomIndex(c);
                    if (col == corridorIndex) { //Can go back only to his room
                        boolean canGoBack = true;
                        Pos target = null;
                        for (int i = board[roomRow].length - 1; i > 0 && canGoBack; i--) {
                            if (isEmpty(roomRow, i)) {
                                if (Objects.isNull(target)) {
                                    target = Pos.init(roomRow, i);
                                }
                            } else {
                                final char other = board[roomRow][i];
                                if (Objects.nonNull(target)) { //can't go back if someone is blocking
                                    canGoBack = false;
                                } else {
                                    canGoBack = c == other; //can't go back if other type in the room
                                }
                            }
                        }
                        if (canGoBack && Objects.nonNull(target)) {
                            final int moves = calculateMoves(actual, target);
                            final int cost = determineCost(c, moves);
                            if (cost > 0) {
                                validMoves.add(Move.init(actual, target, cost));
                            }
                        }
                    } else if (isRoomRow(row)) {
                        boolean calculate = true;
                        if (roomRow == row) { //already in his room
                            boolean noBlockingOther = true;
                            for (int roomCol = col; roomCol < board[row].length && noBlockingOther; roomCol++) {
                                noBlockingOther = c == board[row][roomCol];
                            }
                            calculate = !noBlockingOther;
                        }
                        if (calculate) {
                            for (int corridor = 0; corridor < corridorLength; corridor++) {
                                // move to empty corridor space without room entrance
                                if (!isRoomRow(corridor) && isEmpty(corridor, corridorIndex)) {
                                    final Pos target = Pos.init(corridor, corridorIndex);
                                    final int moves = calculateMoves(actual, target);
                                    final int cost = determineCost(c, moves);
                                    if (cost > 0) {
                                        validMoves.add(Move.init(actual, target, cost));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return validMoves;
    }

    public boolean isRoomRow(final int row) {
        return row == aRow || row == bRow || row == cRow || row == dRow;
    }

    private boolean isEmpty(final int row, final int col) {
        final char c = board[row][col];
        return !(c == 'A' || c == 'B' || c == 'C' || c == 'D');
    }

    public int calculateMoves(final Pos actual, final Pos target) {

        final Pos corridor;
        final Pos room;
        if (actual.column == corridorIndex) {
            corridor = actual;
            room = target;
        } else {
            corridor = target;
            room = actual;
        }
        final int corridorFrom = Math.min(corridor.row, room.row);
        final int corridorTo = Math.max(corridor.row, room.row);

        boolean isBlocked = false;
        int countMoves = 0;
        for (int row = corridorFrom + 1; row <= corridorTo && !isBlocked; row++) {
            if (isEmpty(row, corridorIndex) || row == corridorTo) {
                countMoves++;
            } else {
                isBlocked = true;
            }
        }

        for (int col = corridorIndex; col < room.column && !isBlocked; col++) {
            if (isEmpty(room.row, col)) {
                countMoves++;
            } else {
                isBlocked = true;
            }
        }

        return isBlocked ? 0 : countMoves;
    }

    public int getRoomIndex(final char c) {
        return switch (c) {
            case 'A' -> aRow;
            case 'B' -> bRow;
            case 'C' -> cRow;
            case 'D' -> dRow;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    public record Move(Pos from, Pos to, int cost) {
        public static Move init(final Pos from, final Pos to, final int cost) {
            return new Move(from, to, cost);
        }
    }

    public record Pos(int row, int column) {
        public static Pos init(final int row, final int column) {
            return new Pos(row, column);
        }
    }

}
