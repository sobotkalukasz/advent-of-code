package pl.lsobotka.adventofcode;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TuningTroubleTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7), //
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 5), //
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 6), //
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10), //
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void simpleFirstStarExampleTest(final String input, final int expected) {
        final TuningTrouble tuningTrouble = new TuningTrouble(input);
        final int actual = tuningTrouble.determineStartOfPacketIndex();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleSecondStarExample() {
        return Stream.of(Arguments.of("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19), //
                Arguments.of("bvwbjplbgvbhsrlpgdmjqwftvncz", 23), //
                Arguments.of("nppdvjthqldpwncqszvftbrmjlhg", 23), //
                Arguments.of("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29), //
                Arguments.of("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26));
    }

    @ParameterizedTest
    @MethodSource("simpleSecondStarExample")
    public void simpleSecondStarExampleTest(final String input, final int expected) {
        final TuningTrouble tuningTrouble = new TuningTrouble(input);
        final int actual = tuningTrouble.determineStartOfMessageIndex();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("TuningTrouble", 1109));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarTest(final String fileName, final int expected) {
        final String input = String.join("", getFileInput(fileName));
        final TuningTrouble tuningTrouble = new TuningTrouble(input);
        final int actual = tuningTrouble.determineStartOfPacketIndex();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStarFile() {
        return Stream.of(Arguments.of("TuningTrouble", 3965));
    }

    @ParameterizedTest
    @MethodSource("secondStarFile")
    public void secondStarTest(final String fileName, final int expected) {
        final String input = String.join("", getFileInput(fileName));
        final TuningTrouble tuningTrouble = new TuningTrouble(input);
        final int actual = tuningTrouble.determineStartOfMessageIndex();
        assertEquals(expected, actual);
    }
}
