package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class ParabolicReflectorDishTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/ParabolicReflectorDish_example", 136), //
                Arguments.of("2023/ParabolicReflectorDish", 108_759));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ParabolicReflectorDish parabolicReflectorDish = new ParabolicReflectorDish(input);
        final long actual = parabolicReflectorDish.calculateLoad();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/ParabolicReflectorDish_example", 1_000_000_000, 64), //
                Arguments.of("2023/ParabolicReflectorDish", 1_000_000_000, 89_089));
    }
 
    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long cycle, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ParabolicReflectorDish parabolicReflectorDish = new ParabolicReflectorDish(input);
        final long actual = parabolicReflectorDish.calculateLoadAfterCycles(cycle);
        assertEquals(expected, actual);
    }

}