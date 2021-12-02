package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiveTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("Dive", false, 2039912), Arguments.of("Dive", true, 1942068080));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void starOneFileTest(final String fileName, final boolean withAim, final int expected) throws Exception {
        final List<String> instructions = getFileInput(fileName);

        final Dive dive = new Dive();
        final int actual = dive.determinePositionAndMultiplyCoords(instructions, withAim);
        assertEquals(expected, actual);
    }
}
