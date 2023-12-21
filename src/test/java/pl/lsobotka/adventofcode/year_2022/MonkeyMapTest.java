package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class MonkeyMapTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/MonkeyMap_simple", 6032), //
                Arguments.of("2022/MonkeyMap", 11464));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MonkeyMap monkeyMap = new MonkeyMap(input);
        final long actual = monkeyMap.determinePasswordOnUnfoldedMap();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/MonkeyMap_simple", 5031), //
                Arguments.of("2022/MonkeyMap", 197_122)
        );
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MonkeyMap monkeyMap = new MonkeyMap(input);
        final long actual = monkeyMap.determinePasswordOnFoldedMap();
        assertEquals(expected, actual);
    }

}