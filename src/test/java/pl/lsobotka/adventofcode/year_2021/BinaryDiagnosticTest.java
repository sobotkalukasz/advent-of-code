package pl.lsobotka.adventofcode.year_2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryDiagnosticTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(
                Arrays.asList("00100", "11110", "10110", "10111", "10101", "01111", "00111", "11100", "10000", "11001",
                        "00010", "01010"), 198));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    void simpleExampleTest(final List<String> diagnosticReport, final long expected) {

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getPowerConsumption();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/BinaryDiagnostic", 3985686));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void starOneFileTest(final String fileName, final long expected) {
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
    void simpleExampleLifeSupportTest(final List<String> diagnosticReport, final long expected) {

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getLifeSupport();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceLifeSupportFile() {
        return Stream.of(Arguments.of("2021/BinaryDiagnostic", 2555739));
    }

    @ParameterizedTest
    @MethodSource("testResourceLifeSupportFile")
    void starTwoFileTest(final String fileName, final long expected) {
        final List<String> diagnosticReport = getFileInput(fileName);

        final BinaryDiagnostic binaryDiagnostic = new BinaryDiagnostic(diagnosticReport);
        final long actual = binaryDiagnostic.getLifeSupport();
        assertEquals(expected, actual);
    }
}
