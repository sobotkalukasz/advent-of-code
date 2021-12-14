package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2021/day/14
 * */
public class ExtendedPolymerization {

    private final String template;
    private final Map<String, String> insertionPairs;

    public ExtendedPolymerization(final List<String> input) {
        insertionPairs = new HashMap<>();
        String template = "";
        for (String row : input) {
            if (row.contains("->")) {
                final String[] split = row.replace(" ", "").split("->");
                insertionPairs.put(split[0], split[1]);
            } else if (!row.isEmpty()) {
                template = row;
            }
        }
        this.template = template;
    }

    public long getFormula(int steps) {
        Map<String, Long> pairCount = initialTemplatePairs();

        while (steps-- > 0) {
            pairCount = applyInsertion(pairCount);
        }

        final Map<Character, Long> occurrence = countElements(pairCount);

        final long max = occurrence.values().stream().max(Long::compareTo).orElse(0L);
        final long min = occurrence.values().stream().min(Long::compareTo).orElse(0L);

        return max - min;
    }

    private Map<String, Long> initialTemplatePairs() {
        final Map<String, Long> count = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            final String pair = template.substring(i, i + 2);
            if (count.containsValue(pair)) {
                count.computeIfPresent(pair, (k, v) -> v++);
            } else {
                count.put(pair, 1L);
            }
        }
        return count;
    }

    public Map<String, Long> applyInsertion(final Map<String, Long> pairCount) {
        final Map<String, Long> stepCount = new HashMap<>();
        pairCount.forEach((pair, count) -> {
            if (insertionPairs.containsKey(pair)) {
                final String toAdd = insertionPairs.get(pair);
                final String[] split = pair.split("");
                Arrays.asList(split[0].concat(toAdd), toAdd.concat(split[1])).forEach(newPair -> {
                    if (stepCount.containsKey(newPair)) {
                        stepCount.computeIfPresent(newPair, (k, v) -> v + count);
                    } else {
                        stepCount.put(newPair, count);
                    }
                });
            }
        });
        return stepCount;
    }

    private Map<Character, Long> countElements(final Map<String, Long> pairCount) {
        final Map<Character, Long> elementCount = new HashMap<>();
        pairCount.forEach((pair, count) -> {
            countElement(pair.charAt(0), count, elementCount);
            countElement(pair.charAt(1), count, elementCount);
        });
        elementCount.keySet().forEach(key -> elementCount.computeIfPresent(key, (k, v) -> v / 2));

        final char last = template.charAt(template.length() - 1);
        elementCount.computeIfPresent(last, (k, v) -> v + 1);

        return elementCount;
    }

    private void countElement(final char c, final long count, Map<Character, Long> elementCount) {
        if (elementCount.containsKey(c)) {
            elementCount.computeIfPresent(c, (p, v) -> v + count);
        } else {
            elementCount.put(c, count);
        }
    }
}
