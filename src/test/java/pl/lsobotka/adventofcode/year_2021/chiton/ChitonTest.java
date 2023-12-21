package pl.lsobotka.adventofcode.year_2021.chiton;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChitonTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/Chiton_example", 40L), Arguments.of("2021/Chiton", 386));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Chiton chiton = new Chiton(input);
        final long actual = chiton.findLowestPath();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceLargeMapFile() {
        return Stream.of(Arguments.of("2021/Chiton_example", 315), Arguments.of("2021/Chiton", 2806));
    }

    @ParameterizedTest
    @MethodSource("testResourceLargeMapFile")
    void testResourceLargeMapFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Chiton chiton = new Chiton(input);
        final long actual = chiton.findLowestPathOnLargeMap();
        assertEquals(expected, actual);
    }
}
