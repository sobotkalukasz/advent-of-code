package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class MullItOverTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/MullItOver_example", 161), //
                Arguments.of("2024/MullItOver", 175015740));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MullItOver mullItOver = new MullItOver(input);
        final long actual = mullItOver.resultOfInstructions();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/MullItOver_example_part2", 48), //
                Arguments.of("2024/MullItOver", 112272912));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MullItOver mullItOver = new MullItOver(input);
        final long actual = mullItOver.resultOfDoDontInstructions();
        assertEquals(expected, actual);
    }

}