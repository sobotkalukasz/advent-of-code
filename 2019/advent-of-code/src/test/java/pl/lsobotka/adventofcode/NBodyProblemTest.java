package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBodyProblemTest {

    private static Stream<Arguments> totalEnergyTestResource() {
        return Stream.of(Arguments.of(
                Arrays.asList(List.of(-1, 0, 2), List.of(2, -10, -7), List.of(4, -8, 8), List.of(3, 5, -1)), 10, 179L),
                Arguments.of(
                        Arrays.asList(List.of(-8, -10, 0), List.of(5, 5, 10), List.of(2, -7, 3), List.of(9, -8, -3)),
                        100, 1940L), Arguments.of(
                        Arrays.asList(List.of(16, -11, 2), List.of(0, -4, 7), List.of(6, 4, -10), List.of(-3, -2, -4)),
                        1000, 10055));
    }

    @ParameterizedTest
    @MethodSource("totalEnergyTestResource")
    public void getTotalEnergy(final List<List<Integer>> input, final int steps, final long expected) {
        final NBodyProblem nBodyProblem = new NBodyProblem(input);
        final long actual = nBodyProblem.getMoonEnergyAfterSteps(steps);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> repeatTestResource() {
        return Stream.of(Arguments.of(
                Arrays.asList(List.of(-1, 0, 2), List.of(2, -10, -7), List.of(4, -8, 8), List.of(3, 5, -1)), 2772L),
                Arguments.of(
                        Arrays.asList(List.of(-8, -10, 0), List.of(5, 5, 10), List.of(2, -7, 3), List.of(9, -8, -3)),
                        4686774924L), Arguments.of(
                        Arrays.asList(List.of(16, -11, 2), List.of(0, -4, 7), List.of(6, 4, -10), List.of(-3, -2, -4)),
                        374307970285176L));
    }

    @ParameterizedTest
    @MethodSource("repeatTestResource")
    public void simulateUniverse(final List<List<Integer>> input, final long expected) {
        final NBodyProblem nBodyProblem = new NBodyProblem(input);
        final long actual = nBodyProblem.countDaysToRepeat();
        assertEquals(expected, actual);
    }
}
