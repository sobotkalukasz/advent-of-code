package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class SeedFertilizerTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("SeedFertilizer_example", 35), //
                Arguments.of("SeedFertilizer", 157_211_394));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SeedFertilizer seedFertilizer = new SeedFertilizer(input);
        final long actual = seedFertilizer.findLowestLocation();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("SeedFertilizer_example", 46), //
                Arguments.of("SeedFertilizer", 50_855_035));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SeedFertilizer seedFertilizer = new SeedFertilizer(input);
        final long actual = seedFertilizer.findLowestLocationForSeedRange();
        assertEquals(expected, actual);
    }

}