package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class CrossedWiresTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/CrossedWires_example", 2024), //
                Arguments.of("2024/CrossedWires", 55_114_892_239_566L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final CrossedWires crossedWires = new CrossedWires(lines);
        final long actual = crossedWires.determineNumber();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/CrossedWires", "cdj,dhm,gfm,mrb,qjd,z08,z16,z32"));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final String expected) {
        final List<String> lines = getFileInput(fileName);
        final CrossedWires crossedWires = new CrossedWires(lines);
        final String actual = crossedWires.swapWires();
        assertEquals(expected, actual);
    }

}