package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class UnstableDiffusionTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/UnstableDiffusion_simple", 110), //
                Arguments.of("2022/UnstableDiffusion", 4109));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final UnstableDiffusion diffusion = new UnstableDiffusion(input);
        final long actual = diffusion.countEmptySpaceAfterMoves();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/UnstableDiffusion_simple", 20), //
                Arguments.of("2022/UnstableDiffusion", 1055));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final UnstableDiffusion diffusion = new UnstableDiffusion(input);
        final long actual = diffusion.countMovesToNoAdjacent();
        assertEquals(expected, actual);
    }


}