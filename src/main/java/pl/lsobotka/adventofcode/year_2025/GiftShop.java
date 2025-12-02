package pl.lsobotka.adventofcode.year_2025;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/*
 * https://adventofcode.com/2025/day/2
 * */
public class GiftShop {

    private final  List<Range> ranges;

    public GiftShop(final String input) {
        ranges = Range.from(input);
    }

    public long sumOfInvalidIds() {
        return ranges.parallelStream().map(Range::findInvalidIds).flatMap(List::stream).reduce(Long::sum).orElse(0L);
    }

    public long sumOfInvalidIdsByPattern() {
        return ranges.parallelStream()
                .map(Range::findInvalidIdsPattern)
                .flatMap(List::stream)
                .reduce(Long::sum)
                .orElse(0L);
    }

    private record Range(long min, long max) {

        public static List<Range> from(final String input) {
            return Arrays.stream(input.split(",")).map(range -> {
                final String[] split = range.split("-");
                return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
            }).toList();
        }

        public List<Long> findInvalidIds() {
            return LongStream.rangeClosed(min, max).filter(this::isInvalid).boxed().toList();
        }

        public boolean isInvalid(long id) {
            final char[] chars = String.valueOf(id).toCharArray();
            if (chars.length % 2 != 0) {
                return false;
            }

            final int half = chars.length / 2;

            for (int i = 0; i < half; i++) {
                if (chars[i] != chars[i + half]) {
                    return false;
                }
            }

            return true;
        }

        public List<Long> findInvalidIdsPattern() {
            return LongStream.rangeClosed(min, max).filter(this::hasPattern).boxed().toList();
        }

        public boolean hasPattern(long id) {
            final char[] chars = String.valueOf(id).toCharArray();
            final int length = chars.length;

            final int[] divisors = divisors(length);

            for (int i = 1; i < divisors.length; i++) {
                final int divisor = divisors[i];
                final int size = length / divisor;

                if (hasPattern(chars, divisor, size)) {
                    return true;
                }
            }

            return false;
        }

        private boolean hasPattern(final char[] chars, final int partsQty, final int length) {
            for (int part = 1; part < partsQty; part++) {
                int offset = part * length;
                for (int idx = 0; idx < length; idx++) {
                    if (chars[idx] != chars[offset + idx]) {
                        return false;
                    }
                }
            }
            return true;
        }

        private int[] divisors(final int id) {
            return IntStream.rangeClosed(1, id).filter(i -> id % i == 0).toArray();
        }

    }
}
