package pl.lsobotka.adventofcode.diracdice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/21
 * */
public class DiracDice {
    private static final List<DiracSum> DIRAC_DICE_SUM_PERMUTATIONS = countAllPermutations();

    public static long playSimpleGame(final int firstPos, final int secondPos) {
        final Player first = new Player(firstPos);
        final Player second = new Player(secondPos);
        final Dice dice = new Dice();

        boolean play = true;
        while (play) {
            if (first.applyTurn(dice) >= 1000) {
                play = false;
            } else if (second.applyTurn(dice) >= 1000) {
                play = false;
            }
        }

        final int rollCount = dice.getRollCount();
        final int lowerScore = Math.min(first.getScore(), second.getScore());

        return (long) rollCount * lowerScore;
    }

    public static long playQuantumDice(final int firstPos, final int secondPos) {
        final Result result = playQuantumDice(firstPos, 0, secondPos, 0);
        return result.max();
    }

    private static Result playQuantumDice(final int firstPos, final int firstScore, final int secondPos,
            final int secondScore) {

        if (firstScore >= 21) {
            return Result.first();
        } else if (secondScore >= 21) {
            return Result.second();
        }

        Result totalResult = Result.init(0, 0);

        for (DiracSum permutation : DIRAC_DICE_SUM_PERMUTATIONS) {
            final int tempPos = (firstPos + permutation.sum()) % 10;
            final int tempScore = firstScore + (tempPos == 0 ? 10 : tempPos);
            final Result result = playQuantumDice(secondPos, secondScore, tempPos, tempScore);
            totalResult = totalResult.add(result.secondPlayer * permutation.times(),
                    result.firstPlayer * permutation.times());
        }
        return totalResult;
    }

    private record DiracSum(int sum, long times) {
        public static DiracSum init(final int sum, final long times) {
            return new DiracSum(sum, times);
        }
    }

    private record Result(long firstPlayer, long secondPlayer) {
        public static Result init(final long firstPlayer, final long secondPlayer) {
            return new Result(firstPlayer, secondPlayer);
        }

        public static Result first() {
            return new Result(1, 0);
        }

        public static Result second() {
            return new Result(0, 1);
        }

        public Result add(final long firstPlayer, final long secondPlayer) {
            return Result.init(this.firstPlayer + firstPlayer, this.secondPlayer + secondPlayer);
        }

        public long max() {
            return Math.max(firstPlayer, secondPlayer);
        }
    }

    public static List<DiracSum> countAllPermutations() {
        final Map<Integer, Integer> permutations = new HashMap<>();

        for (int a = 1; a <= 3; a++) {
            for (int b = 1; b <= 3; b++) {
                for (int c = 1; c <= 3; c++) {
                    final int value = a + b + c;

                    if (Objects.isNull(permutations.computeIfPresent(value, (k, v) -> v + 1))) {
                        permutations.put(value, 1);
                    }
                }
            }
        }

        return permutations.entrySet()
                .stream()
                .map(e -> DiracSum.init(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

}
