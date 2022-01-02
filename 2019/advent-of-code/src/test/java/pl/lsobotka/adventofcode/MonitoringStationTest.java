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
        final List<String> input = getFileInput(fileName);
        final char[][] asteroidMap = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            asteroidMap[i] = input.get(i).toCharArray();
        }

        final MonitoringStation station = new MonitoringStation(asteroidMap);
        final int actual = station.findBestLocation();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> destroyAsteroids() {
        return Stream.of(
                Arguments.of("MonitoringStation_example_05", 200, 802),
                Arguments.of("MonitoringStation", 200, -1)
        );
    }

    @ParameterizedTest
    @MethodSource("destroyAsteroids")
    public void destroyAsteroidsTest(String fileName, int asteroidNumber, int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final char[][] asteroidMap = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            asteroidMap[i] = input.get(i).toCharArray();
        }

        final MonitoringStation station = new MonitoringStation(asteroidMap);
        final int actual = station.getValueForAsteroid(asteroidNumber);
        assertEquals(expected, actual);
    }

}
