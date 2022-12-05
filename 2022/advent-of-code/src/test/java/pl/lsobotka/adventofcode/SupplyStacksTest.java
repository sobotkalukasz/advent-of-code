package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupplyStacksTest extends BaseTest {

    private static Stream<Arguments> simpleFirstStarExample() {
        return Stream.of(Arguments.of(List.of( //
                "    [D]    ", //
                "[N] [C]    ", //
                "[Z] [M] [P]", //
                " 1   2   3 ", //
                "", //
                "move 1 from 2 to 1", //
                "move 3 from 1 to 3", //
                "move 2 from 2 to 1", //
                "move 1 from 1 to 2"), "CMZ"));
    }

    @ParameterizedTest
    @MethodSource("simpleFirstStarExample")
    public void simpleFirstStarExampleTest(final List<String> input, final String expected) {
        final SupplyStacks supplyStacks = new SupplyStacks(input);
        final String actual = supplyStacks.applySingleOperations();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleSecondStarExample() {
        return Stream.of(Arguments.of(List.of( //
                "    [D]    ", //
                "[N] [C]    ", //
                "[Z] [M] [P]", //
                " 1   2   3 ", //
                "", //
                "move 1 from 2 to 1", //
                "move 3 from 1 to 3", //
                "move 2 from 2 to 1", //
                "move 1 from 1 to 2"), "MCD"));
    }

    @ParameterizedTest
    @MethodSource("simpleSecondStarExample")
    public void simpleSecondStarExampleTest(final List<String> input, final String expected) {
        final SupplyStacks supplyStacks = new SupplyStacks(input);
        final String actual = supplyStacks.applyBulkOperations();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> firstStarFile() {
        return Stream.of(Arguments.of("SupplyStacks", "SHMSDGZVC"));
    }

    @ParameterizedTest
    @MethodSource("firstStarFile")
    public void firstStarTest(final String fileName, final String expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final SupplyStacks supplyStacks = new SupplyStacks(input);
        final String actual = supplyStacks.applySingleOperations();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStarFile() {
        return Stream.of(Arguments.of("SupplyStacks", "VRZGHDFBQ"));
    }

    @ParameterizedTest
    @MethodSource("secondStarFile")
    public void secondStarTest(final String fileName, final String expected) throws Exception {
        final List<String> input = getFileInput(fileName);
        final SupplyStacks supplyStacks = new SupplyStacks(input);
        final String actual = supplyStacks.applyBulkOperations();
        assertEquals(expected, actual);
    }

}