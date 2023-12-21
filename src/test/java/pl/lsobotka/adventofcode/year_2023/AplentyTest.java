package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class AplentyTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/Aplenty_example", 19114), //
                Arguments.of("2023/Aplenty", 333263));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Aplenty aplenty = new Aplenty(input);
        final long actual = aplenty.sumAcceptedRatings();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/Aplenty_example", 167_409_079_868_000L), //
                Arguments.of("2023/Aplenty", 130_745_440_937_650L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Aplenty aplenty = new Aplenty(input);
        final long actual = aplenty.countAcceptedPermutations();
        assertEquals(expected, actual);
    }

}