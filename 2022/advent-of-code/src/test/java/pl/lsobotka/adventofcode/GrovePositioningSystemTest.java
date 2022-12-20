package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class GrovePositioningSystemTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("GrovePositioningSystem_simple", 3), //
                Arguments.of("GrovePositioningSystem", 8028));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<Integer> input = getFileInputAsInteger(fileName);
        final GrovePositioningSystem system = new GrovePositioningSystem(input);
        final long actual = system.sumOfCoordinates();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("GrovePositioningSystem_simple", 1623178306L), //
                Arguments.of("GrovePositioningSystem", 8798438007673L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) throws Exception {
        final List<Integer> input = getFileInputAsInteger(fileName);
        final GrovePositioningSystem system = new GrovePositioningSystem(input);
        final long actual = system.sumOfCoordinatesWithDecryptionKey();
        assertEquals(expected, actual);
    }

}