package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HydrothermalVentureTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("HydrothermalVenture_example", 5), Arguments.of("HydrothermalVenture", 5124));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void starOneFileTest(final String fileName, final int expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final HydrothermalVenture venture = new HydrothermalVenture(inputRows);
        final long actual = venture.countOverlapsLines();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceStarTwoFile() {
        return Stream.of(Arguments.of("HydrothermalVenture_example", 12), Arguments.of("HydrothermalVenture", 19771));
    }

    @ParameterizedTest
    @MethodSource("testResourceStarTwoFile")
    public void starTwoFileTest(final String fileName, final int expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final HydrothermalVenture venture = new HydrothermalVenture(inputRows);
        final long actual = venture.countOverlapsAll();
        assertEquals(expected, actual);
    }
}
