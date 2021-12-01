package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    public void starOneFileTest(String fileName, int expected) throws Exception {
        List<Integer> input = getFileInput(fileName).stream().map(Integer::valueOf).collect(Collectors.toList());

        SonarSweep sonarSweep = new SonarSweep();
        int actual = sonarSweep.depthMeasurement(input);
        assertEquals(expected, actual);
    }
}
