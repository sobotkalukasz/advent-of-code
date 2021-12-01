package pl.lsobotka.adventofcode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SonarSweepTest extends BaseTest {

    private static Stream<Arguments> starOneFile() {
        return Stream.of(
                Arguments.of("SonarSweep", 1581)
        );
    }

    @ParameterizedTest
    @MethodSource("starOneFile")
    public void starOneFileTest(final String fileName, final int expected) throws Exception {
        final List<Integer> input = getFileInput(fileName).stream().map(Integer::valueOf).collect(Collectors.toList());

        final SonarSweep sonarSweep = new SonarSweep();
        final int actual = sonarSweep.depthMeasurement(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> starTwoFile() {
        return Stream.of(
                Arguments.of("SonarSweep", 3, 1618)
        );
    }

    @ParameterizedTest
    @MethodSource("starTwoFile")
    public void starTwoFileTest(final String fileName, final int measurementsToSum, final int expected) throws Exception {
        final List<Integer> input = getFileInput(fileName).stream().map(Integer::valueOf).collect(Collectors.toList());

        final SonarSweep sonarSweep = new SonarSweep();
        final int actual = sonarSweep.depthMeasurementSlidingWindow(input, measurementsToSum);
        assertEquals(expected, actual);
    }

    @Test
    public void starTwoTest() {
        final List<Integer> input = Arrays.asList(199, 200, 208, 210, 200, 207, 240, 269, 260, 263);

        final SonarSweep sonarSweep = new SonarSweep();
        final int actual = sonarSweep.depthMeasurementSlidingWindow(input, 3);
        assertEquals(5, actual);
    }
}
