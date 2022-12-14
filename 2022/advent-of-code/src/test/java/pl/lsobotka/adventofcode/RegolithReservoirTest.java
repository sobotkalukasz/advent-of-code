package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class RegolithReservoirTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("RegolithReservoir_simple", 24), //
                Arguments.of("RegolithReservoir", 768));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RegolithReservoir reservoir = new RegolithReservoir(input);
        final long actual = reservoir.countSandUntilFallOf();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("RegolithReservoir_simple", 93), //
                Arguments.of("RegolithReservoir", 26_686));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final RegolithReservoir reservoir = new RegolithReservoir(input);
        final long actual = reservoir.countSandUntilFull();
        assertEquals(expected, actual);
    }

}