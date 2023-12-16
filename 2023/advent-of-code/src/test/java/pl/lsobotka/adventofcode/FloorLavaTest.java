package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class FloorLavaTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("FloorLava_example", 46), //
                Arguments.of("FloorLava", 7498));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final FloorLava floorLava = new FloorLava(input);
        final long actual = floorLava.countEnergizedTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("FloorLava_example", 51), //
                Arguments.of("FloorLava", 7846));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final FloorLava floorLava = new FloorLava(input);
        final long actual = floorLava.maxEnergizedTiles();
        assertEquals(expected, actual);
    }

}