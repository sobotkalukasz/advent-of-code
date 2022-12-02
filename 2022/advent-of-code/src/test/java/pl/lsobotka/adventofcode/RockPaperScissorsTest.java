package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class RockPaperScissorsTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of(Arrays.asList("A Y", "B X", "C Z"), 15));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void simpleFirstStarExampleTest(final List<String> input, final long expected) {
        final RockPaperScissors rockPaperScissors = new RockPaperScissors(input);
        final long actual = rockPaperScissors.pointsInRoundsByMove();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleSecondStarExample() {
        return Stream.of(Arguments.of(Arrays.asList("A Y", "B X", "C Z"), 12));
    }

    @ParameterizedTest
    @MethodSource("simpleSecondStarExample")
    public void simpleSecondStarExampleTest(final List<String> input, final long expected) {
        final RockPaperScissors rockPaperScissors = new RockPaperScissors(input);
        final long actual = rockPaperScissors.pointsInRoundsByOutcome();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("RockPaperScissors", 14375));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RockPaperScissors rockPaperScissors = new RockPaperScissors(input);
        final long actual = rockPaperScissors.pointsInRoundsByMove();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStarFile() {
        return Stream.of(Arguments.of("RockPaperScissors", 10274));
    }

    @ParameterizedTest
    @MethodSource("secondStarFile")
    public void secondStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RockPaperScissors rockPaperScissors = new RockPaperScissors(input);
        final long actual = rockPaperScissors.pointsInRoundsByOutcome();
        assertEquals(expected, actual);
    }

}