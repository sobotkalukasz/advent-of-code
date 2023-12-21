package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReactorRebootTest extends BaseTest {

    private static Stream<Arguments> isOverlapping() {
        return Stream.of(Arguments.of("x=-10..10,y=-10..10,z=-10..10", "x=-10..10,y=-10..10,z=-10..10", true),
                Arguments.of("x=-10..10,y=-10..10,z=-10..10", "x=10..11,y=10..11,z=10..11", true),
                Arguments.of("x=-10..10,y=-10..10,z=-10..10", "x=11..11,y=11..11,z=5..30", false),
                Arguments.of("x=-10..10,y=-10..10,z=-10..10", "x=11..11,y=11..11,z=11..11", false));
    }

    @ParameterizedTest
    @MethodSource("isOverlapping")
    void isOverlapping(final String a, final String b, final boolean expected) {
        final ReactorReboot.Range first = new ReactorReboot.Range(a);
        final ReactorReboot.Range second = new ReactorReboot.Range(b);
        final boolean actual = first.isOverlapping(second);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(
                List.of("on x=10..12,y=10..12,z=10..12", "on x=11..13,y=11..13,z=11..13", "off x=9..11,y=9..11,z=9..11",
                        "on x=10..10,y=10..10,z=10..10"), "x=-50..50,y=-50..50,z=-50..50", 39));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    void simpleExampleBigTest(final List<String> instructions, final String limit, final int expected) {
        final ReactorReboot reactorReboot = new ReactorReboot(instructions, limit);
        final long actual = reactorReboot.applyBigInstructions();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/ReactorReboot_example", "x=-50..50,y=-50..50,z=-50..50", 590784),
                Arguments.of("2021/ReactorReboot", "x=-50..50,y=-50..50,z=-50..50", 647062));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final String limit, final int expected) {
        final List<String> input = getFileInput(fileName);
        final ReactorReboot reactorReboot = new ReactorReboot(input, limit);
        final long actual = reactorReboot.applyBigInstructions();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testBigResourceFile() {
        return Stream.of(Arguments.of("2021/ReactorReboot_example2", 2758514936282235L),
                Arguments.of("2021/ReactorReboot", 1319618626668022L));
    }

    @ParameterizedTest
    @MethodSource("testBigResourceFile")
    void testBigResourceFileTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ReactorReboot reactorReboot = new ReactorReboot(input, null);
        final long actual = reactorReboot.applyBigInstructions();

        assertEquals(expected, actual);
    }
}



