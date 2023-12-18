package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class LavaductLagoonTest extends BaseTest {
    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("LavaductLagoon_example", 62), //
                Arguments.of("LavaductLagoon", 76_387));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final LavaductLagoon lavaductLagoon = new LavaductLagoon(input);
        final long actual = lavaductLagoon.lagoonSize();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("LavaductLagoon_example", 952_408_144_115L), //
                Arguments.of("LavaductLagoon", 250_022_188_522_074L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final LavaductLagoon lavaductLagoon = new LavaductLagoon(input);
        final long actual = lavaductLagoon.extendedLagoonSize();
        assertEquals(expected, actual);
    }
}