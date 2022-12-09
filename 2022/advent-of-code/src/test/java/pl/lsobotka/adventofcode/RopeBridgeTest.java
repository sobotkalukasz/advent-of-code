package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RopeBridgeTest extends BaseTest {

    private static Stream<Arguments> cases() {
        return Stream.of(Arguments.of("RopeBridge_simple", 0, 13), //
                Arguments.of("RopeBridge_simple_2", 10, 36), //
                Arguments.of("RopeBridge", 0, 6354), //
                Arguments.of("RopeBridge", 10, 2651)); //
    }

    @ParameterizedTest
    @MethodSource("cases")
    public void caseTest(final String fileName, final int ropeSize, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RopeBridge ropeBridge = new RopeBridge(input);
        final int actual = ropeBridge.countUniqueTailPositionsOf(ropeSize);
        assertEquals(expected, actual);
    }
}
