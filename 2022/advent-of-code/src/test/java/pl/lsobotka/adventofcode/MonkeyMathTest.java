package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class MonkeyMathTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("MonkeyMath_simple", 152), //
                Arguments.of("MonkeyMath", 66_174_565_793_494L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final MonkeyMath monkeyMath = new MonkeyMath(input);
        final long actual = monkeyMath.yellNumber();
        assertEquals(expected, actual);
    }

}