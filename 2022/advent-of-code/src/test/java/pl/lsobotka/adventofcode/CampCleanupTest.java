package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class CampCleanupTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of(List.of("2-4,6-8", "2-3,4-5", "5-7,7-9", "2-8,3-7", "6-6,4-6", "2-6,4-8"), 2));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void simpleFirstStarExampleTest(final List<String> input, final long expected) {
        final CampCleanup campCleanup = new CampCleanup(input);
        final long actual = campCleanup.countContainOther();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("CampCleanup", 562));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final CampCleanup campCleanup = new CampCleanup(input);
        final long actual = campCleanup.countContainOther();
        assertEquals(expected, actual);
    }
}