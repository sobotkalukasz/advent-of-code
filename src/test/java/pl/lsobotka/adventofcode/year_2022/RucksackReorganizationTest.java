package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class RucksackReorganizationTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of(
                List.of("vJrwpWtwJgWrhcsFMMfFFhFp", "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", "PmmdzqPrVvPwwTWBwg",
                        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "ttgJtRGJQctTZtZT", "CrZsJsPPZsGzwwsLwLmpwMDw"), 157));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void simpleFirstStarExampleTest(final List<String> input, final long expected) {
        final RucksackReorganization reorganization = new RucksackReorganization(input);
        long actual = reorganization.sumPriorityOfCommonItems();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleSecondStarExample() {
        return Stream.of(Arguments.of(
                List.of("vJrwpWtwJgWrhcsFMMfFFhFp", "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", "PmmdzqPrVvPwwTWBwg",
                        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "ttgJtRGJQctTZtZT", "CrZsJsPPZsGzwwsLwLmpwMDw"), 70));
    }

    @ParameterizedTest
    @MethodSource("simpleSecondStarExample")
    public void simpleSecondStarExampleTest(final List<String> input, final long expected) {
        final RucksackReorganization reorganization = new RucksackReorganization(input);
        long actual = reorganization.sumPriorityOfCommonItemsInGroupOfRucksacks();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("2022/RucksackReorganization", 8109));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final RucksackReorganization reorganization = new RucksackReorganization(input);
        long actual = reorganization.sumPriorityOfCommonItems();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStarFile() {
        return Stream.of(Arguments.of("2022/RucksackReorganization", 2738));
    }

    @ParameterizedTest
    @MethodSource("secondStarFile")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final RucksackReorganization reorganization = new RucksackReorganization(input);
        long actual = reorganization.sumPriorityOfCommonItemsInGroupOfRucksacks();
        assertEquals(expected, actual);
    }



}