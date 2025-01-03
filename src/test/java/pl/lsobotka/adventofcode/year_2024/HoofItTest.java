package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class HoofItTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/HoofIt_example", 36), //
                Arguments.of("2024/HoofIt", 548));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HoofIt hoofIt = new HoofIt(input);
        final long actual = hoofIt.sumOfTrailHeadScores();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/HoofIt_example", 81), //
                Arguments.of("2024/HoofIt", 1252));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HoofIt hoofIt = new HoofIt(input);
        final long actual = hoofIt.sumOfTrailHeadRatings();
        assertEquals(expected, actual);
    }

}