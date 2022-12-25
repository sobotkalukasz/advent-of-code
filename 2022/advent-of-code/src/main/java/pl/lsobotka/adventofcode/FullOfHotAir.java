package pl.lsobotka.adventofcode;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2022/day/24
 * */
public class FullOfHotAir {

    private static final Map<Character, Integer> toDecimalMapping = Map.of('2', 2, '1', 1, '0', 0, '-', -1, '=', -2);
    private static final Map<Long, String> toSnafuMapping = Map.of(0L, "0", 1L, "1", 2L, "2", 3L, "=", 4L, "-");

    List<String> snafu;

    FullOfHotAir(final List<String> input) {
        this.snafu = input;
    }

    public String sumSnafu() {
        final long decimal = snafu.stream().mapToLong(this::toDecimal).sum();
        return toSnafu(decimal);
    }

    private long toDecimal(final String snafu) {
        long decimal = 0;

        for (int index = 0; index < snafu.length(); index++) {
            final char c = snafu.charAt(snafu.length() - 1 - index);
            final int digit = toDecimalMapping.get(c);
            final long pow = BigInteger.valueOf(5).pow(index).longValue();
            final long number = digit * pow;
            decimal += number;
        }
        return decimal;
    }

    private String toSnafu(final long number) {
        String snafu;
        if (number == 0) {
            snafu = "";
        } else {
            final long reminder = number % 5;
            final String snafuMapping = toSnafuMapping.get(reminder);
            final int subtract = toDecimalMapping.get(snafuMapping.charAt(0));
            snafu = toSnafu((number - subtract) / 5).concat(snafuMapping);
        }
        return snafu;
    }

}
