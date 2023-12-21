package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class PulsePropagationTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/PulsePropagation_example_1", 1000, 32_000_000), //
                Arguments.of("2023/PulsePropagation_example_2", 1000, 11_687_500),
                Arguments.of("2023/PulsePropagation", 1000, 825_896_364));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long times, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PulsePropagation pulsePropagation = new PulsePropagation(input);
        final long actual = pulsePropagation.pressButton(times);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/PulsePropagation", "rx", 243_566_897_206_981L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final String outputModule, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PulsePropagation pulsePropagation = new PulsePropagation(input);
        final long actual = pulsePropagation.whenModuleReceiveLowPulse(outputModule);
        assertEquals(expected, actual);
    }

}