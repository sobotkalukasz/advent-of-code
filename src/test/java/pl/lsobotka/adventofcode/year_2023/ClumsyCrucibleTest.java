package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ClumsyCrucibleTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/ClumsyCrucible_example_1", 102), //
                Arguments.of("2023/ClumsyCrucible", 791));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ClumsyCrucible clumsyCrucible = new ClumsyCrucible(input);
        final long actual = clumsyCrucible.minimalHeatLost();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/ClumsyCrucible_example_1", 94), //
                Arguments.of("2023/ClumsyCrucible_example_2", 71), //
                Arguments.of("2023/ClumsyCrucible", 900));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ClumsyCrucible clumsyCrucible = new ClumsyCrucible(input);
        final long actual = clumsyCrucible.minimalHeatLostWithComplexDirections();
        assertEquals(expected, actual);
    }

}