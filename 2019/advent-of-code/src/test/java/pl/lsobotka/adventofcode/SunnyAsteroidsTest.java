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
                Arguments.of(new int[]{3, 0, 4, 0, 99}, 5, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgram")
    public void simpleProgramTest(int[] program, int input, int expected) {
        SunnyAsteroids sunnyAsteroids = new SunnyAsteroids(program, input);
        List<Integer> actual = sunnyAsteroids.execute();
        assertEquals(actual.size(), 1);
        assertEquals(expected, actual.get(0));
    }

    private static Stream<Arguments> simpleProgramFile() {
        return Stream.of(
                Arguments.of("SunnyAsteroids", 1, 16348437)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleProgramFile")
    public void simpleProgramFileTest(String fileName, int input, int expected) throws Exception {
        int[] program = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToInt(Integer::valueOf).toArray();
        SunnyAsteroids sunnyAsteroids = new SunnyAsteroids(program, input);
        List<Integer> actual = sunnyAsteroids.execute();
        assertEquals(expected, actual.stream().filter(i -> i != 0).findFirst().orElse(0));
    }

}
