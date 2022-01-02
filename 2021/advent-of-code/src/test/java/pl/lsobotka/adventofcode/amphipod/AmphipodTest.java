package pl.lsobotka.adventofcode.amphipod;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmphipodTest {

    private static Stream<Arguments> isCompleted() {
        return Stream.of(
                Arguments.of(List.of("#############", "#...........#", "###B#C#B#D###", "  #A#D#C#A#", "  #########"),
                        false),
                Arguments.of(List.of("#############", "#...........#", "###A#B#C#D###", "  #A#B#C#D#", "  #########"),
                        true));
    }

    @ParameterizedTest
    @MethodSource("isCompleted")
    public void isCompletedTest(final List<String> input, final boolean expected) {
        final Board board = new Board(input);
        final boolean actual = board.isCompleted();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResource() {
        return Stream.of(
                Arguments.of(List.of("#############", "#...........#", "###B#C#B#D###", "  #A#D#C#A#", "  #########"),
                        12521),
                Arguments.of(List.of("#############", "#...........#", "###B#B#C#D###", "  #D#C#A#A#", "  #########"),
                        15111), Arguments.of(
                        List.of("#############", "#...........#", "###B#C#B#D###", "  #D#C#B#A#", "  #D#B#A#C#",
                                "  #A#D#C#A#", "  #########"), 44169), Arguments.of(
                        List.of("#############", "#...........#", "###B#B#C#D###", "  #D#C#B#A#", "  #D#B#A#C#",
                                "  #D#C#A#A#", "  #########"), 47625));
    }

    @ParameterizedTest
    @MethodSource("testResource")
    public void optimalPath(final List<String> input, final long expected) {
        final Amphipod amphipod = new Amphipod(input);
        final long actual = amphipod.findOptimalScore();

        assertEquals(expected, actual);
    }

}
