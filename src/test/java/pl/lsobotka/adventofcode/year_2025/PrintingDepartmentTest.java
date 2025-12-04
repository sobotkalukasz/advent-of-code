package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintingDepartmentTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/PrintingDepartment_example", 13), //
                Arguments.of("2025/PrintingDepartment", 1523));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PrintingDepartment printingDepartment = new PrintingDepartment(input);
        final long actual = printingDepartment.countAccessible();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/PrintingDepartment_example", 43), //
                Arguments.of("2025/PrintingDepartment", 9290));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final PrintingDepartment printingDepartment = new PrintingDepartment(input);
        final long actual = printingDepartment.removeAllPossible();
        assertEquals(expected, actual);
    }

}