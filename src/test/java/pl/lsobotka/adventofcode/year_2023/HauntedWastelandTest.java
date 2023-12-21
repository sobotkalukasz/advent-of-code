package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class HauntedWastelandTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/HauntedWasteland_example", 2), //
                Arguments.of("2023/HauntedWasteland_example2", 6), //
                Arguments.of("2023/HauntedWasteland", 21251));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HauntedWasteland hauntedWasteland = new HauntedWasteland(input);
        final long actual = hauntedWasteland.countIterations();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/HauntedWasteland_example3", 6), //
                Arguments.of("2023/HauntedWasteland", 11678319315857L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final HauntedWasteland hauntedWasteland = new HauntedWasteland(input);
        final long actual = hauntedWasteland.countSimultaneousIterations();
        assertEquals(expected, actual);
    }

}