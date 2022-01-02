package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SevenSegmentSearchTest extends BaseTest {

    private static Stream<Arguments> testResourceFirstStarFile() {
        return Stream.of(Arguments.of("SevenSegmentSearch_example", 26), Arguments.of("SevenSegmentSearch", 387));
    }

    @ParameterizedTest
    @MethodSource("testResourceFirstStarFile")
    public void testResourceFirstStarFileTest(final String fileName, final long expected) throws Exception {
        final List<String> outputValues = getFileInput(fileName);

        final SevenSegmentSearch sevenSegmentSearch = new SevenSegmentSearch();
        final long actual = sevenSegmentSearch.countUniqueOutputValues(outputValues);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceSecondStarFile() {
        return Stream.of(Arguments.of("SevenSegmentSearch_example", 61229), Arguments.of("SevenSegmentSearch", 986034));
    }

    @ParameterizedTest
    @MethodSource("testResourceSecondStarFile")
    public void testResourceSecondStarFileTest(final String fileName, final long expected) throws Exception {
        final List<String> outputValues = getFileInput(fileName);

        final SevenSegmentSearch sevenSegmentSearch = new SevenSegmentSearch();
        final long actual = sevenSegmentSearch.sumOutputValues(outputValues);
        assertEquals(expected, actual);
    }

}
