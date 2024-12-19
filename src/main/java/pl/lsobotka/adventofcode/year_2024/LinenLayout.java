package pl.lsobotka.adventofcode.year_2024;

import java.util.*;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2024/day/19
 * */
public class LinenLayout {

    Map<Character, List<String>> towels;
    List<String> patterns;

    LinenLayout(final List<String> lines) {
        this.towels = new HashMap<>();
        this.patterns = new ArrayList<>();

        for (String line : lines) {
            if (line.contains(",")) {
                towels.putAll(Arrays.stream(line.split(","))
                        .map(String::trim)
                        .collect(Collectors.groupingBy(s -> s.charAt(0))));
            } else if (!line.isEmpty()) {
                patterns.add(line);
            }
        }
    }

    long countPossiblePatterns() {
        return patterns.stream().filter(this::isPatternPossible).count();
    }

    long countPossibleWays() {
        return patterns.stream().parallel().filter(this::isPatternPossible).mapToLong(this::countPossibleWays).sum();
    }

    private boolean isPatternPossible(final String pattern) {

        final Queue<Path> paths = new PriorityQueue<>(Comparator.comparingInt(Path::pointer).reversed());
        paths.add(new Path(0));

        while (!paths.isEmpty()) {
            final Path current = paths.poll();
            if (current.pointer == pattern.length()) {
                return true;
            }
            final char actual = pattern.charAt(current.pointer);
            for (String towel : towels.getOrDefault(actual, Collections.emptyList())) {
                if (pattern.startsWith(towel, current.pointer)) {
                    paths.add(new Path(current.pointer + towel.length()));
                }
            }
        }

        return false;
    }

    private long countPossibleWays(String pattern) {
        int length = pattern.length();
        long[] dp = new long[length + 1];
        dp[0] = 1;

        for (int i = 0; i < length; i++) {
            if (dp[i] > 0) {
                char actual = pattern.charAt(i);
                for (String towel : towels.getOrDefault(actual, Collections.emptyList())) {
                    if (pattern.startsWith(towel, i)) {
                        dp[i + towel.length()] += dp[i];
                    }
                }
            }
        }

        return dp[length];
    }

    record Path(int pointer) {
    }
}
