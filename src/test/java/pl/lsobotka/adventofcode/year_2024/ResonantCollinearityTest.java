package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResonantCollinearityTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/ResonantCollinearity_example", 14), //
                Arguments.of("2024/ResonantCollinearity", 222));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ResonantCollinearity resonantCollinearity = new ResonantCollinearity(input);
        final long actual = resonantCollinearity.antinodeLocations();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/ResonantCollinearity_example", 34), //
                Arguments.of("2024/ResonantCollinearity", 884));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ResonantCollinearity resonantCollinearity = new ResonantCollinearity(input);
        final long actual = resonantCollinearity.resonantAntinodeLocations();
        assertEquals(expected, actual);
    }
}
