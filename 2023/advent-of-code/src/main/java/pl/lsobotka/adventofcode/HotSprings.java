package pl.lsobotka.adventofcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2023/day/12
 * */
public class HotSprings {

    private final List<ConditionRecord> conditionRecords;

    public HotSprings(final List<String> input) {
        this.conditionRecords = ConditionRecord.from(input);
    }

    long sumDifferentArrangements() {
        return conditionRecords.stream().map(ConditionRecord::countPossibleArrangement).reduce(0L, Long::sum);
    }

    record ConditionRecord(String condition, Pattern pattern) {
        private static final Pattern rowPattern = Pattern.compile("([{.?#}]+) ([0-9,]+)");
        private static final Pattern recordPattern = Pattern.compile("([0-9]+)");

        static List<ConditionRecord> from(List<String> input) {

            final List<ConditionRecord> conditionRecords = new ArrayList<>();

            for (String row : input) {
                final Matcher matcher = rowPattern.matcher(row);
                if (matcher.find()) {
                    final String condition = matcher.group(1);
                    final Matcher recordMatcher = recordPattern.matcher(matcher.group(2));
                    final List<Integer> records = recordMatcher.results()
                            .map(MatchResult::group)
                            .map(Integer::parseInt)
                            .toList();
                    conditionRecords.add(new ConditionRecord(condition, buildPattern(records)));
                }
            }

            return conditionRecords;
        }

        static private Pattern buildPattern(final List<Integer> records) {
            final StringBuilder patternBuilder = new StringBuilder();
            patternBuilder.append("\\.*");
            for (int i = 0; i < records.size(); i++) {
                patternBuilder.append("[#]{").append(records.get(i)).append("}");
                if (i == records.size() - 1) {
                    patternBuilder.append("\\.*");
                } else {
                    patternBuilder.append("\\.+");
                }
            }
            return Pattern.compile(patternBuilder.toString());
        }

        long countPossibleArrangement() {
            final Queue<String> possible = new ArrayDeque<>();
            possible.add(condition);

            for (int i = 0; i < condition.length(); i++) {
                final List<String> next = new ArrayList<>();
                while (!possible.isEmpty()) {
                    final String toValidate = possible.poll();
                    if (toValidate.charAt(i) == '?') {
                        StringBuilder string = new StringBuilder(toValidate);

                        string.setCharAt(i, '.');
                        next.add(string.toString());

                        string.setCharAt(i, '#');
                        next.add(string.toString());
                    } else {
                        next.add(toValidate);
                    }
                }
                possible.addAll(next);
            }
            return possible.stream().filter(pattern.asMatchPredicate()).count();
        }
    }
}
