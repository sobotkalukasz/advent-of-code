package pl.lsobotka.adventofcode.year_2021.passagepathing;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PassagePathingTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/PassagePathing_example_01", 10), Arguments.of(
                        "2021/PassagePathing_example_02", 19),
                Arguments.of("2021/PassagePathing_example_03", 226), Arguments.of("2021/PassagePathing", 5457));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final PassagePathing passagePathing = new PassagePathing(input);
        final long actual = passagePathing.countAllPathSingleVisit();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceStarTwoFile() {
        return Stream.of(Arguments.of("2021/PassagePathing_example_01", 36), Arguments.of(
                        "2021/PassagePathing_example_02", 103),
                Arguments.of("2021/PassagePathing_example_03", 3509), Arguments.of("2021/PassagePathing", 128506));
    }

    @ParameterizedTest
    @MethodSource("testResourceStarTwoFile")
    void testResourceStarTwoFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final PassagePathing passagePathing = new PassagePathing(input);
        final long actual = passagePathing.countAllPathsVisitFirstSmallTwice();
        assertEquals(expected, actual);
    }
}
