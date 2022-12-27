package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SunnyAsteroidsTest extends BaseTest {

    private static Stream<Arguments> simpleProgram() {
        return Stream.of(
                Arguments.of(new long[]{3, 0, 4, 0, 99}, 5, 5),
                Arguments.of(new long[]{3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8}, 8L, 1L),
                Arguments.of(new long[]{3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8}, 5L, 0L),
                Arguments.of(new long[]{3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8}, 7L, 1L),
                Arguments.of(new long[]{3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8}, 10L, 0L),
                Arguments.of(new long[]{3, 3, 1108, -1, 8, 3, 4, 3, 99}, 8L, 1L),
                Arguments.of(new long[]{3, 3, 1108, -1, 8, 3, 4, 3, 99}, 5L, 0L),
                Arguments.of(new long[]{3, 3, 1107, -1, 8, 3, 4, 3, 99}, 7L, 1L),
                Arguments.of(new long[]{3, 3, 1107, -1, 8, 3, 4, 3, 99}, 10L, 0L),
                Arguments.of(new long[]{3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9}, 0L, 0L),
                Arguments.of(new long[]{3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9}, 5L, 1L),
                Arguments.of(new long[]{3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1}, 0L, 0L),
                Arguments.of(new long[]{3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1}, 5L, 1L),
                Arguments.of(new long[]{3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                        1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                        999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99}, 7L, 999L),
                Arguments.of(new long[]{3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                        1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                        999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99}, 8L, 1000L),
                Arguments.of(new long[]{3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                        1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                        999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99}, 9L, 1001L)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgram")
    public void simpleProgramTest(long[] program, long input, long expected) {
        SunnyAsteroids sunnyAsteroids = new SunnyAsteroids(program, input);
        List<Long> actual = sunnyAsteroids.execute();
        assertEquals(actual.size(), 1);
        assertEquals(expected, actual.get(0));
    }

    private static Stream<Arguments> simpleProgramFile() {
        return Stream.of(
                Arguments.of("SunnyAsteroids", 1L, 16348437L),
                Arguments.of("SunnyAsteroids", 5L, 6959377L)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgramFile")
    public void simpleProgramFileTest(String fileName, long input, long expected) {
        long[] program = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToLong(Long::valueOf).toArray();
        SunnyAsteroids sunnyAsteroids = new SunnyAsteroids(program, input);
        List<Long> actual = sunnyAsteroids.execute();
        assertEquals(expected, actual.stream().filter(i -> i != 0).findFirst().orElse(0L));
    }

}
