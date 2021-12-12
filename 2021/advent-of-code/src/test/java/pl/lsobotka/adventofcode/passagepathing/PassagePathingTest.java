package pl.lsobotka.adventofcode.passagepathing;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassagePathingTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("PassagePathing_example_01", 10), Arguments.of("PassagePathing_example_02", 19),
                Arguments.of("PassagePathing_example_03", 226), Arguments.of("PassagePathing", 5457));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final PassagePathing passagePathing = new PassagePathing(input);
        final long actual = passagePathing.countAllPathSingleVisit();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceStarTwoFile() {
        return Stream.of(Arguments.of("PassagePathing_example_01", 36), Arguments.of("PassagePathing_example_02", 103),
                Arguments.of("PassagePathing_example_03", 3509), Arguments.of("PassagePathing", 128506));
    }

    @ParameterizedTest
    @MethodSource("testResourceStarTwoFile")
    public void testResourceStarTwoFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final PassagePathing passagePathing = new PassagePathing(input);
        final long actual = passagePathing.countAllPathsVisitFirstSmallTwice();
        assertEquals(expected, actual);
    }
}
