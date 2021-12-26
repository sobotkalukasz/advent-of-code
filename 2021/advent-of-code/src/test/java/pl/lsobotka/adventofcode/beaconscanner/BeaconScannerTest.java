package pl.lsobotka.adventofcode.beaconscanner;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeaconScannerTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("BeaconScanner_example", 79), Arguments.of("BeaconScanner", 315));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconScanner beaconScanner = new BeaconScanner(input);
        final int actual = beaconScanner.getUniqueBeaconsSize();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceDistanceFile() {
        return Stream.of(Arguments.of("BeaconScanner_example", 3621), Arguments.of("BeaconScanner", 13192));
    }

    @ParameterizedTest
    @MethodSource("testResourceDistanceFile")
    public void testResourceDistanceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconScanner beaconScanner = new BeaconScanner(input);
        final int actual = beaconScanner.getBiggestDistance();

        assertEquals(expected, actual);
    }
}
