package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SyntaxScoringTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/SyntaxScoring_example", 26397), Arguments.of("2021/SyntaxScoring", 344193));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);

        final SyntaxScoring syntaxScoring = new SyntaxScoring(input);
        final long actual = syntaxScoring.getScoreOfIllegalCharacters();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceCompletedScoreFile() {
        return Stream.of(Arguments.of("2021/SyntaxScoring_example", 288957),
                Arguments.of("2021/SyntaxScoring", 3241238967L));
    }

    @ParameterizedTest
    @MethodSource("testResourceCompletedScoreFile")
    void testResourceCompletedScoreFileTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);

        final SyntaxScoring syntaxScoring = new SyntaxScoring(input);
        final long actual = syntaxScoring.getScoreOfCompleteCharacters();
        assertEquals(expected, actual);
    }
}
