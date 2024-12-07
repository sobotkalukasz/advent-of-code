package pl.lsobotka.adventofcode.year_2024;

import java.math.BigInteger;
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
        final BigInteger sumOfCorrectEquations = bridgeRepair.findSumOfCorrectEquations();
        final long actual = sumOfCorrectEquations.longValue();
        assertEquals(expected, actual);
    }
}
