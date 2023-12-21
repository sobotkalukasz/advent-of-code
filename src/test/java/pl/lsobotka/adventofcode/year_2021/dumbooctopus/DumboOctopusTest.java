package pl.lsobotka.adventofcode.year_2021.dumbooctopus;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DumboOctopusTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/DumboOctopus_example", 100, 1656L), Arguments.of("2021/DumboOctopus", 100, 1793L));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void countFlashAfterStepsTest(final String fileName, final int numberOfSteps, final long expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final DumboOctopus dumboOctopus = new DumboOctopus(inputRows);
        final long actual = dumboOctopus.countFlashesAfterSteps(numberOfSteps);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> countStepsToAllFlashFile() {
        return Stream.of(Arguments.of("2021/DumboOctopus_example", 195L), Arguments.of("2021/DumboOctopus", 247L));
    }

    @ParameterizedTest
    @MethodSource("countStepsToAllFlashFile")
    void countStepsToAllFlashTest(final String fileName, final long expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final DumboOctopus dumboOctopus = new DumboOctopus(inputRows);
        final long actual = dumboOctopus.countStepsToFlashAll();
        assertEquals(expected, actual);
    }

}
