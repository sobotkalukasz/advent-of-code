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

    long countPossibleSafeReports() {
        return reports.stream().filter(Report::isSafeOrCanBeSafe).count();
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
        return (validate(INCREASE) || validate(DECREASE)) && validate(DIFF_3);
    }

    boolean isSafeOrCanBeSafe() {
        boolean canBeSafe = (validate(INCREASE) || validate(DECREASE)) && validate(DIFF_3);
        for (int idxToSkip = 0; idxToSkip < levels().size(); idxToSkip++) {
            if (!canBeSafe) {
                final List<Integer> copy = new ArrayList<>(levels.subList(0, idxToSkip));
                copy.addAll(levels.subList(idxToSkip + 1, levels.size()));
                canBeSafe = (validate(INCREASE, copy) || validate(DECREASE, copy)) && validate(DIFF_3, copy);
            }
        }

        return canBeSafe;
    }

    private boolean validate(final BiPredicate<Integer, Integer> predicate) {
        return validate(predicate, levels);
    }

    private boolean validate(final BiPredicate<Integer, Integer> predicate, final List<Integer> copy) {
        for (int i = 1; i < copy.size(); i++) {
            int previous = copy.get(i - 1);
            int current = copy.get(i);
            if (!predicate.test(previous, current)) {
                return false;
            }
        }
        return true;
    }

}
