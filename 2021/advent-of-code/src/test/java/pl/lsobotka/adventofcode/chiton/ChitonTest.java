package pl.lsobotka.adventofcode.chiton;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChitonTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("Chiton_example", 40L), Arguments.of("Chiton", 386));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Chiton chiton = new Chiton(input);
        final long actual = chiton.findLowestPath();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceLargeMapFile() {
        return Stream.of(Arguments.of("Chiton_example", 315), Arguments.of("Chiton", 2806));
    }

    @ParameterizedTest
    @MethodSource("testResourceLargeMapFile")
    public void testResourceLargeMapFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Chiton chiton = new Chiton(input);
        final long actual = chiton.findLowestPathOnLargeMap();
        assertEquals(expected, actual);
    }
}
