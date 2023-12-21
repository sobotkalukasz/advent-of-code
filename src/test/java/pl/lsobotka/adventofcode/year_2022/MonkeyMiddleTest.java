package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonkeyMiddleTest extends BaseTest {

    private static Stream<Arguments> testCase() {
        return Stream.of(Arguments.of("2022/MonkeyMiddle_simple", 20, 10_605), //
                Arguments.of("2022/MonkeyMiddle", 20, 110_264), //
                Arguments.of("2022/MonkeyMiddle_simple", 10_000, 2_713_310_158L), //
                Arguments.of("2022/MonkeyMiddle", 10_000, 23_612_457_316L));
    }

    @ParameterizedTest
    @MethodSource("testCase")
    public void caseTest(final String fileName, final int rounds, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MonkeyMiddle monkeyMiddle = new MonkeyMiddle(input);
        final long actual = monkeyMiddle.getMonkeyBusiness(rounds);
        assertEquals(expected, actual);
    }

}
