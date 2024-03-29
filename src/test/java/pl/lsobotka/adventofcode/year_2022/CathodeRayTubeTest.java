package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CathodeRayTubeTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/CathodeRayTube_simple", 13_140), //
                Arguments.of("2022/CathodeRayTube", 15_680));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CathodeRayTube cathodeRayTube = new CathodeRayTube(input);
        final long actual = cathodeRayTube.determineSignalStrength();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/CathodeRayTube_simple", ""), //
                Arguments.of("2022/CathodeRayTube", "ZFBFHGUP"));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName) {
        final List<String> input = getFileInput(fileName);
        final CathodeRayTube cathodeRayTube = new CathodeRayTube(input);
        System.out.println(cathodeRayTube.printScreen());
    }
}
