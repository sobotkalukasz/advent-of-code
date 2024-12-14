package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClawContraptionTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/ClawContraption_example", 480), //
                Arguments.of("2024/ClawContraption", 33209));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final String input = String.join(" ", lines);
        final ClawContraption clawContraption = new ClawContraption(input);
        final long actual = clawContraption.tokenCost();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/ClawContraption_example", 875318608908L), //
                Arguments.of("2024/ClawContraption", 83102355665474L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final String input = String.join(" ", lines);
        final ClawContraption clawContraption = new ClawContraption(input);
        final long actual = clawContraption.tokenCostWithoutLimit();
        assertEquals(expected, actual);
    }
}
