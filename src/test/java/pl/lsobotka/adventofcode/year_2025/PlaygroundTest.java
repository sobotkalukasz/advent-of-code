package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaygroundTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/Playground_example", 10, 40), //
                Arguments.of("2025/Playground", 1000, 330786));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int conQty, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Playground playground = new Playground(input);
        final long actual = playground.makeConnections(conQty);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/Playground_example", 25272), //
                Arguments.of("2025/Playground", 3276581616L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Playground playground = new Playground(input);
        final long actual = playground.distanceByLastConnection();
        assertEquals(expected, actual);
    }

}