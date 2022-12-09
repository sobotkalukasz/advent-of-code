package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RopeBridgeTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("RopeBridge_simple", 13), //
                Arguments.of("RopeBridge", 6354));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RopeBridge ropeBridge = new RopeBridge(input);
        final int actual = ropeBridge.countUniqueTailPositions();
        assertEquals(expected, actual);
    }
}
