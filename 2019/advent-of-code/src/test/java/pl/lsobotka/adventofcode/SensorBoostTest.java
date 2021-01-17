package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SensorBoostTest extends BaseTest {

    private static Stream<Arguments> sensorBoost() {
        return Stream.of(
                Arguments.of(new long[]{1102, 34915192, 34915192, 7, 4, 7, 99, 0}, 0L, 1219070632396864L),
                Arguments.of(new long[]{104, 1125899906842624L, 99}, 0, 1125899906842624L)
        );
    }

    @ParameterizedTest
    @MethodSource("sensorBoost")
    public void sensorBoostTest(long[] program, long input, long expected) {
        SensorBoost boost = new SensorBoost(program, input);
        List<Long> actual = boost.execute();
        assertEquals(expected, actual.get(0));
    }

    private static Stream<Arguments> sensorBoostExample() {
        return Stream.of(
                Arguments.of(new long[]{109, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99}, 0L)
        );
    }

    @ParameterizedTest
    @MethodSource("sensorBoostExample")
    public void sensorBoostExampleTest(long[] program, long input) {
        SensorBoost boost = new SensorBoost(program, input);
        List<Long> actual = boost.execute();
        List<Long> expected = LongStream.of(program).boxed().collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> sensorBoostFile() {
        return Stream.of(
                Arguments.of("SensorBoost", 1L, 2518058886L)
        );
    }

    @ParameterizedTest
    @MethodSource("sensorBoostFile")
    public void sensorBoostFileTest(String fileName, long input, long expected) throws Exception {
        long[] program = getFileInput(fileName).stream().map(s -> s.split(",")).flatMap(Stream::of).mapToLong(Long::valueOf).toArray();
        SensorBoost boost = new SensorBoost(program, input);
        List<Long> actual = boost.execute();
        assertEquals(expected, actual.stream().filter(i -> i != 0).findFirst().orElse(0L));
    }
}
