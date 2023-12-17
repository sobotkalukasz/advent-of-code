package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ClumsyCrucibleTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("ClumsyCrucible_example", 102), //
                Arguments.of("ClumsyCrucible", 791));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ClumsyCrucible clumsyCrucible = new ClumsyCrucible(input);
        final long actual = clumsyCrucible.minimalHeatLost();
        assertEquals(expected, actual);
    }

}