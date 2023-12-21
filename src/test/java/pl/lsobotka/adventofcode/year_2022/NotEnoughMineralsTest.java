package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class NotEnoughMineralsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/NotEnoughMinerals_simple", 33), //
                Arguments.of("2022/NotEnoughMinerals", 1382));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final NotEnoughMinerals minerals = new NotEnoughMinerals(input);
        final int actual = minerals.qualityLevel();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/NotEnoughMinerals_simple", 2, 3472), //
                Arguments.of("2022/NotEnoughMinerals", 3, 31740));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int amount, final long expected) {
        final List<String> input = getFileInput(fileName);
        final NotEnoughMinerals minerals = new NotEnoughMinerals(input);
        final int actual = minerals.qualityLevelOfFirst(amount);
        assertEquals(expected, actual);
    }

}