package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class CosmicExpansionTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("CosmicExpansion_example", 374), //
                Arguments.of("CosmicExpansion", 9_805_264));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CosmicExpansion cosmicExpansion = new CosmicExpansion(input);
        final long actual = cosmicExpansion.sumOfDistances();
        assertEquals(expected, actual);
    }

}