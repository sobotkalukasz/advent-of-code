package pl.lsobotka.adventofcode.year_2019;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniversalOrbitMapTest extends BaseTest {

    private static Stream<Arguments> simpleOrbitMap() {
        return Stream.of(
                Arguments.of(List.of("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L"),
                        42));
    }

    @ParameterizedTest
    @MethodSource("simpleOrbitMap")
    void simpleOrbitMapTest(List<String> input, int expected) {
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbits();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleOrbitMapFile() {
        return Stream.of(Arguments.of("2019/UniversalOrbitMap", 278744));
    }

    @ParameterizedTest
    @MethodSource("simpleOrbitMapFile")
    void simpleOrbitMapFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbits();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> orbitTransfer() {
        return Stream.of(Arguments.of(
                List.of("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU",
                        "I)SAN"), "YOU", "SAN", 4));
    }

    @ParameterizedTest
    @MethodSource("orbitTransfer")
    void orbitTransferTest(List<String> input, String from, String to, int expected) {
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbitalTransfer(from, to);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> orbitTransferFile() {
        return Stream.of(Arguments.of("2019/UniversalOrbitMap", "YOU", "SAN", 475));
    }

    @ParameterizedTest
    @MethodSource("orbitTransferFile")
    void orbitTransferFileTest(String fileName, String from, String to, int expected) {
        List<String> input = getFileInput(fileName);
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbitalTransfer(from, to);
        assertEquals(expected, actual);
    }
}
