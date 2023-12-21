package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DockingDataTest extends BaseTest {

    private static Stream<Arguments> dockingData() {
        return Stream.of(Arguments.of(new ArrayList<>(
                Arrays.asList("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", "mem[8] = 11", "mem[7] = 101",
                        "mem[8] = 0")), 165));
    }

    @ParameterizedTest
    @MethodSource("dockingData")
    void dockingDataTest(List<String> data, long expected) {

        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemValue(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataFile() {
        return Stream.of(Arguments.of("2020/DockingData", 13496669152158L));
    }

    @ParameterizedTest
    @MethodSource("dockingDataFile")
    void dockingDataFileTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);

        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemValue(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataComplex() {
        return Stream.of(Arguments.of(new ArrayList<>(
                Arrays.asList("mask = 000000000000000000000000000000X1001X", "mem[42] = 100",
                        "mask = 00000000000000000000000000000000X0XX", "mem[26] = 1")), 208));
    }

    @ParameterizedTest
    @MethodSource("dockingDataComplex")
    public void dockingDataComplexTest(List<String> data, long expected) {

        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemAddress(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataComplexFile() {
        return Stream.of(Arguments.of("2020/DockingData", 3278997609887L));
    }

    @ParameterizedTest
    @MethodSource("dockingDataComplexFile")
    public void dockingDataComplexFileTest(String fileName, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemAddress(input);
        assertEquals(expected, actual);
    }
}
