package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class WaitForItTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/WaitForIt_example", 288), //
                Arguments.of("2023/WaitForIt", 3317888));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final WaitForIt waitForIt = WaitForIt.ofRaces(input);
        final long actual = waitForIt.countWaysToBeatDistanceRecord();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/WaitForIt_example", 71503), //
                Arguments.of("2023/WaitForIt", 24655068));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final WaitForIt waitForIt = WaitForIt.ofSingleRace(input);
        final long actual = waitForIt.countWaysToBeatDistanceRecord();
        assertEquals(expected, actual);
    }
}