package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class CodeChronicleTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/CodeChronicle_example", 3), //
                Arguments.of("2024/CodeChronicle", 3344));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int expected) {
        final List<String> lines = getFileInput(fileName);
        final CodeChronicle code = new CodeChronicle(lines);
        final int actual = code.countUniquePairs();
        assertEquals(expected, actual);
    }

}