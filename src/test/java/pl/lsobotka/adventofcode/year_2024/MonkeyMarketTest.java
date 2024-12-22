package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class MonkeyMarketTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/MonkeyMarket_example_simple", 10, 5_908_254), //
                Arguments.of("2024/MonkeyMarket_example", 2000, 37_327_623), //
                Arguments.of("2024/MonkeyMarket", 2000, 18_694_566_361L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int time, final long expected) {
        final List<Integer> lines = getFileInputAsInteger(fileName);
        final MonkeyMarket monkeyMarket = new MonkeyMarket(lines);
        final long actual = monkeyMarket.sumSecretNumbersAfter(time);
        assertEquals(expected, actual);
    }

}