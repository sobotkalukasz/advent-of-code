package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrashCompactorTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/TrashCompactor_example", 4277556), //
                Arguments.of("2025/TrashCompactor", 6725216329103L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final TrashCompactor trashCompactor = new TrashCompactor(input);
        final long actual = trashCompactor.grandTotal();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/TrashCompactor_example", 3263827), //
                Arguments.of("2025/TrashCompactor", 10600728112865L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final TrashCompactor trashCompactor = new TrashCompactor(input);
        final long actual = trashCompactor.correctTotal();
        assertEquals(expected, actual);
    }

}