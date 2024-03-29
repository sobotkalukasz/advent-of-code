package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class MonkeyMathTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/MonkeyMath_simple", 152), //
                Arguments.of("2022/MonkeyMath", 66_174_565_793_494L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MonkeyMath monkeyMath = new MonkeyMath(input);
        final long actual = monkeyMath.yellNumber();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/MonkeyMath_simple", 301), //
                Arguments.of("2022/MonkeyMath", 3_327_575_724_809L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MonkeyMath monkeyMath = new MonkeyMath(input);
        final long actual = monkeyMath.guessYellNumber();
        assertEquals(expected, actual);
    }

}