package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiveTest extends BaseTest {

    private static Stream<Arguments> starOneFile() {
        return Stream.of(
                Arguments.of("Dive", 2039912)
        );
    }

    @ParameterizedTest
    @MethodSource("starOneFile")
    public void starOneFileTest(final String fileName, final int expected) throws Exception {
        final List<String> instructions = getFileInput(fileName);

        final Dive dive = new Dive();
        final int actual = dive.determinePositionAndMultiplyCoords(instructions);
        assertEquals(expected, actual);
    }
}
