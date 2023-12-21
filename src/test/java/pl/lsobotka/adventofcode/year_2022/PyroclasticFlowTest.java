package pl.lsobotka.adventofcode.year_2022;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class PyroclasticFlowTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/PyroclasticFlow_simple", 2022, 3068), //
                Arguments.of("2022/PyroclasticFlow", 2022, 3153));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long rounds, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final PyroclasticFlow flow = new PyroclasticFlow(input);
        final long actual = flow.rockFall(rounds);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/PyroclasticFlow_simple", 1_000_000_000_000L, 1_514_285_714_288L),
                Arguments.of("2022/PyroclasticFlow", 1_000_000_000_000L, 1_553_665_689_155L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long rounds, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final PyroclasticFlow flow = new PyroclasticFlow(input);
        final long actual = flow.rockFall(rounds);
        assertEquals(expected, actual);
    }

}