package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrabCombatTest extends BaseTest {

    private static Stream<Arguments> combatFile() {
        return Stream.of(Arguments.of("2020/CrabCombat_example", 306), //
                Arguments.of("2020/CrabCombat", 33434));
    }

    @ParameterizedTest
    @MethodSource("combatFile")
    void combatFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getWinnerScore();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> combatRecursiveFile() {
        return Stream.of(Arguments.of("2020/CrabCombat_example", 291), //
                Arguments.of("2020/CrabCombat", 31657));
    }

    @ParameterizedTest
    @MethodSource("combatRecursiveFile")
    void combatRecursiveFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getRecursiveWinnerScore();
        assertEquals(expected, actual);
    }
}
