package pl.lsobotka.adventofcode.year_2022;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/2
 * */
public class RockPaperScissors {

    private final List<String> input;

    RockPaperScissors(final List<String> input) {
        this.input = Collections.unmodifiableList(input);
    }

    long pointsInRoundsByMove() {
        final List<Round> roundsByMove = getRoundsByMove(input);
        return roundsByMove.stream().mapToLong(Round::getYourPoints).sum();
    }

    private List<Round> getRoundsByMove(final List<String> input) {
        return input.stream().map(row -> {
            final String[] split = row.split("\\s+");
            final Move elfMove = Move.of(split[0]);
            final Move yourMove = Move.of(split[1]);
            return new Round(elfMove, yourMove);
        }).collect(Collectors.toList());
    }

    long pointsInRoundsByOutcome() {
        final List<Round> roundsByOutcome = getRoundsByOutcome(input);
        return roundsByOutcome.stream().mapToLong(Round::getYourPoints).sum();
    }

    private List<Round> getRoundsByOutcome(final List<String> input) {
        return input.stream().map(row -> {
            final String[] split = row.split("\\s+");
            final Move elfMove = Move.of(split[0]);
            final Outcome outcome = Outcome.of(split[1]);
            final Move yourMove = Move.from(elfMove, outcome);
            return new Round(elfMove, yourMove);
        }).collect(Collectors.toList());
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

        private final String codA;
        private final String codB;
        private final int points;

        Move(String codA, String codB, int points) {
            this.codA = codA;
            this.codB = codB;
            this.points = points;
        }

        static Move of(final String rawVal) {
            return Arrays.stream(Move.values())
                    .filter(m -> rawVal.equalsIgnoreCase(m.codA) || rawVal.equalsIgnoreCase(m.codB))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown move: " + rawVal));
        }

        static Move from(final Move other, final Outcome outcome) {
            final Move moveForOutcome;
            if (outcome.equals(Outcome.LOSE)) {
                moveForOutcome = switch (other) {
                    case ROCK -> SCISSORS;
                    case PAPER -> ROCK;
                    case SCISSORS -> PAPER;
                };
            } else if (outcome.equals(Outcome.WIN)) {
                moveForOutcome = switch (other) {
                    case ROCK -> PAPER;
                    case PAPER -> SCISSORS;
                    case SCISSORS -> ROCK;
                };
            } else {
                moveForOutcome = other;
            }
            return moveForOutcome;
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
        WIN("Z", 6), LOSE("X", 0), DRAW("Y", 3);

        final String code;
        final int points;

        Outcome(final String code, final int points) {
            this.code = code;
            this.points = points;
        }

        static Outcome of(final String code) {
            return Arrays.stream(Outcome.values())
                    .filter(v -> v.code.equalsIgnoreCase(code))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown outcome: " + code));
        }
    }
}
