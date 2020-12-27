package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandheldHaltingTest {

    private static Stream<Arguments> instructions(){
        return Stream.of(
                Arguments.of("src/test/resources/HandheldHalting", 1217)
        );
    }

    @ParameterizedTest
    @MethodSource("instructions")
    public void executeInstructionsTest(String path, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        HandheldHalting handheldHalting = new HandheldHalting();
        List<HandheldHalting.Instruction> instructions = handheldHalting.createInstructions(rawData);
        int actual = handheldHalting.executeProgram(instructions);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> fixProgram(){
        return Stream.of(
                Arguments.of("src/test/resources/HandheldHalting", 501)
        );
    }

    @ParameterizedTest
    @MethodSource("fixProgram")
    public void fixProgramTest(String path, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        HandheldHalting handheldHalting = new HandheldHalting();
        List<HandheldHalting.Instruction> instructions = handheldHalting.createInstructions(rawData);
        int actual = handheldHalting.fixProgram(instructions);

        assertEquals(expected, actual);
    }
}
