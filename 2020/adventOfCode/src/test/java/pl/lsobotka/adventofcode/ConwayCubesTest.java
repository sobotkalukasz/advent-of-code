package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConwayCubesTest {

    private static Stream<Arguments> cubesData(){
        return Stream.of(
                Arguments.of(new char[][][]{{{'.', '#', '.'},{'.', '.', '#'},{'#', '#', '#'}}}, 6, 112)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesData")
    public void cubesDataTest(char[][][] data, int cycles, long expected) {

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(data, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesDataFile(){
        return Stream.of(
                Arguments.of("src/test/resources/ConwayCubes", 6, 424)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesDataFile")
    public void cubesDataFileTest(String path, int cycles, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        char[][][] arrData = new char[1][data.size()][0];
        for(int i = 0; i < data.size(); i++){
            arrData[0][i] = data.get(i).toCharArray();
        }

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(arrData, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesHyperData(){
        return Stream.of(
                Arguments.of(new char[][][][]{{{{'.', '#', '.'},{'.', '.', '#'},{'#', '#', '#'}}}}, 6, 848)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesHyperData")
    public void cubesHyperDataTest(char[][][][] data, int cycles, long expected) {

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(data, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesHyperDataFile(){
        return Stream.of(
                Arguments.of("src/test/resources/ConwayCubes", 6, 2460)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesHyperDataFile")
    public void cubesHyperDataFileTest(String path, int cycles, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        char[][][][] arrData = new char[1][1][data.size()][0];
        for(int i = 0; i < data.size(); i++){
            arrData[0][0][i] = data.get(i).toCharArray();
        }

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(arrData, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }
}
