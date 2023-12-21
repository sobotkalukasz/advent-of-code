package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class GearRatiosTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/GearRatios_example", 4361), //
                Arguments.of("2023/GearRatios", 517021));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final GearRatios gearRatios = new GearRatios(input);
        final int actual = gearRatios.sumOfEnginePartNumbers();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/GearRatios_example", 467835), //
                Arguments.of("2023/GearRatios", 81296995));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final GearRatios gearRatios = new GearRatios(input);
        final int actual = gearRatios.sumOfGearRatio();
        assertEquals(expected, actual);
    }

}