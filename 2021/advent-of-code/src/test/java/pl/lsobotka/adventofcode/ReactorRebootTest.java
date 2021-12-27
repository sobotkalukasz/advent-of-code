package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactorRebootTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(
                List.of("on x=10..12,y=10..12,z=10..12", "on x=11..13,y=11..13,z=11..13", "off x=9..11,y=9..11,z=9..11",
                        "on x=10..10,y=10..10,z=10..10"), "x=-50..50,y=-50..50,z=-50..50", 39));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    public void simpleExampleTest(final List<String> instructions, final String limit, final int expected) {
        final ReactorReboot reactorReboot = new ReactorReboot(instructions, limit);
        final int actual = reactorReboot.applyInstructions();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("ReactorReboot_example", "x=-50..50,y=-50..50,z=-50..50", 590784),
                Arguments.of("ReactorReboot", "x=-50..50,y=-50..50,z=-50..50", 647062));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final String limit, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final ReactorReboot reactorReboot = new ReactorReboot(input, limit);
        final int actual = reactorReboot.applyInstructions();

        assertEquals(expected, actual);
    }
}
