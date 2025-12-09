package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieTheaterTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/MovieTheater_example", 50), //
                Arguments.of("2025/MovieTheater", 4771532800L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MovieTheater movieTheater = new MovieTheater(input);
        final long actual = movieTheater.largestArea();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/MovieTheater_example", 24), //
                Arguments.of("2025/MovieTheater", 1544362560L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MovieTheater movieTheater = new MovieTheater(input);
        final long actual = movieTheater.largesAreaInside();
        assertEquals(expected, actual);
    }

}