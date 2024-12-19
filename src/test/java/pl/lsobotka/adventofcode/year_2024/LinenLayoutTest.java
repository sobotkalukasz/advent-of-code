package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class LinenLayoutTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/LinenLayout_example", 6), //
                Arguments.of("2024/LinenLayout", 240));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int expected) {
        final List<String> lines = getFileInput(fileName);
        final LinenLayout linenLayout = new LinenLayout(lines);
        final long actual = linenLayout.countPossiblePatterns();
        assertEquals(expected, actual);
    }

}