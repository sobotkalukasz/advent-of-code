package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2024/day/2
 * */
public class RedNosedReport {

    private final List<Report> reports;

    public RedNosedReport(final List<String> lines) {
        reports = lines.stream().map(Report::parse).toList();
    }

    long countSafeReports() {
        return reports.stream().filter(Report::isSafe).count();
    }

}

record Report(List<Integer> levels) {

    private static final Pattern PATTERN = Pattern.compile("\\b\\d+\\b");

    private static final BiPredicate<Integer, Integer> INCREASE = (previous, current) -> previous < current;
    private static final BiPredicate<Integer, Integer> DECREASE = (previous, current) -> previous > current;
    private static final BiPredicate<Integer, Integer> DIFF_3 = (previous, current) -> Math.abs(previous - current)
            <= 3;

    static Report parse(final String input) {
        final Matcher matcher = PATTERN.matcher(input);
        final List<Integer> numbers = new ArrayList<>();
        while (matcher.find()) {
            numbers.add(Integer.parseInt(matcher.group()));
        }
        return new Report(numbers);
    }

    boolean isSafe() {
        boolean safe = false;
        if (validate(INCREASE) || validate(DECREASE)) {
            safe = validate(DIFF_3);
        }
        return safe;
    }

    private boolean validate(final BiPredicate<Integer, Integer> test) {

        for (int i = 1; i < levels.size(); i++) {
            int previous = levels.get(i - 1);
            int current = levels.get(i);
            if (!test.test(previous, current)) {
                return false;
            }
        }
        return true;
    }
}
