package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationOrderTest extends BaseTest {

    private static Stream<Arguments> mathData() {
        return Stream.of(Arguments.of("1 + 2 * 3 + 4 * 5 + 6", 71D), Arguments.of("2 * 3 + (4 * 5)", 26D),
                Arguments.of("1 + (2 * 3) + (4 * (5 + 6))", 51D), Arguments.of("5 + (8 * 3 + 9 + 3 * 4 * 3)", 437D),
                Arguments.of("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 12240D),
                Arguments.of("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", 13632D));
    }

    @ParameterizedTest
    @MethodSource("mathData")
    void mathDataTest(String input, double expected) {

        OperationOrder oo = new OperationOrder();
        double actual = oo.calculate(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> mathDataFile() {
        return Stream.of(Arguments.of("2020/OperationOrder", 5374004645253L));
    }

    @ParameterizedTest
    @MethodSource("mathDataFile")
    void mathDataFileTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);

        OperationOrder oo = new OperationOrder();
        long sum = input.stream().mapToLong(oo::calculate).sum();
        assertEquals(expected, sum);
    }

    private static Stream<Arguments> mathWeirdData() {
        return Stream.of(Arguments.of("1 + 2 * 3 + 4 * 5 + 6", 231), //
                Arguments.of("2 * 3 + (4 * 5)", 46), //
                Arguments.of("1 + (2 * 3) + (4 * (5 + 6))", 51), //
                Arguments.of("5 + (8 * 3 + 9 + 3 * 4 * 3)", 1445), //
                Arguments.of("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))", 669060), //
                Arguments.of("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2", 23340));
    }

    @ParameterizedTest
    @MethodSource("mathWeirdData")
    void mathWeirdDataTest(String input, int expected) {

        OperationOrder oo = new OperationOrder();
        long actual = oo.calculateWeird(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> mathWeirdDataFile() {
        return Stream.of(Arguments.of("2020/OperationOrder", 88782789402798L));
    }

    @ParameterizedTest
    @MethodSource("mathWeirdDataFile")
    void mathWeirdDataFileTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);

        OperationOrder oo = new OperationOrder();
        long sum = input.stream().mapToLong(oo::calculateWeird).sum();
        assertEquals(expected, sum);
    }
}
