package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class PipeMazeTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/PipeMaze_example", 4), //
                Arguments.of("2023/PipeMaze_example_2", 8), //
                Arguments.of("2023/PipeMaze", 6867));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PipeMaze pipeMaze = new PipeMaze(input);
        final long actual = pipeMaze.farthestStep();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/PipeMaze_example_3", 4), //
                Arguments.of("2023/PipeMaze_example_4", 4), //
                Arguments.of("2023/PipeMaze_example_5", 8), //
                Arguments.of("2023/PipeMaze_example_6", 10), //
                Arguments.of("2023/PipeMaze", 595));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PipeMaze pipeMaze = new PipeMaze(input);
        final long actual = pipeMaze.countEnclosedTiles();
        assertEquals(expected, actual);
    }

}