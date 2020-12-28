package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandheldHaltingTest extends BaseTest {

    private static Stream<Arguments> instructions() {
        return Stream.of(
                Arguments.of("HandheldHalting", 1217)
        );
    }

    @ParameterizedTest
    @MethodSource("instructions")
    public void executeInstructionsTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        HandheldHalting handheldHalting = new HandheldHalting();
        List<HandheldHalting.Instruction> instructions = handheldHalting.createInstructions(input);
        int actual = handheldHalting.executeProgram(instructions);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> fixProgram() {
        return Stream.of(
                Arguments.of("HandheldHalting", 501)
        );
    }

    @ParameterizedTest
    @MethodSource("fixProgram")
    public void fixProgramTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        HandheldHalting handheldHalting = new HandheldHalting();
        List<HandheldHalting.Instruction> instructions = handheldHalting.createInstructions(input);
        int actual = handheldHalting.fixProgram(instructions);

        assertEquals(expected, actual);
    }
}
