package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeaconScannerTest extends BaseTest{

    @Test
    public void coord_shouldCorrectlyDetermineDiff(){
        final BeaconScanner.Coord first = new BeaconScanner.Coord(-5, 0, 15);
        final BeaconScanner.Coord second = new BeaconScanner.Coord(20, -6, 33);

        final BeaconScanner.Coord actual = first.getAbsoluteDiff(second);
        final BeaconScanner.Coord expected = new BeaconScanner.Coord(25, 6, 18);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("BeaconScanner_example", 79)
                , Arguments.of("BeaconScanner", 315)
        );
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconScanner beaconScanner = new BeaconScanner(input);
        final int actual = beaconScanner.getUniqueBeaconsSize();

        assertEquals(expected, actual);
    }
}
