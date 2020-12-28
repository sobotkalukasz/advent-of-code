package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RainRiskTest extends BaseTest {

    private static Stream<Arguments> simpleCommands() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("F10", "N3", "F7", "R90", "F11")), 25)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleCommands")
    public void simpleCommandsTest(List<String> commands, int expected) {

        RainRisk rain = new RainRisk();
        long actual = rain.executeSimpleCommands(commands);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleCommandsFile() {
        return Stream.of(
                Arguments.of("RainRisk", 757)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleCommandsFile")
    public void simpleCommandsFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        RainRisk rain = new RainRisk();
        long actual = rain.executeSimpleCommands(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> wayPointCommands() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("F10", "N3", "F7", "R90", "F11")), 286)
        );
    }

    @ParameterizedTest
    @MethodSource("wayPointCommands")
    public void wayPointCommandsTest(List<String> commands, int expected) {
        RainRisk rain = new RainRisk();
        long actual = rain.executeWayPointCommands(commands);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> wayPointCommandsFile() {
        return Stream.of(
                Arguments.of("RainRisk", 51249)
        );
    }

    @ParameterizedTest
    @MethodSource("wayPointCommandsFile")
    public void wayPointCommandsFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        RainRisk rain = new RainRisk();
        long actual = rain.executeWayPointCommands(input);
        assertEquals(expected, actual);
    }
}
