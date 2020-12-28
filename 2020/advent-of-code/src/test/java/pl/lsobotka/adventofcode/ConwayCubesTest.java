package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConwayCubesTest extends BaseTest {

    private static Stream<Arguments> cubesData() {
        return Stream.of(
                Arguments.of(new char[][][]{{{'.', '#', '.'}, {'.', '.', '#'}, {'#', '#', '#'}}}, 6, 112)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesData")
    public void cubesDataTest(char[][][] data, int cycles, long expected) {

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(data, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesDataFile() {
        return Stream.of(
                Arguments.of("ConwayCubes", 6, 424)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesDataFile")
    public void cubesDataFileTest(String fileName, int cycles, long expected) throws Exception {
        List<String> data = getFileInput(fileName);

        char[][][] arrData = new char[1][data.size()][0];
        for (int i = 0; i < data.size(); i++) {
            arrData[0][i] = data.get(i).toCharArray();
        }

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(arrData, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesHyperData() {
        return Stream.of(
                Arguments.of(new char[][][][]{{{{'.', '#', '.'}, {'.', '.', '#'}, {'#', '#', '#'}}}}, 6, 848)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesHyperData")
    public void cubesHyperDataTest(char[][][][] data, int cycles, long expected) {

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(data, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> cubesHyperDataFile() {
        return Stream.of(
                Arguments.of("ConwayCubes", 6, 2460)
        );
    }

    @ParameterizedTest
    @MethodSource("cubesHyperDataFile")
    public void cubesHyperDataFileTest(String fileName, int cycles, long expected) throws Exception {
        List<String> data = getFileInput(fileName);

        char[][][][] arrData = new char[1][1][data.size()][0];
        for (int i = 0; i < data.size(); i++) {
            arrData[0][0][i] = data.get(i).toCharArray();
        }

        ConwayCubes cc = new ConwayCubes();
        long actual = cc.calculate(arrData, cycles, ConwayCubes.ACTIVE);
        assertEquals(expected, actual);
    }
}
