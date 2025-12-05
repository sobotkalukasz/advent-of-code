package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2025/day/5
 * */
public class Cafeteria {

    private static final Pattern RANGE_PATTERN = Pattern.compile("^(?<min>\\d+)-(?<max>\\d+)$");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^(?<value>\\d+)$");

    private final List<Range> ranges;
    private final List<Long> ids;

    public Cafeteria(final List<String> input) {
        this.ranges = input.stream().map(Range::of).filter(Objects::nonNull).toList();
        this.ids = input.stream().map(this::getId).filter(Objects::nonNull).toList();
    }

    public long countValid() {
        return ids.stream().filter(this::isValid).count();
    }

    public long countAll() {
        final Deque<Range> queue = new ArrayDeque<>(ranges);
        final Set<Range> verified = new HashSet<>();

        while (!queue.isEmpty()) {
            final Range current = queue.removeFirst();
            final Optional<Range> overlaps = queue.stream().filter(current::overlaps).findFirst();
            if (overlaps.isPresent()) {
                final List<Range> split = current.split(overlaps.get());
                queue.remove(overlaps.get());
                queue.addAll(split);
            } else {
                verified.add(current);
            }
        }

        return verified.stream().mapToLong(Range::getPossible).sum();
    }

    private boolean isValid(long value) {
        return ranges.stream().anyMatch(range -> range.contains(value));
    }

    private Long getId(final String raw) {
        final var matcher = DIGIT_PATTERN.matcher(raw);
        if (matcher.find()) {
            return Long.parseLong(matcher.group("value"));
        }
        return null;
    }

    record Range(long min, long max) {

        static Range of(final String raw) {
            final var matcher = RANGE_PATTERN.matcher(raw);
            if (matcher.find()) {
                return new Range(Long.parseLong(matcher.group("min")), Long.parseLong(matcher.group("max")));
            }
            return null;
        }

        boolean contains(final long value) {
            return value >= min && value <= max;
        }

        boolean overlaps(final Range other) {
            return other.contains(min) || other.contains(max);
        }

        long getPossible() {
            return max - min + 1;
        }

        private List<Range> split(final Range other) {
            final List<Long> cuts = Stream.of(min, max, other.min, other.max).distinct().sorted().toList();

            final List<Range> result = new ArrayList<>();
            for (int i = 0; i < cuts.size() - 1; i++) {
                if (i == 0) {
                    result.add(new Range(cuts.get(i), cuts.get(i + 1)));
                } else {
                    result.add(new Range(cuts.get(i) + 1, cuts.get(i + 1)));
                }
            }
            return result;
        }

    }
}
