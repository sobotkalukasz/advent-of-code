package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class StepCounterTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("StepCounter_example", 6, 16), //
                Arguments.of("StepCounter", 64, 3716));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long steps, final long expected) {
        final List<String> input = getFileInput(fileName);
        final StepCounter stepCounter = new StepCounter(input);
        final long actual = stepCounter.howManyReached(steps);
        assertEquals(expected, actual);
    }

}