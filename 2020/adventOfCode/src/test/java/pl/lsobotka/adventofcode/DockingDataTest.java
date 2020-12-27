package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DockingDataTest {

    private static Stream<Arguments> dockingData(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", "mem[8] = 11", "mem[7] = 101", "mem[8] = 0")), 165)
        );
    }

    @ParameterizedTest
    @MethodSource("dockingData")
    public void dockingDataTest(List<String> data, long expected) {


        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemValue(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataFile(){
        return Stream.of(
                Arguments.of("src/test/resources/DockingData", 13496669152158L)
        );
    }

    @ParameterizedTest
    @MethodSource("dockingDataFile")
    public void dockingDataFileTest(String path, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();


        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemValue(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataComplex(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("mask = 000000000000000000000000000000X1001X", "mem[42] = 100","mask = 00000000000000000000000000000000X0XX", "mem[26] = 1")), 208)
        );
    }

    @ParameterizedTest
    @MethodSource("dockingDataComplex")
    public void dockingDataComplexTest(List<String> data, long expected) {

        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemAddress(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> dockingDataComplexFile(){
        return Stream.of(
                Arguments.of("src/test/resources/DockingData", 3278997609887L)
        );
    }

    @ParameterizedTest
    @MethodSource("dockingDataComplexFile")
    public void dockingDataComplexFileTest(String path, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();


        DockingData dockingData = new DockingData();
        long actual = dockingData.processMemAddress(data);
        assertEquals(expected, actual);
    }
}
