package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SevenSegmentSearchTest extends BaseTest {

    private static Stream<Arguments> testResourceFirstStarFile() {
        return Stream.of(Arguments.of("2021/SevenSegmentSearch_example", 26), //
                Arguments.of("2021/SevenSegmentSearch", 387));
    }

    @ParameterizedTest
    @MethodSource("testResourceFirstStarFile")
    void testResourceFirstStarFileTest(final String fileName, final long expected) {
        final List<String> outputValues = getFileInput(fileName);

        final SevenSegmentSearch sevenSegmentSearch = new SevenSegmentSearch();
        final long actual = sevenSegmentSearch.countUniqueOutputValues(outputValues);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceSecondStarFile() {
        return Stream.of(Arguments.of("2021/SevenSegmentSearch_example", 61229), //
                Arguments.of("2021/SevenSegmentSearch", 986034));
    }

    @ParameterizedTest
    @MethodSource("testResourceSecondStarFile")
    void testResourceSecondStarFileTest(final String fileName, final long expected) {
        final List<String> outputValues = getFileInput(fileName);

        final SevenSegmentSearch sevenSegmentSearch = new SevenSegmentSearch();
        final long actual = sevenSegmentSearch.sumOutputValues(outputValues);
        assertEquals(expected, actual);
    }

}
