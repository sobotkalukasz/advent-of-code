package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class BoilingBouldersTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/BoilingBoulders_simple", 64), //
                Arguments.of("2022/BoilingBoulders", 4288));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected){
        final List<String> input = getFileInput(fileName);
        final BoilingBoulders boulders = new BoilingBoulders(input);
        final int actual = boulders.surfaceArea();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/BoilingBoulders_simple", 58), //
                Arguments.of("2022/BoilingBoulders", 2494));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final BoilingBoulders boulders = new BoilingBoulders(input);
        final int actual = boulders.outsideSurfaceArea();
        assertEquals(expected, actual);
    }

}