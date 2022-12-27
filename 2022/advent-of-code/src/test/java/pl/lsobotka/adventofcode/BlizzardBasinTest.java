package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class BlizzardBasinTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("BlizzardBasin_simple", 18), //
                Arguments.of("BlizzardBasin", 314));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final BlizzardBasin basin = new BlizzardBasin(input);
        final int actual = basin.oneWay();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("BlizzardBasin_simple", 54), //
                Arguments.of("BlizzardBasin", 896));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final BlizzardBasin basin = new BlizzardBasin(input);
        final int actual = basin.goOnceAgain();
        assertEquals(expected, actual);
    }

}