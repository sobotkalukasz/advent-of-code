package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class NeverOddsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/NeverOdds_example", 7, 27, 2), //
                Arguments.of("2023/NeverOdds", 200_000_000_000_000L, 400_000_000_000_000L, 25_810));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long from, final long to, final long expected) {
        final List<String> input = getFileInput(fileName);
        final NeverOdds neverOdds = new NeverOdds(input);
        final long actual = neverOdds.intersectXYatRange(from, to);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/NeverOdds_example", 47), //
                Arguments.of("2023/NeverOdds", 652_666_650_475_950L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final NeverOdds neverOdds = new NeverOdds(input);
        final long actual = neverOdds.intersectXYZ();
        assertEquals(expected, actual);
    }

}