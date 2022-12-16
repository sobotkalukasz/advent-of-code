package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ProboscideaVolcaniumTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("ProboscideaVolcanium_simple", 1651), //
                Arguments.of("ProboscideaVolcanium", 1940));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final ProboscideaVolcanium volcanium = new ProboscideaVolcanium(input);
        final int actual = volcanium.determineFlowRate();
        assertEquals(expected, actual);
    }

}