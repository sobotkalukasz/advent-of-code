package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    long mostBananas(final int time) {
        final Map<String, Long> prices = new HashMap<>();
        for (Long value : values) {
            final Set<String> alreadyStored = new HashSet<>();
            final List<Long> secrets = getPrices(value, time);
            for (int i = 4; i < secrets.size(); i++) {
                long a = secrets.get(i - 4);
                long b = secrets.get(i - 3);
                long c = secrets.get(i - 2);
                long d = secrets.get(i - 1);
                long e = secrets.get(i);

                final String key = (b - a) + "," + (c - b) + "," + (d - c) + "," + (e - d);
                if (alreadyStored.add(key)) {
                    prices.merge(key, e, Long::sum);
                }
            }
        }

        return prices.values().stream().mapToLong(v -> v).max().orElse(0);
    }

    private List<Long> getPrices(long initial, int times) {
        List<Long> prices = new ArrayList<>(times);
        prices.add(initial % 10);

        long result = initial;
        for (int t = 0; t < times; t++) {
            result = next(result);
            prices.add(result % 10);
        }

        return prices;
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
