package pl.lsobotka.adventofcode.year_2024;

import java.util.*;
import java.util.stream.Collectors;

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
                if (canMatchTowel(pattern, current.pointer, towel)) {
                    paths.add(new Path(current.pointer + towel.length()));
                }
            }
        }

        return false;
    }

    private boolean canMatchTowel(String pattern, int startPointer, String towel) {
        if (startPointer + towel.length() > pattern.length()) {
            return false;
        }
        for (int i = 1; i < towel.length(); i++) {
            if (pattern.charAt(startPointer + i) != towel.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    record Path(int pointer) {
    }
}
