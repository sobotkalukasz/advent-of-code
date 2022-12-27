package pl.lsobotka.adventofcode;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlawedFrequencyTransmissionTest extends BaseTest {

    private static Stream<Arguments> exampleInputSignalTestResource() {
        return Stream.of(Arguments.of("12345678", new int[] { 0, 1, 0, -1 }, 1, "48226158"),
                Arguments.of("12345678", new int[] { 0, 1, 0, -1 }, 2, "34040438"),
                Arguments.of("12345678", new int[] { 0, 1, 0, -1 }, 3, "03415518"),
                Arguments.of("12345678", new int[] { 0, 1, 0, -1 }, 4, "01029498"),
                Arguments.of("80871224585914546619083218645595", new int[] { 0, 1, 0, -1 }, 100, "24176176"),
                Arguments.of("19617804207202209144916044189917", new int[] { 0, 1, 0, -1 }, 100, "73745418"),
                Arguments.of("69317163492948606335995924319873", new int[] { 0, 1, 0, -1 }, 100, "52432133"));
    }

    @ParameterizedTest
    @MethodSource("exampleInputSignalTestResource")
    public void exampleInputTest(final String input, final int[] basePattern, final int phases, final String expected) {

        final FlawedFrequencyTransmission transmission = new FlawedFrequencyTransmission(basePattern, input);
        final String actual = transmission.getOutputAfter(phases);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("FlawedFrequencyTransmission", new int[] { 0, 1, 0, -1 }, 100, "19239468"));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void firstStarTest(final String fileName, final int[] basePattern, final int phases, final String expected) {
        final String input = String.join("", getFileInput(fileName));

        final FlawedFrequencyTransmission transmission = new FlawedFrequencyTransmission(basePattern, input);
        final String actual = transmission.getOutputAfter(phases);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> realMessageTestResourceFile() {
        return Stream.of(Arguments.of("FlawedFrequencyTransmission", new int[] { 0, 1, 0, -1 }, "96966221"));
    }

    @ParameterizedTest
    @MethodSource("realMessageTestResourceFile")
    public void realMessageTest(final String fileName, final int[] basePattern, final String expected) {
        final String input = String.join("", getFileInput(fileName));

        final FlawedFrequencyTransmission transmission = new FlawedFrequencyTransmission(basePattern, input);
        final String actual = transmission.getRealOutput();
        assertEquals(expected, actual);
    }
}
