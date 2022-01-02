package pl.lsobotka.adventofcode.dumbooctopus;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DumboOctopusTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("DumboOctopus_example", 100, 1656L), Arguments.of("DumboOctopus", 100, 1793L));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void countFlashAfterStepsTest(final String fileName, final int numberOfSteps, final long expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final DumboOctopus dumboOctopus = new DumboOctopus(inputRows);
        final long actual = dumboOctopus.countFlashesAfterSteps(numberOfSteps);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> countStepsToAllFlashFile() {
        return Stream.of(Arguments.of("DumboOctopus_example", 195L), Arguments.of("DumboOctopus", 247L));
    }

    @ParameterizedTest
    @MethodSource("countStepsToAllFlashFile")
    public void countStepsToAllFlashTest(final String fileName, final long expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final DumboOctopus dumboOctopus = new DumboOctopus(inputRows);
        final long actual = dumboOctopus.countStepsToFlashAll();
        assertEquals(expected, actual);
    }

}
