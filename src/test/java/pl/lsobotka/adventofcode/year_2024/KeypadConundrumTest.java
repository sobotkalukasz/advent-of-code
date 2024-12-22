package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class KeypadConundrumTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of( //
                Arguments.of("2024/KeypadConundrum_example", 3, 126_384), //
                Arguments.of("2024/KeypadConundrum_example_A", 1, 348), //
                Arguments.of("2024/KeypadConundrum_example_A", 2, 812), //
                Arguments.of("2024/KeypadConundrum_example_A", 3, 1_972), //
                Arguments.of("2024/KeypadConundrum_example_B", 3, 58_800), //
                Arguments.of("2024/KeypadConundrum_example_C", 3, 12_172), //
                Arguments.of("2024/KeypadConundrum_example_D", 3, 29_184), //
                Arguments.of("2024/KeypadConundrum_example_E", 3, 24_256), //
                Arguments.of("2024/KeypadConundrum", 3, 162_740),
                Arguments.of("2024/KeypadConundrum", 26, -1));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, int controlQty, final int expected) {
        final List<String> lines = getFileInput(fileName);
        final KeypadConundrum keypadConundrum = new KeypadConundrum(lines, controlQty);
        final long actual = keypadConundrum.determineComplexity();
        assertEquals(expected, actual);
    }

}