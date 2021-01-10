package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UniversalOrbitMapTest extends BaseTest {

    private static Stream<Arguments> simpleOrbitMap() {
        return Stream.of(
                Arguments.of(List.of("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L"), 42)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleOrbitMap")
    public void simpleOrbitMapTest(List<String> input, int expected) {
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbits();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleOrbitMapFile() {
        return Stream.of(
                Arguments.of("UniversalOrbitMap", 278744)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleOrbitMapFile")
    public void simpleOrbitMapFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbits();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> orbitTransfer() {
        return Stream.of(
                Arguments.of(List.of("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN"), "YOU", "SAN", 4)
        );
    }

    @ParameterizedTest
    @MethodSource("orbitTransfer")
    public void orbitTransferTest(List<String> input, String from, String to, int expected) {
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbitalTransfer(from, to);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> orbitTransferFile() {
        return Stream.of(
                Arguments.of("UniversalOrbitMap", "YOU", "SAN", 475)
        );
    }

    @ParameterizedTest
    @MethodSource("orbitTransferFile")
    public void orbitTransferFileTest(String fileName, String from, String to, int expected) throws Exception {
        List<String> input = getFileInput(fileName);
        UniversalOrbitMap orbitMap = new UniversalOrbitMap(input);
        int actual = orbitMap.countOrbitalTransfer(from, to);
        assertEquals(expected, actual);
    }
}
