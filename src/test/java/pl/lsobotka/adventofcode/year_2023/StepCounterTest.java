package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class StepCounterTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/StepCounter_example", 6, 16), //
                Arguments.of("2023/StepCounter", 64, 3716));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long steps, final long expected) {
        final List<String> input = getFileInput(fileName);
        final StepCounter stepCounter = new StepCounter(input);
        final long actual = stepCounter.howManyReached(steps);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/StepCounter_example", 6, 16), //
                Arguments.of("2023/StepCounter_example", 10, 50), //
                Arguments.of("2023/StepCounter_example", 50, 1594),//
                Arguments.of("2023/StepCounter_example", 100, 6536), //
                Arguments.of("2023/StepCounter_example", 500, 167004), //
                Arguments.of("2023/StepCounter_example", 1000, 668697), //
                Arguments.of("2023/StepCounter_example", 5000, 16733044), //
                Arguments.of("2023/StepCounter", 26501365, 616_583_483_179_597L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long steps, final long expected) {
        final List<String> input = getFileInput(fileName);
        final StepCounter stepCounter = new StepCounter(input);
        final long actual = stepCounter.howManyReachedInfinite(steps);
        assertEquals(expected, actual);
    }

}