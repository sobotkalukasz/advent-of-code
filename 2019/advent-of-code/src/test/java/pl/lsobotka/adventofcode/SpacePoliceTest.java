package pl.lsobotka.adventofcode;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpacePoliceTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("SpacePolice", 2082));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void countPaintedPanelTest(String fileName, int expected) throws Exception {
        long[] program = getFileInput(fileName).stream()
                .map(s -> s.split(","))
                .flatMap(Stream::of)
                .mapToLong(Long::valueOf)
                .toArray();

        final SpacePolice spacePolice = new SpacePolice(program);
        int actual = spacePolice.countPaintedPanels();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> printResourceFile() {
        return Stream.of(Arguments.of("SpacePolice", "FARBCFJK"));
    }

    @ParameterizedTest
    @MethodSource("printResourceFile")
    public void printPaintedPanelTest(final String fileName, final String expected) throws Exception {
        long[] program = getFileInput(fileName).stream()
                .map(s -> s.split(","))
                .flatMap(Stream::of)
                .mapToLong(Long::valueOf)
                .toArray();

        final SpacePolice spacePolice = new SpacePolice(program);
        spacePolice.printPaintedPanels();
        assertEquals(expected, "FARBCFJK");
    }
}
