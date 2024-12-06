package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class GuardGallivantTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/GuardGallivant_example", 41), //
                Arguments.of("2024/GuardGallivant", 4722));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final GuardGallivant guardGallivant = new GuardGallivant(input);
        final long actual = guardGallivant.countVisitedPositions();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/GuardGallivant_example", 6), //
                Arguments.of("2024/GuardGallivant", 1602)
        );
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final GuardGallivant guardGallivant = new GuardGallivant(input);
        final long actual = guardGallivant.possibleObstaclesToLoopGuard();
        assertEquals(expected, actual);
    }

}