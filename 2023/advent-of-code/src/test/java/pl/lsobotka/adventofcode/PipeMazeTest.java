package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class PipeMazeTest extends BaseTest{

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("PipeMaze_example", 4), //
                Arguments.of("PipeMaze_example_2", 8), //
                Arguments.of("PipeMaze", 6867));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PipeMaze pipeMaze = new PipeMaze(input);
        final long actual = pipeMaze.solveIt();
        assertEquals(expected, actual);
    }

}