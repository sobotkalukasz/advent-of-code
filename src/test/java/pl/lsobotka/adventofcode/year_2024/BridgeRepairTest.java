package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BridgeRepairTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/BridgeRepair_example", 3749), //
                Arguments.of("2024/BridgeRepair", 8401132154762L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final BridgeRepair bridgeRepair = new BridgeRepair(input);
        final long actual = bridgeRepair.findSumOfCorrectEquations().longValue();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStr() {
        return Stream.of(Arguments.of("2024/BridgeRepair_example", 11387), //
                Arguments.of("2024/BridgeRepair", 95297119227552L));
    }

    @ParameterizedTest
    @MethodSource("secondStr")
    void secondStrTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final BridgeRepair bridgeRepair = new BridgeRepair(input);
        final long actual = bridgeRepair.findSumOfCorrectEquationsWithConcatenation().longValue();
        assertEquals(expected, actual);
    }
}
