package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2024/day/1
 * */
public class HistorianHysteria {

    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\s+(\\d+)");

    private final List<Integer> left;
    private final List<Integer> right;

    public HistorianHysteria(final List<String> input) {
        left = new ArrayList<>();
        right = new ArrayList<>();

        input.forEach(line -> {
            final Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                left.add(Integer.parseInt(matcher.group(1)));
                right.add(Integer.parseInt(matcher.group(2)));
            }
        });
    }

    long totalDistance() {
        Collections.sort(left);
        Collections.sort(right);

        int distance = 0;
        for (int i = 0; i < left.size(); i++) {
            distance += Math.abs(left.get(i) - (right.get(i)));
        }

        return distance;
    }

    long similarityScore() {
        final Map<Integer, Long> frequency = right.stream()
                .collect(Collectors.groupingBy(number -> number, Collectors.counting()));
        return left.stream().map(id -> frequency.getOrDefault(id, 0L) * id).reduce(Long::sum).orElse(0L);
    }
}
