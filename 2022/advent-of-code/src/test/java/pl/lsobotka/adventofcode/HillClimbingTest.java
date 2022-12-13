package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillClimbingTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("HillClimbing_simple", 31), //
                Arguments.of("HillClimbing", 504));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final HillClimbing hill = new HillClimbing(input);
        final int actual = hill.getBestStepsToTopFromStartPoint();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("HillClimbing_simple", 29), //
                Arguments.of("HillClimbing", 500));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final HillClimbing hill = new HillClimbing(input);
        final int actual = hill.getBestStepsToTopFromPossibleStartingPoints();
        assertEquals(expected, actual);
    }
}
