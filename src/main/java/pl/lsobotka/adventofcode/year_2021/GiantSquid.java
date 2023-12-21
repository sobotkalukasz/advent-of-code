package pl.lsobotka.adventofcode.year_2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/4
 * */
public class GiantSquid {

    private List<Integer> numbers;
    private List<Board> boards;

    public GiantSquid(final List<String> inputRows) {
        final int processedRows = initNumbers(inputRows);
        initBoards(inputRows, processedRows);
        playBingo();
    }

    public int firstBoardWin() {
        final BinaryOperator<Board> lessRoundsWin = (a, b) -> a.getPlayedRounds() < b.getPlayedRounds() ? a : b;
        return getWiningResult(lessRoundsWin);
    }

    public int lastBoardWin() {
        final BinaryOperator<Board> moreRoundsWin = (a, b) -> a.getPlayedRounds() > b.getPlayedRounds() ? a : b;
        return getWiningResult(moreRoundsWin);
    }

    private void playBingo() {
        for (int value : numbers) {
            boards.forEach(board -> board.checkNumber(value));
        }
    }

    private int getWiningResult(final BinaryOperator<Board> reduce) {
        return boards.stream().filter(Board::isBingo).reduce(reduce).map(Board::calculateWiningResult).orElse(0);
    }

    private int initNumbers(final List<String> inputRows) {
        final List<String> tempRows = new ArrayList<>();
        int processedRow = 0;
        for (String row : inputRows) {
            processedRow++;
            if (row.isEmpty()) {
                break;
            }
            tempRows.add(row);
        }

        this.numbers = tempRows.stream()
                .reduce(String::concat)
                .map(str -> str.split(","))
                .map(Arrays::stream)
                .map(str -> str.map(Integer::parseInt).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        return processedRow;
    }

    private void initBoards(final List<String> inputRows, int processedRow) {
        final List<Board> boards = new ArrayList<>();
        List<String> tempBoard = new ArrayList<>();

        for (; processedRow <= inputRows.size(); processedRow++) {
            if (processedRow == inputRows.size() || inputRows.get(processedRow).isEmpty()) {
                boards.add(new Board(tempBoard));
                tempBoard.clear();
            } else {
                tempBoard.add(inputRows.get(processedRow));
            }
        }

        this.boards = boards;
    }

    private static class Board {

        private final BingoNumber[][] board;
        private boolean isBingo;
        private int playedRounds;
        private int winingValue;

        private Board(final List<String> tempBoard) {
            this.board = tempBoard.stream()
                    .map(row -> row.split(" "))
                    .map(row -> Arrays.stream(row)
                            .filter(str -> !str.equals(""))
                            .map(BingoNumber::new)
                            .toArray(BingoNumber[]::new))
                    .toArray(BingoNumber[][]::new);
        }

        public void checkNumber(final int value) {
            if (!isBingo) {
                playedRounds++;
                for (BingoNumber[] row : board) {
                    for (BingoNumber number : row) {
                        number.test(value);
                    }
                }
                isBingo = playedRounds >= board.length && checkBingo();
                if (isBingo) {
                    winingValue = value;
                }
            }
        }

        public boolean isBingo() {
            return isBingo;
        }

        public int getPlayedRounds() {
            return playedRounds;
        }

        public int calculateWiningResult() {
            final Integer sumOfUnmarked = Arrays.stream(board)
                    .flatMap(Arrays::stream)
                    .filter(number -> !number.isMarked())
                    .map(BingoNumber::getValue)
                    .reduce(Integer::sum)
                    .orElse(0);
            return sumOfUnmarked * winingValue;
        }

        private boolean checkBingo() {
            final int markNumbersToWin = board.length;
            boolean isBingo = checkBingoHorizontal(markNumbersToWin);
            if (!isBingo) {
                isBingo = checkBingoVertical(markNumbersToWin);
            }
            return isBingo;
        }

        private boolean checkBingoHorizontal(final int markNumbersToWin) {
            boolean isBingo = false;
            for (BingoNumber[] row : board) {
                if (isBingo) {
                    break;
                }
                int markedRowsHorizontal = 0;
                for (BingoNumber number : row) {
                    if (number.isMarked()) {
                        markedRowsHorizontal++;
                    }
                }
                isBingo = markedRowsHorizontal == markNumbersToWin;
            }
            return isBingo;
        }

        private boolean checkBingoVertical(final int markNumbersToWin) {
            boolean isBingo = false;
            for (int i = 0; i < markNumbersToWin; i++) {
                if (isBingo) {
                    break;
                }
                int markedRowsVertical = 0;
                for (BingoNumber[] bingoNumbers : board) {
                    if (bingoNumbers[i].isMarked()) {
                        markedRowsVertical++;
                    }
                }
                isBingo = markedRowsVertical == markNumbersToWin;
            }
            return isBingo;
        }

        private static class BingoNumber {

            private final int value;
            private boolean marked;

            private BingoNumber(final String value) {
                this.value = Integer.parseInt(value);
            }

            public void test(final int test) {
                marked = marked || value == test;
            }

            public boolean isMarked() {
                return marked;
            }

            public int getValue() {
                return value;
            }

        }
    }
}
