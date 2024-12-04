package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class CaresSearchTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/CaresSearch_example", "XMAS", 18), //
                Arguments.of("2024/CaresSearch", "XMAS", 2593));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final String word, final long expected) {
        final List<String> input = getFileInput(fileName);
        final CaresSearch caresSearch = new CaresSearch(input);
        final long actual = caresSearch.countWord(word);
        assertEquals(expected, actual);
    }

}