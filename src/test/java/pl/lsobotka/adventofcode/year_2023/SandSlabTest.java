package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class SandSlabTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/SandSlab_example", 5), //
                Arguments.of("2023/SandSlab", 418));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SandSlab sandSlab = new SandSlab(input);
        final long actual = sandSlab.countBricksPossibleRemove();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/SandSlab_example", 7), //
                Arguments.of("2023/SandSlab", 70702));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final SandSlab sandSlab = new SandSlab(input);
        final long actual = sandSlab.countBricksThatWouldFall();
        assertEquals(expected, actual);
    }
}