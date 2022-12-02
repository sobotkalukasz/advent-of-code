package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RockPaperScissors {

    private final List<Round> rounds;

    RockPaperScissors(final List<String> input) {
        rounds = input.stream().map(row -> {
            final String[] split = row.split("\\s+");
            final Move elfMove = Move.of(split[0]);
            final Move yourMove = Move.of(split[1]);
            return new Round(elfMove, yourMove);
        }).collect(Collectors.toList());
    }

    long yourPoints() {
        return rounds.stream().mapToLong(Round::getYourPoints).sum();
    }

}

class Round {
    private final Move elf;
    private final Move you;

    private final int yourPoints;

    public Round(Move elf, Move you) {
        this.elf = elf;
        this.you = you;
        this.yourPoints = you.round(elf);
    }

    public int getYourPoints() {
        return yourPoints;
    }
}

enum Move {
    ROCK("A", "X", 1), PAPER("B", "Y", 2), SCISSORS("C", "Z", 3);

    private final String valA;
    private final String valB;
    private final int points;

    Move(String valA, String valB, int points) {
        this.valA = valA;
        this.valB = valB;
        this.points = points;
    }

    static Move of(final String rawVal) {
        return Arrays.stream(Move.values())
                .filter(m -> rawVal.equalsIgnoreCase(m.valA) || rawVal.equalsIgnoreCase(m.valB))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown move: " + rawVal));
    }

    int round(final Move other) {
        final Outcome outcome = roundOutcome(other);
        return outcome.points + this.points;
    }

    private Outcome roundOutcome(final Move other) {
        final Outcome outcome;
        if (this.equals(other)) {
            outcome = Outcome.DRAW;
        } else {
            outcome = switch (this) {
                case ROCK -> other.equals(SCISSORS) ? Outcome.WIN : Outcome.LOSE;
                case PAPER -> other.equals(ROCK) ? Outcome.WIN : Outcome.LOSE;
                case SCISSORS -> other.equals(PAPER) ? Outcome.WIN : Outcome.LOSE;
            };
        }
        return outcome;
    }
}

enum Outcome {
    WIN(6), LOSE(0), DRAW(3);

    final int points;

    Outcome(int points) {
        this.points = points;
    }
}
