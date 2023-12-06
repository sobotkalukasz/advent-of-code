package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WaitForIt {

    private static final Pattern TIME_PATTERN = Pattern.compile("Time:([0-9\\s]+)");
    private static final Pattern DISTANCE_PATTERN = Pattern.compile("Distance:([0-9\\s]+)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("([0-9]+)");

    private final List<Race> races;

    private WaitForIt(String input) {
        final Matcher timeMatcher = TIME_PATTERN.matcher(input);
        final Matcher distanceMatcher = DISTANCE_PATTERN.matcher(input);

        if (timeMatcher.find() && distanceMatcher.find()) {
            final List<Long> times = extractNumbers(timeMatcher.group(1));
            final List<Long> distances = extractNumbers(distanceMatcher.group(1));

            this.races = new ArrayList<>();
            for (int i = 0; i < times.size(); i++) {
                races.add(new Race(times.get(i), distances.get(i)));
            }

        } else {
            throw new IllegalArgumentException("Unable to read input: " + input);
        }
    }

    public static WaitForIt ofRaces(final List<String> input) {
        final String inputAsSingleRow = String.join(" ", input);
        return new WaitForIt(inputAsSingleRow);
    }

    public static WaitForIt ofSingleRace(final List<String> input) {
        final String inputAsSingleRow = String.join(" ", input).replaceAll(" ", "");
        return new WaitForIt(inputAsSingleRow);
    }

    private List<Long> extractNumbers(final String group) {
        return NUMBER_PATTERN.matcher(group).results().map(MatchResult::group).map(Long::parseLong).toList();
    }

    public long countWaysToBeatDistanceRecord() {
        return races.stream().map(Race::countWaysToBeatDistanceRecord).reduce(1L, (a, b) -> a * b);
    }

    record Race(long time, long distance) {

        long countWaysToBeatDistanceRecord() {
            long beatCount = 0;
            for (int i = 0; i < time; i++) {
                final long timeLeft = time - i;
                if (timeLeft * i > distance) {
                    beatCount++;
                }
            }
            return beatCount;
        }
    }
}
