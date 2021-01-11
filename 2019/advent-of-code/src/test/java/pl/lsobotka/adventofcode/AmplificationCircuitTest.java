package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AmplificationCircuitTest extends BaseTest {

    private static Stream<Arguments> simpleProgram() {
        return Stream.of(
                Arguments.of(new int[]{3, 15, 3, 16, 1002, 16, 10, 16, 1, 16, 15, 15, 4, 15, 99, 0, 0}, 4, 43210), //4,3,2,1,0
                Arguments.of(new int[]{3, 23, 3, 24, 1002, 24, 10, 24, 1002, 23, -1, 23,
                        101, 5, 23, 23, 1, 24, 23, 23, 4, 23, 99, 0, 0}, 4, 54321), //0,1,2,3,4
                Arguments.of(new int[]{3, 31, 3, 32, 1002, 32, 10, 32, 1001, 31, -2, 31, 1007, 31, 0, 33,
                        1002, 33, 7, 33, 1, 33, 31, 31, 1, 32, 31, 31, 4, 31, 99, 0, 0, 0}, 4, 65210) //1,0,4,3,2
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgram")
    public void simpleProgramTest(int[] program, int rangeClosed, int expected) {
        AmplificationCircuit circuit = new AmplificationCircuit(program, rangeClosed);
        int actual = circuit.calculateMaxSignal();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleProgramFile() {
        return Stream.of(
                Arguments.of("AmplificationCircuit", 4, 75228)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgramFile")
    public void simpleProgramFileTest(String fileName, int rangeClosed, int expected) throws Exception {
        int[] program = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToInt(Integer::valueOf).toArray();
        AmplificationCircuit circuit = new AmplificationCircuit(program, rangeClosed);
        int actual = circuit.calculateMaxSignal();
        assertEquals(expected, actual);
    }

}
