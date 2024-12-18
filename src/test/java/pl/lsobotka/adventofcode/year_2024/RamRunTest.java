package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class RamRunTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/RamRun_example", 6, 12, 22), //
                Arguments.of("2024/RamRun", 70, 1024, 260));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int size, final int time, final int expected) {
        final List<String> lines = getFileInput(fileName);
        final RamRun ramRun = new RamRun(lines, size);
        final int actual = ramRun.countStepsAfter(time);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/RamRun_example", 6, "6,1"), //
                Arguments.of("2024/RamRun", 70, "24,48"));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final int size, final String expected) {
        final List<String> lines = getFileInput(fileName);
        final RamRun ramRun = new RamRun(lines, size);
        final String actual = ramRun.determineWhenNotPossible();
        assertEquals(expected, actual);
    }
}