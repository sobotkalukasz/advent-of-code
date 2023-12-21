package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HillClimbingTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/HillClimbing_simple", 31), //
                Arguments.of("2022/HillClimbing", 504));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final HillClimbing hill = new HillClimbing(input);
        final int actual = hill.getBestStepsToTopFromStartPoint();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/HillClimbing_simple", 29), //
                Arguments.of("2022/HillClimbing", 500));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final HillClimbing hill = new HillClimbing(input);
        final int actual = hill.getBestStepsToTopFromPossibleStartingPoints();
        assertEquals(expected, actual);
    }
}
