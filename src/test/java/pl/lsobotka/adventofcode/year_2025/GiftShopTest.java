package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftShopTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/GiftShop_example", 1227775554), //
                Arguments.of("2025/GiftShop", 38437576669L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final GiftShop giftShop = new GiftShop(input);
        final long actual = giftShop.sumOfInvalidIds();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/GiftShop_example", 4174379265L), //
                Arguments.of("2025/GiftShop", 49046150754L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final GiftShop giftShop = new GiftShop(input);
        final long actual = giftShop.sumOfInvalidIdsByPattern();
        assertEquals(expected, actual);
    }

}