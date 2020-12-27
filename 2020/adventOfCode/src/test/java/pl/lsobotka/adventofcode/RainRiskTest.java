package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RainRiskTest {

    private static Stream<Arguments> simpleCommands(){
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

    private static Stream<Arguments> simpleCommandsFile(){
        return Stream.of(
                Arguments.of("src/test/resources/RainRisk", 757)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleCommandsFile")
    public void simpleCommandsFileTest(String path, int expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> commands = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        RainRisk rain = new RainRisk();
        long actual = rain.executeSimpleCommands(commands);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> wayPointCommands(){
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

    private static Stream<Arguments> wayPointCommandsFile(){
        return Stream.of(
                Arguments.of("src/test/resources/RainRisk", 51249)
        );
    }

    @ParameterizedTest
    @MethodSource("wayPointCommandsFile")
    public void wayPointCommandsFileTest(String path, int expected) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> commands = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        RainRisk rain = new RainRisk();
        long actual = rain.executeWayPointCommands(commands);
        assertEquals(expected, actual);
    }
}
