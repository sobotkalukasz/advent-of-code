package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class CamelCardsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/CamelCards_example", 6440), //
                Arguments.of("2023/CamelCards", 246409899));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CamelCards camelCards = new CamelCards(input);
        final long actual = camelCards.winningScore();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/CamelCards_example", 5905), //
                Arguments.of("2023/CamelCards", 244848487));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CamelCards camelCards = new CamelCards(input);
        final long actual = camelCards.winningScoreWithJoker();
        assertEquals(expected, actual);
    }

}