package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HydrothermalVentureTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/HydrothermalVenture_example", 5),
                Arguments.of("2021/HydrothermalVenture", 5124));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void starOneFileTest(final String fileName, final int expected) {
        final List<String> inputRows = getFileInput(fileName);

        final HydrothermalVenture venture = new HydrothermalVenture(inputRows);
        final long actual = venture.countOverlapsLines();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceStarTwoFile() {
        return Stream.of(Arguments.of("2021/HydrothermalVenture_example", 12),
                Arguments.of("2021/HydrothermalVenture", 19771));
    }

    @ParameterizedTest
    @MethodSource("testResourceStarTwoFile")
    void starTwoFileTest(final String fileName, final int expected) {
        final List<String> inputRows = getFileInput(fileName);

        final HydrothermalVenture venture = new HydrothermalVenture(inputRows);
        final long actual = venture.countOverlapsAll();
        assertEquals(expected, actual);
    }
}
