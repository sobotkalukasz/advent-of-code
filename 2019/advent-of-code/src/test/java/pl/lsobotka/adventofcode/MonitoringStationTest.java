package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonitoringStationTest extends BaseTest {

    private static Stream<Arguments> asteroids() {
        return Stream.of(
                Arguments.of("MonitoringStation_example_01", 33),
                Arguments.of("MonitoringStation_example_02", 8),
                Arguments.of("MonitoringStation_example_03", 35),
                Arguments.of("MonitoringStation_example_04", 41),
                Arguments.of("MonitoringStation_example_05", 210),
                Arguments.of("MonitoringStation", 309)
        );
    }

    @ParameterizedTest
    @MethodSource("asteroids")
    public void findMonitoringStationTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);
        char[][] asteroidMap = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            asteroidMap[i] = input.get(i).toCharArray();
        }

        MonitoringStation station = new MonitoringStation(asteroidMap);
        int actual = station.findBestLocation();
        assertEquals(expected, actual);
    }

}
