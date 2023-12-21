package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class CosmicExpansionTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/CosmicExpansion_example", 374), //
                Arguments.of("2023/CosmicExpansion", 9_805_264));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CosmicExpansion cosmicExpansion = new CosmicExpansion(input);
        final long actual = cosmicExpansion.sumOfDistances();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/CosmicExpansion_example", 10, 1030), //
                Arguments.of("2023/CosmicExpansion_example", 100, 8410), //
                Arguments.of("2023/CosmicExpansion", 1_000_000, 779_032_247_216L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int age, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CosmicExpansion cosmicExpansion = new CosmicExpansion(input);
        final long actual = cosmicExpansion.sumOfDistancesWithUniverseAge(age);
        assertEquals(expected, actual);
    }

}