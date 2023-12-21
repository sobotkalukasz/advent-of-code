package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ManyWorldsInterpretationTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of( //
                Arguments.of("ManyWorldsInterpretation_simple_01", 8), //
                Arguments.of("ManyWorldsInterpretation_simple_02", 86), //
                Arguments.of("ManyWorldsInterpretation_simple_03", 132), //
                Arguments.of("ManyWorldsInterpretation_simple_05", 81), //
                Arguments.of("ManyWorldsInterpretation_simple_04", 136), //
                Arguments.of("ManyWorldsInterpretation", 5068));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ManyWorldsInterpretation interpretation = new ManyWorldsInterpretation(input);
        final int actual = interpretation.shortestPathToCollectAllKeys();
        assertEquals(expected, actual);
    }

}