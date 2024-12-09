package pl.lsobotka.adventofcode.year_2024;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class DiskFragmenterTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/DiskFragmenter_example", 1928), //
                Arguments.of("2024/DiskFragmenter", 6320029754031L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final String input = getFileInputSingleLine(fileName);
        final DiskFragmenter diskFragmenter = new DiskFragmenter(input);
        final long actual = diskFragmenter.getChecksum();
        assertEquals(expected, actual);
    }

}