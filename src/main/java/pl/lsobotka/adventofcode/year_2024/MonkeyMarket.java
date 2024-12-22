package pl.lsobotka.adventofcode.year_2024;

import java.util.List;

/*
 * https://adventofcode.com/2024/day/22
 * */
public class MonkeyMarket {

    final List<Long> values;

    MonkeyMarket(List<Integer> input) {
        this.values = input.stream().map(Long::valueOf).toList();
    }

    long sumSecretNumbersAfter(final int time) {
        return values.stream().mapToLong(v -> calculateSecretNumber(v, time)).sum();
    }

    private long calculateSecretNumber(long initial, int times) {

        long result = initial;
        for (int t = 0; t < times; t++) {
            result = next(result);
        }
        return result;
    }

    private long next(long initial) {
        long secret = initial;

        long value = initial * 64;
        value = value ^ secret; //mix
        secret = value % 16777216; //prune

        value = secret / 32;
        value = value ^ secret;
        secret = value % 16777216;

        value = secret * 2048;
        value = value ^ secret;
        secret = value % 16777216;

        return secret;
    }
}
