package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrossedWiresTest extends BaseTest {

    private static Stream<Arguments> simplePaths() {
        return Stream.of(
                Arguments.of(List.of("R8","U5","L5","D3"), List.of("U7","R6","D4","L4"), 6),
                Arguments.of(List.of("R75","D30","R83","U83","L12","D49","R71","U7","L72"), List.of("U62","R66","U55","R34","D71","R55","D58","R83"), 159),
                Arguments.of(List.of("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51"), List.of("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"), 135)
        );
    }

    @ParameterizedTest
    @MethodSource("simplePaths")
    public void simplePathTest(List<String> firstPaths, List<String> secondPaths, int expected) {
        CrossedWires wires = new CrossedWires();
        int actual = wires.calcIntersectionDistance(firstPaths, secondPaths);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simplePathsFile() {
        return Stream.of(
                Arguments.of("CrossedWires", 403)
        );
    }

    @ParameterizedTest
    @MethodSource("simplePathsFile")
    public void simplePathsFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);
        List<String> firstPaths = Stream.of(input.get(0).split(",")).collect(Collectors.toList());
        List<String> secondPaths = Stream.of(input.get(1).split(",")).collect(Collectors.toList());

        CrossedWires wires = new CrossedWires();
        int actual = wires.calcIntersectionDistance(firstPaths, secondPaths);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> fewestSteps() {
        return Stream.of(
                Arguments.of(List.of("R8","U5","L5","D3"), List.of("U7","R6","D4","L4"), 30),
                Arguments.of(List.of("R75","D30","R83","U83","L12","D49","R71","U7","L72"), List.of("U62","R66","U55","R34","D71","R55","D58","R83"), 610),
                Arguments.of(List.of("R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51"), List.of("U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"), 410)
        );
    }

    @ParameterizedTest
    @MethodSource("fewestSteps")
    public void fewestStepsTest(List<String> firstPaths, List<String> secondPaths, int expected) {
        CrossedWires wires = new CrossedWires();
        int actual = wires.calcFewestSteps(firstPaths, secondPaths);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> fewestStepsFile() {
        return Stream.of(
                Arguments.of("CrossedWires", 4158)
        );
    }

    @ParameterizedTest
    @MethodSource("fewestStepsFile")
    public void fewestStepsFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);
        List<String> firstPaths = Stream.of(input.get(0).split(",")).collect(Collectors.toList());
        List<String> secondPaths = Stream.of(input.get(1).split(",")).collect(Collectors.toList());

        CrossedWires wires = new CrossedWires();
        int actual = wires.calcFewestSteps(firstPaths, secondPaths);
        assertEquals(expected, actual);
    }
}
