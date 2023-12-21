package pl.lsobotka.adventofcode.year_2019;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoringStationTest extends BaseTest {

    private static Stream<Arguments> asteroids() {
        return Stream.of(Arguments.of("2019/MonitoringStation_example_01", 33),
                Arguments.of("2019/MonitoringStation_example_02", 8),
                Arguments.of("2019/MonitoringStation_example_03", 35),
                Arguments.of("2019/MonitoringStation_example_04", 41),
                Arguments.of("2019/MonitoringStation_example_05", 210), Arguments.of("2019/MonitoringStation", 309));
    }

    @ParameterizedTest
    @MethodSource("asteroids")
    void findMonitoringStationTest(String fileName, int expected) {
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
        return Stream.of(Arguments.of("2019/MonitoringStation_example_05", 200, 802),
                Arguments.of("2019/MonitoringStation", 200, 416));
    }

    @ParameterizedTest
    @MethodSource("destroyAsteroids")
    void destroyAsteroidsTest(String fileName, int asteroidNumber, int expected) {
        final List<String> input = getFileInput(fileName);
        final char[][] asteroidMap = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            asteroidMap[i] = input.get(i).toCharArray();
        }

        final MonitoringStation station = new MonitoringStation(asteroidMap);
        final int actual = station.getValueForDestroyedAsteroid(asteroidNumber);
        assertEquals(expected, actual);
    }

}
