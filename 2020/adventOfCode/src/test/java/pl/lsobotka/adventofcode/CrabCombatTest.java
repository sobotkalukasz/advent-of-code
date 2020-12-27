package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrabCombatTest {

    private static Stream<Arguments> combatFile() {
        return Stream.of(
                Arguments.of("src/test/resources/CrabCombat_example", 306),
                Arguments.of("src/test/resources/CrabCombat", 33434)
        );
    }

    @ParameterizedTest
    @MethodSource("combatFile")
    public void combatFileTest(String path, int expected) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getWinnerScore();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> combatRecursiveFile() {
        return Stream.of(
                Arguments.of("src/test/resources/CrabCombat_example", 291),
                Arguments.of("src/test/resources/CrabCombat", 31657)
        );
    }

    @ParameterizedTest
    @MethodSource("combatRecursiveFile")
    public void combatRecursiveFileTest(String path, int expected) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        CrabCombat combat = new CrabCombat(input);
        int actual = combat.getRecursiveWinnerScore();
        assertEquals(expected, actual);
    }
}
