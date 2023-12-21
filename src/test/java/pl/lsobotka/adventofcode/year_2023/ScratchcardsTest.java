package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScratchcardsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/Scratchcards_example", 13), //
                Arguments.of("2023/Scratchcards", 26443));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final Scratchcards Scratchcards = new Scratchcards(input);
        final int actual = Scratchcards.sumCardPoints();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/Scratchcards_example", 30), //
                Arguments.of("2023/Scratchcards", 6284877));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final int expected) {
        final List<String> input = getFileInput(fileName);
        final Scratchcards Scratchcards = new Scratchcards(input);
        final int actual = Scratchcards.countCards();
        assertEquals(expected, actual);
    }
}
