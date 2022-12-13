package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.lsobotka.adventofcode.DistressSignal.*;

public class DistressSignalTest extends BaseTest {

    private static Stream<Arguments> closeIndex() {
        return Stream.of(Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", 1, 26), //
                Arguments.of("[1,[2,[3,[4,[5,6,7]]]],8,9]", 4, 21));
    }

    @ParameterizedTest
    @MethodSource("closeIndex")
    public void closeIndexTest(final String testString, final int from, final int expected) {
        final PairFactory factory = new PairFactory();
        final int actual = factory.determineCloseIndex(testString, from);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("DistressSignal_simple", 13), //
                Arguments.of("DistressSignal", 5503));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final DistressSignal signal = new DistressSignal(input);
        final int actual = signal.determineSumOfRightOrderPairs();
        assertEquals(expected, actual);
    }
}
