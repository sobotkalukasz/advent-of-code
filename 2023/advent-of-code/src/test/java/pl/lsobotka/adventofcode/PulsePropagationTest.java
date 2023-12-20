package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class PulsePropagationTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("PulsePropagation_example_1", 1000, 32_000_000), //
                Arguments.of("PulsePropagation_example_2", 1000, 11_687_500),
                Arguments.of("PulsePropagation", 1000, 825_896_364));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long times, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PulsePropagation pulsePropagation = new PulsePropagation(input);
        final long actual = pulsePropagation.pressButton(times);
        assertEquals(expected, actual);
    }

}