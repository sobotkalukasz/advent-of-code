package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrabCombatTest extends BaseTest {

    private static Stream<Arguments> combatFile() {
        return Stream.of(
                Arguments.of("CrabCombat_example", 306),
                Arguments.of("CrabCombat", 33434)
        );
    }

    @ParameterizedTest
    @MethodSource("combatFile")
    public void combatFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getWinnerScore();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> combatRecursiveFile() {
        return Stream.of(
                Arguments.of("CrabCombat_example", 291),
                Arguments.of("CrabCombat", 31657)
        );
    }

    @ParameterizedTest
    @MethodSource("combatRecursiveFile")
    public void combatRecursiveFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getRecursiveWinnerScore();
        assertEquals(expected, actual);
    }
}
