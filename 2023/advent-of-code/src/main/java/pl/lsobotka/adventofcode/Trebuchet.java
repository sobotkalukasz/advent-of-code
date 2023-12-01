package pl.lsobotka.adventofcode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2023/day/1
 * */
public class Trebuchet {

    private static final Map<String, Integer> dictionary = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
            "six", 6, "seven", 7, "eight", 8, "nine", 9);

    private final List<String> data;

    public Trebuchet(List<String> data) {
        this.data = data;

    }

    int sumOfCalibrationValues() {
        return data.stream().map(row -> extractRowNumber(row, false)).reduce(Integer::sum).orElse(0);
    }

    int sumOfComplexCalibrationValues() {
        return data.stream().map(row -> extractRowNumber(row, true)).reduce(Integer::sum).orElse(0);
    }

    private int extractRowNumber(final String row, final boolean includeDictionary) {
        final Deque<Integer> integers = new LinkedList<>();
        for (int i = 0; i < row.length(); i++) {
            if (Character.isDigit(row.charAt(i))) {
                integers.add(Character.getNumericValue(row.charAt(i)));
            } else if (includeDictionary) {
                final String subRow = row.substring(i);
                dictionary.keySet()
                        .stream()
                        .filter(subRow::startsWith)
                        .findFirst()
                        .ifPresent(key -> integers.add(dictionary.get(key)));
            }
        }
        final String digitString = String.valueOf(integers.getFirst()) + integers.getLast();
        return Integer.parseInt(digitString);
    }
}
