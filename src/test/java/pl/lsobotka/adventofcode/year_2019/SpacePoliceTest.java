package pl.lsobotka.adventofcode.year_2019;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpacePoliceTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2019/SpacePolice", 2082));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void countPaintedPanelTest(String fileName, int expected) {
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
        return Stream.of(Arguments.of("2019/SpacePolice", "FARBCFJK"));
    }

    @ParameterizedTest
    @MethodSource("printResourceFile")
    void printPaintedPanelTest(final String fileName, final String expected) {
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
