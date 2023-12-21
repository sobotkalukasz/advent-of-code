package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiveTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/Dive", false, 2039912), Arguments.of("2021/Dive", true, 1942068080));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void starOneFileTest(final String fileName, final boolean withAim, final int expected) {
        final List<String> instructions = getFileInput(fileName);

        final Dive dive = new Dive();
        final int actual = dive.determinePositionAndMultiplyCoords(instructions, withAim);
        assertEquals(expected, actual);
    }
}
