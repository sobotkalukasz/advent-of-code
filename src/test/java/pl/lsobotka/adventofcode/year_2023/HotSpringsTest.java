package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class HotSpringsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/HotSprings_example", 21), //
                Arguments.of("2023/HotSprings", 7260));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HotSprings hotSprings = new HotSprings(input);
        final long actual = hotSprings.sumDifferentArrangements();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/HotSprings_example", 5, 525152), //
                Arguments.of("2023/HotSprings", 5, 1909291258644L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final int times, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HotSprings hotSprings = new HotSprings(input);
        final long actual = hotSprings.sumUnfoldedDifferentArrangements(times);
        assertEquals(expected, actual);
    }
}