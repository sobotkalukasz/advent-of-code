package pl.lsobotka.adventofcode.year_2023;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class LensLibraryTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/LensLibrary_example", 1320), //
                Arguments.of("2023/LensLibrary", 511_215));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final LensLibrary lensLibrary = new LensLibrary(input);
        final long actual = lensLibrary.sumOfLensHash();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/LensLibrary_example", 145), //
                Arguments.of("2023/LensLibrary", 236_057));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final LensLibrary lensLibrary = new LensLibrary(input);
        final long actual = lensLibrary.calculateFocussingPower();
        assertEquals(expected, actual);
    }

}