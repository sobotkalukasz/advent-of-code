package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistressSignalTest extends BaseTest {

    private static Stream<Arguments> closeIndex() {
        return Stream.of(Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", 1, 26), //
                Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", 4, 21));
    }

    @ParameterizedTest
    @MethodSource("closeIndex")
    public void closeIndexTest(final String testString, final int from, final int expected) {
        final int actual = DistressSignal.ElementFactory.determineClosingIndex(testString, from);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/DistressSignal_simple", 13), //
                Arguments.of("2022/DistressSignal", 5_503));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final DistressSignal signal = new DistressSignal(input);
        final int actual = signal.determineSumOfRightOrderPairs();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/DistressSignal_simple", 140), //
                Arguments.of("2022/DistressSignal", 20_952));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final DistressSignal signal = new DistressSignal(input);
        final int actual = signal.determineDecoderKey();
        assertEquals(expected, actual);
    }
}
