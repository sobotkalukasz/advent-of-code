package pl.lsobotka.adventofcode.year_2021.beaconscanner;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeaconScannerTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/BeaconScanner_example", 79), Arguments.of("2021/BeaconScanner", 315));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconScanner beaconScanner = new BeaconScanner(input);
        final int actual = beaconScanner.getUniqueBeaconsSize();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceDistanceFile() {
        return Stream.of(Arguments.of("2021/BeaconScanner_example", 3621), Arguments.of("2021/BeaconScanner", 13192));
    }

    @ParameterizedTest
    @MethodSource("testResourceDistanceFile")
    void testResourceDistanceFileTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final BeaconScanner beaconScanner = new BeaconScanner(input);
        final int actual = beaconScanner.getBiggestDistance();

        assertEquals(expected, actual);
    }
}
