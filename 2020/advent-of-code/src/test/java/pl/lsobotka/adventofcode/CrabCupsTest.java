package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrabCupsTest {

    private static Stream<Arguments> cups() {
        return Stream.of(
                Arguments.of("389125467", 10, "92658374"),
                Arguments.of("389125467", 100, "67384529"),
                Arguments.of("398254716", 100, "45798623")
        );
    }

    @ParameterizedTest
    @MethodSource("cups")
    public void cupsTest(String input, int moves, String expected) {

        CrabCups crabCups = new CrabCups(input);
        String actual = crabCups.playSimpleGame(moves);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cupsAdvanced() {
        return Stream.of(
                Arguments.of("389125467", 1_000_000, 10_000_000, 149245887792L),
                Arguments.of("398254716", 1_000_000, 10_000_000, 235551949822L)
        );
    }

    @ParameterizedTest
    @MethodSource("cupsAdvanced")
    public void cupsAdvancedTest(String input, int size, int moves, long expected) {

        CrabCups crabCups = new CrabCups(input, size);
        long actual = crabCups.playAdvancedGame(moves);
        assertEquals(expected, actual);
    }
}
