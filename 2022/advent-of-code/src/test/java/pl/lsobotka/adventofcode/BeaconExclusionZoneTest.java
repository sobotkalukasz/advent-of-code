package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class BeaconExclusionZoneTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("BeaconExclusionZone_simple", 10, 26), //
                Arguments.of("BeaconExclusionZone", 2_000_000, 4_883_971));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long rowToCheck, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconExclusionZone exclusionZone = new BeaconExclusionZone(input);
        final long actual = exclusionZone.countEmptyFor(rowToCheck);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(
                Arguments.of("BeaconExclusionZone_simple", 20, 56_000_011), //
                Arguments.of("BeaconExclusionZone", 4_000_000, 12_691_026_767_556L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long max, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final BeaconExclusionZone exclusionZone = new BeaconExclusionZone(input);
        final long actual = exclusionZone.findTuningFrequency(max);
        assertEquals(expected, actual);
    }

}