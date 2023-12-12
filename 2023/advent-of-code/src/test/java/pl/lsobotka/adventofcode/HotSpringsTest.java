package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class HotSpringsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("HotSprings_example", 21), //
                Arguments.of("HotSprings", 7260));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HotSprings hotSprings = new HotSprings(input);
        final long actual = hotSprings.sumDifferentArrangements();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("HotSprings_example", 5, 525152), //
                Arguments.of("HotSprings", 5, 1909291258644L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int times, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HotSprings hotSprings = new HotSprings(input);
        final long actual = hotSprings.sumUnfoldedDifferentArrangements(times);
        assertEquals(expected, actual);
    }
}