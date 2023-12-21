package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeaCucumberTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/SeaCucumber_example", 58), //
                Arguments.of("2021/SeaCucumber", 435));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);

        final SeaCucumber seaCucumber = new SeaCucumber(input);
        final int actual = seaCucumber.countMoves();

        assertEquals(expected, actual);
    }
}
