package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeaCucumberTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("SeaCucumber_example", 58), Arguments.of("SeaCucumber", 435));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final SeaCucumber seaCucumber = new SeaCucumber(input);
        final int actual = seaCucumber.countMoves();

        assertEquals(expected, actual);
    }
}
