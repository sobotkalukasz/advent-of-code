package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyntaxScoringTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("SyntaxScoring_example", 26397), Arguments.of("SyntaxScoring", 344193));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final SyntaxScoring syntaxScoring = new SyntaxScoring(input);
        final long actual = syntaxScoring.getScoreOfIllegalCharacters();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceCompletedScoreFile() {
        return Stream.of(Arguments.of("SyntaxScoring_example", 288957), Arguments.of("SyntaxScoring", 3241238967L));
    }

    @ParameterizedTest
    @MethodSource("testResourceCompletedScoreFile")
    public void testResourceCompletedScoreFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final SyntaxScoring syntaxScoring = new SyntaxScoring(input);
        final long actual = syntaxScoring.getScoreOfCompleteCharacters();
        assertEquals(expected, actual);
    }
}
