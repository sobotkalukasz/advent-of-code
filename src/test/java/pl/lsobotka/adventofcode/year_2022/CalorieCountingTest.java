package pl.lsobotka.adventofcode.year_2022;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class CalorieCountingTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of(
                Arrays.asList("1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000", "",
                        "10000"), 24_000));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void caloriesCountingTest(final List<String> input, final long expected) {
        final CalorieCounting counting = new CalorieCounting(input);
        final long actual = counting.getMostCalories();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleSecondStarExample() {
        return Stream.of(Arguments.of(
                Arrays.asList("1000", "2000", "3000", "", "4000", "", "5000", "6000", "", "7000", "8000", "9000", "",
                        "10000"), 3, 45_000));
    }

    @ParameterizedTest
    @MethodSource("simpleSecondStarExample")
    public void caloriesTopCountingTest(final List<String> input, final int top, final long expected) {
        final CalorieCounting counting = new CalorieCounting(input);
        final long actual = counting.getTopCalories(top);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("2022/CalorieCounting", 70698));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarCalorieCountingTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CalorieCounting counting = new CalorieCounting(input);
        final long actual = counting.getMostCalories();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStarFile() {
        return Stream.of(Arguments.of("2022/CalorieCounting", 3, 206_643));
    }

    @ParameterizedTest
    @MethodSource("secondStarFile")
    public void secondStarCalorieCountingTest(final String fileName, final int top, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CalorieCounting counting = new CalorieCounting(input);
        final long actual = counting.getTopCalories(top);
        assertEquals(expected, actual);
    }

}