package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryDiagnosticTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(
                Arrays.asList("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001",
                        "00010", "01010"), 198));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    public void simpleExampleTest(final List<String> diagnosticReport, final long expected) {

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getPowerConsumption();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("BinaryDiagnostic", 3985686));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void starOneFileTest(final String fileName, final long expected) throws Exception {
        final List<String> diagnosticReport = getFileInput(fileName);

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getPowerConsumption();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleExampleLifeSupport() {
        return Stream.of(Arguments.of(
                Arrays.asList("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001",
                        "00010", "01010"), 230));
    }

    @ParameterizedTest
    @MethodSource("simpleExampleLifeSupport")
    public void simpleExampleLifeSupportTest(final List<String> diagnosticReport, final long expected) {

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getLifeSupport();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceLifeSupportFile() {
        return Stream.of(Arguments.of("BinaryDiagnostic", 2555739));
    }

    @ParameterizedTest
    @MethodSource("testResourceLifeSupportFile")
    public void starTwoFileTest(final String fileName, final long expected) throws Exception {
        final List<String> diagnosticReport = getFileInput(fileName);

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getLifeSupport();
        assertEquals(expected, actual);
    }
}
