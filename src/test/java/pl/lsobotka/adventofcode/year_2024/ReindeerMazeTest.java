package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class ReindeerMazeTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/ReindeerMaze_example_1", 7036), //
                Arguments.of("2024/ReindeerMaze_example_2", 11048), //
                Arguments.of("2024/ReindeerMaze", 115500));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final ReindeerMaze reindeerMaze = new ReindeerMaze(lines);
        final long actual = reindeerMaze.getBestScoreFromMaze();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/ReindeerMaze_example_1", 45), //
                Arguments.of("2024/ReindeerMaze_example_2", 64), //
                Arguments.of("2024/ReindeerMaze", 679));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final ReindeerMaze reindeerMaze = new ReindeerMaze(lines);
        final long actual = reindeerMaze.countBestTiles();
        assertEquals(expected, actual);
    }

}