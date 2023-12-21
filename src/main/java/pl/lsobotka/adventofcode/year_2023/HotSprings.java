package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2023/day/12
 * */
public class HotSprings {

    private final List<String> input;

    public HotSprings(final List<String> input) {
        this.input = input;
    }

    long sumDifferentArrangements() {
        return sumUnfoldedDifferentArrangements(1);
    }

    long sumUnfoldedDifferentArrangements(final int unfoldTimes) {
        final List<ConditionRecord> conditionRecords = ConditionRecord.from(input, unfoldTimes);
        return conditionRecords.stream().map(ConditionRecord::possibleArrangement).reduce(0L, Long::sum);
    }

    record ConditionRecord(String condition, List<Integer> records) {

        private static final Pattern rowPattern = Pattern.compile("([{.?#}]+) ([0-9,]+)");
        private static final Pattern recordPattern = Pattern.compile("([0-9]+)");
        private static final char OPERATIONAL = '.';
        private static final char DAMAGED = '#';
        private static final char UNKNOWN = '?';

        static List<ConditionRecord> from(final List<String> input, final int unfoldTimes) {

            final List<ConditionRecord> conditionRecords = new ArrayList<>();

            for (String row : input) {
                final Matcher matcher = rowPattern.matcher(row);
                if (matcher.find()) {
                    final String condition = matcher.group(1).concat(String.valueOf(UNKNOWN)).repeat(unfoldTimes);
                    final String expandedCondition = condition.substring(0, condition.length() - 1);

                    final Matcher recordMatcher = recordPattern.matcher(
                            matcher.group(2).concat(" ").repeat(unfoldTimes));
                    final List<Integer> records = recordMatcher.results()
                            .map(MatchResult::group)
                            .map(Integer::parseInt)
                            .toList();

                    conditionRecords.add(new ConditionRecord(expandedCondition, records));
                }
            }

            return conditionRecords;
        }

        long possibleArrangement() {
            return possibleArrangement(new HashMap<>(), condition, records);
        }

        long possibleArrangement(final Map<CacheKey, Long> cache, String condition, List<Integer> records) {
            final CacheKey key = new CacheKey(condition, records);

            if (cache.containsKey(key)) {
                return cache.get(key);
            } else
                if (condition.isBlank()) {
                return records.isEmpty() ? 1 : 0;
            }

            long arrangements = 0;
            final char first = condition.charAt(0);

            if (first == UNKNOWN) {
                arrangements = possibleArrangement(cache, OPERATIONAL + condition.substring(1), records);
                arrangements += possibleArrangement(cache, DAMAGED + condition.substring(1), records);
            } else if (first == OPERATIONAL) {
                arrangements = possibleArrangement(cache, condition.substring(1), records);
            } else if (first == DAMAGED && !records.isEmpty()) {
                final int damagedRecord = records.getFirst();
                if (isDamagedRecordPossible(damagedRecord, condition)) {
                    final List<Integer> leftRecords = records.subList(1, records.size());
                    if (damagedRecord == condition.length()) {
                        arrangements = leftRecords.isEmpty() ? 1 : 0;
                    } else if (condition.charAt(damagedRecord) == OPERATIONAL) {
                        arrangements = possibleArrangement(cache, condition.substring(damagedRecord), leftRecords);
                    } else if (condition.charAt(damagedRecord) == UNKNOWN) {
                        arrangements = possibleArrangement(cache, OPERATIONAL + condition.substring(damagedRecord + 1),
                                leftRecords);
                    }
                }
            }
            cache.put(key, arrangements);
            return arrangements;
        }

        private boolean isDamagedRecordPossible(final int length, final String condition) {
            return length <= condition.length() && condition.substring(0, length)
                    .chars()
                    .noneMatch(c -> c == OPERATIONAL);
        }
    }

    record CacheKey(String condition, List<Integer> records) {
    }
}
