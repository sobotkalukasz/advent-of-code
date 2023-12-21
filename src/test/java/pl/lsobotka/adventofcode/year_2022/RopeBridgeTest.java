package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RopeBridgeTest extends BaseTest {

    private static Stream<Arguments> cases() {
        return Stream.of(Arguments.of("2022/RopeBridge_simple", 0, 13), //
                Arguments.of("2022/RopeBridge_simple_2", 10, 36), //
                Arguments.of("2022/RopeBridge", 0, 6354), //
                Arguments.of("2022/RopeBridge", 10, 2651)); //
    }

    @ParameterizedTest
    @MethodSource("cases")
    public void caseTest(final String fileName, final int ropeSize, final int expected) {
        final List<String> input = getFileInput(fileName);
        final RopeBridge ropeBridge = new RopeBridge(input);
        final int actual = ropeBridge.countUniqueTailPositionsOf(ropeSize);
        assertEquals(expected, actual);
    }
}
