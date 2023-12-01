package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class TrebuchetTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("Trebuchet_example", 142), //
                Arguments.of("Trebuchet", 54081));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final Trebuchet trebuchet = new Trebuchet(input);
        final int actual = trebuchet.sumOfCalibrationValues();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("Trebuchet_second_example", 281), //
                Arguments.of("Trebuchet", 54649));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final Trebuchet trebuchet = new Trebuchet(input);
        final int actual = trebuchet.sumOfComplexCalibrationValues();
        assertEquals(expected, actual);
    }

}