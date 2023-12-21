package pl.lsobotka.adventofcode.year_2021.amphipod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/*
 * https://adventofcode.com/2021/day/23
 * */
public class Amphipod {

    final Board board;

    public Amphipod(final List<String> input) {
        board = new Board(input);
    }

    public long findOptimalScore() {
        final PriorityQueue<BoardState> states = new PriorityQueue<>();
        states.add(new BoardState(board, 0));

        long lowestScore = Long.MAX_VALUE;
        final Map<String, Long> visited = new HashMap<>();

        while (!states.isEmpty()) {
            final BoardState actual = states.poll();
            if (actual.score() >= lowestScore) {
                break;
            }

            final Board board = actual.board();
            final List<Board.Move> validMoves = board.findAllValidMoves();
            for (Board.Move move : validMoves) {
                final Board copy = board.copy();
                copy.applyMove(move);
                final long score = actual.score() + move.cost();
                if (copy.isCompleted()) {
                    lowestScore = Math.min(lowestScore, score);
                } else {
                    final String print = copy.print();
                    if (score < visited.getOrDefault(print, Long.MAX_VALUE)) {
                        visited.put(print, score);
                        states.add(new BoardState(copy, score));
                    }
                }
            }
        }
        return lowestScore;
    }
}
