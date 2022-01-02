package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2021/day/8
 * */
public class SevenSegmentSearch {

    private final static String OUTPUT_VALUE_DELIMITER = "\\|";
    private final static int ONE_DIGIT_SEGMENTS = 2;
    private final static int FOUR_DIGIT_SEGMENTS = 4;
    private final static int SEVEN_DIGIT_SEGMENTS = 3;
    private final static int EIGHT_DIGIT_SEGMENTS = 7;

    public long countUniqueOutputValues(final List<String> input) {
        return input.stream()
                .map(str -> str.split(OUTPUT_VALUE_DELIMITER))
                .map(split -> split[1])
                .map(str -> str.split(" "))
                .flatMap(Stream::of)
                .filter(this::isUniqueDigit)
                .count();
    }

    private boolean isUniqueDigit(final String value) {
        final int length = value.length();
        return length == ONE_DIGIT_SEGMENTS || length == FOUR_DIGIT_SEGMENTS || length == SEVEN_DIGIT_SEGMENTS
                || length == EIGHT_DIGIT_SEGMENTS;
    }

    public long sumOutputValues(final List<String> input) {
        return input.stream().map(this::sumRow).reduce(Long::sum).orElse(0L);
    }

    private long sumRow(final String row) {
        final String[] split = row.split(OUTPUT_VALUE_DELIMITER);
        final Set<String> uniqueDigitSegments = getUniqueSegmentsAsStrings(split[0], isUniqueDigitPredicate());
        final Set<String> uniqueDigitSegmentsWithoutFour = getUniqueSegmentsAsStrings(split[0],
                isUniqueDigitWithoutFourPredicate());
        final String digitAsString = Arrays.stream(split[1].split(" "))
                .filter(str -> !str.isEmpty())
                .map(digit -> decodeDigit(digit, uniqueDigitSegments, uniqueDigitSegmentsWithoutFour))
                .reduce(String::concat)
                .orElse("");
        return Long.parseLong(digitAsString);
    }

    private Set<String> getUniqueSegmentsAsStrings(final String data, final Predicate<String> filter) {
        return Arrays.stream(data.split("\\s"))
                .filter(filter)
                .map(str -> str.split(""))
                .flatMap(Stream::of)
                .collect(Collectors.toSet());
    }

    private Predicate<String> isUniqueDigitPredicate() {
        return value -> {
            final int length = value.length();
            return length == ONE_DIGIT_SEGMENTS || length == FOUR_DIGIT_SEGMENTS || length == SEVEN_DIGIT_SEGMENTS;
        };
    }

    private Predicate<String> isUniqueDigitWithoutFourPredicate() {
        return value -> {
            final int length = value.length();
            return length == ONE_DIGIT_SEGMENTS || length == SEVEN_DIGIT_SEGMENTS;
        };
    }

    private String decodeDigit(final String digit, final Set<String> uniqueSegments,
            final Set<String> uniqueSegmentsWithoutFour) {
        final int segments = digit.length();
        final Set<String> digitSegments = Arrays.stream(digit.split("")).collect(Collectors.toSet());
        final int commonUniqueDigit = countCommonElements(digitSegments, uniqueSegments);
        final int commonUniqueWithoutFour = countCommonElements(digitSegments, uniqueSegmentsWithoutFour);
        final String result;
        if (segments == ONE_DIGIT_SEGMENTS) {
            result = "1";
        } else if (segments == FOUR_DIGIT_SEGMENTS) {
            result = "4";
        } else if (segments == SEVEN_DIGIT_SEGMENTS) {
            result = "7";
        } else if (segments == EIGHT_DIGIT_SEGMENTS) {
            result = "8";
        } else if (segments == 5) {
            if (commonUniqueDigit == 3) {
                result = "2";
            } else if (commonUniqueWithoutFour == 2) {
                result = "5";
            } else {
                result = "3";
            }
        } else if (segments == 6) {
            if (commonUniqueDigit == 5) {
                result = "9";
            } else if (commonUniqueWithoutFour == 2) {
                result = "6";
            } else {
                result = "0";
            }
        } else {
            result = "";
        }
        return result;
    }

    public int countCommonElements(final Set<String> s1, final Set<String> s2) {
        final HashSet<String> copy = new HashSet<>(s1);
        copy.retainAll(s2);
        return copy.size();
    }
}
