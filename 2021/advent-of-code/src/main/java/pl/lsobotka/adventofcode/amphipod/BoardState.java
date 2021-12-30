package pl.lsobotka.adventofcode.amphipod;

public record BoardState(Board board, long score) implements Comparable<BoardState> {

    @Override
    public int compareTo(BoardState other) {
        return Long.compare(this.score, other.score);
    }
}
