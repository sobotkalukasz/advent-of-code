package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretEntranceTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/SecretEntrance_example", 3), //
                Arguments.of("2025/SecretEntrance", 962));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SecretEntrance entrance = new SecretEntrance(input);
        final long actual = entrance.countStopAt();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/SecretEntrance_example", 6), //
                Arguments.of("2025/SecretEntrance", 5782));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SecretEntrance entrance = new SecretEntrance(input);
        final long actual = entrance.countPointingAt();
        assertEquals(expected, actual);
    }

}