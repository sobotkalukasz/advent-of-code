package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class RaceConditionTest extends BaseTest {

    private static Stream<Arguments> stars() {
        return Stream.of(Arguments.of("2024/RaceCondition_example", 2, 64, 1), //
                Arguments.of("2024/RaceCondition", 2, 100, 1499), //
                Arguments.of("2024/RaceCondition_example", 20, 50, 285), //
                Arguments.of("2024/RaceCondition", 20, 100, 1027164));
    }

    @ParameterizedTest
    @MethodSource("stars")
    void starsTest(final String fileName, final int cheatTime, final int saveAtLeast, final int expected) {
        final List<String> lines = getFileInput(fileName);
        final RaceCondition raceCondition = new RaceCondition(lines);
        final long actual = raceCondition.countShortcutsOver(cheatTime, saveAtLeast);
        assertEquals(expected, actual);
    }
}