package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class TrebuchetTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/Trebuchet_example", 142), //
                Arguments.of("2023/Trebuchet", 54081));
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
        return Stream.of(Arguments.of("2023/Trebuchet_second_example", 281), //
                Arguments.of("2023/Trebuchet", 54649));
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