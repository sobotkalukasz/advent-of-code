package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class BoilingBouldersTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("BoilingBoulders_simple", 64), //
                Arguments.of("BoilingBoulders", 4288));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BoilingBoulders boulders = new BoilingBoulders(input);
        final int actual = boulders.surfaceArea();
        assertEquals(expected, actual);
    }

}