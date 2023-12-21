package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class CubeConundrumTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/CubeConundrum_example", 8), //
                Arguments.of("2023/CubeConundrum", 2061));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final CubeConundrum cubeConundrum = new CubeConundrum(input);
        final int actual = cubeConundrum.sumIdsOfPossibleGames();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/CubeConundrum_example", 2286), //
                Arguments.of("2023/CubeConundrum", 72596));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final CubeConundrum cubeConundrum = new CubeConundrum(input);
        final int actual = cubeConundrum.sumThePowerOfGames();
        assertEquals(expected, actual);
    }

}