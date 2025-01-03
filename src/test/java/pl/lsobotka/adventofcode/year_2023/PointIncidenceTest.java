package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class PointIncidenceTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/PointIncidence_example", 405), //
                Arguments.of("2023/PointIncidence", 34100));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PointIncidence pointIncidence = new PointIncidence(input);
        final long actual = pointIncidence.findReflections();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/PointIncidence_example", 400), //
                Arguments.of("2023/PointIncidence", 33106));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PointIncidence pointIncidence = new PointIncidence(input);
        final long actual = pointIncidence.findReflectionsWithSmudge();
        assertEquals(expected, actual);
    }

}