package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgramAlarmTest extends BaseTest {

    private static Stream<Arguments> simplePrograms() {
        return Stream.of(
                Arguments.of(new int[]{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50}, new int[]{3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50}),
                Arguments.of(new int[]{1, 0, 0, 0, 99}, new int[]{2, 0, 0, 0, 99}),
                Arguments.of(new int[]{2, 3, 0, 3, 99}, new int[]{2, 3, 0, 6, 99}),
                Arguments.of(new int[]{2, 4, 4, 5, 99, 0}, new int[]{2, 4, 4, 5, 99, 9801}),
                Arguments.of(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99}, new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99})
        );
    }

    @ParameterizedTest
    @MethodSource("simplePrograms")
    public void simpleProgramsTest(int[] program, int[] expected) {
        int[] actual = ProgramAlarm.processProgram(program);
        assertArrayEquals(expected, actual);
    }

    private static Stream<Arguments> programAlarm() {
        return Stream.of(
                Arguments.of("ProgramAlarm", 12, 2, 4576384)
        );
    }

    @ParameterizedTest
    @MethodSource("programAlarm")
    public void programAlarmTest(String fileName, int firstVal, int secondVal, int expected) {
        int[] ints = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToInt(Integer::valueOf).toArray();
        ints[1] = firstVal;
        ints[2] = secondVal;

        int[] actual = ProgramAlarm.processProgram(ints);
        assertEquals(expected, actual[0]);
    }

    private static Stream<Arguments> findParams() {
        return Stream.of(
                Arguments.of("ProgramAlarm", 19690720, 5398)
        );
    }

    @ParameterizedTest
    @MethodSource("findParams")
    public void findParamsTest(String fileName, int searchVal, int expected) {
        int[] ints = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToInt(Integer::valueOf).toArray();
        ProgramAlarm.Params params = ProgramAlarm.findParamsFor(ints, searchVal);
        assertEquals(100 * params.noun() + params.verb(), expected);
    }

}
