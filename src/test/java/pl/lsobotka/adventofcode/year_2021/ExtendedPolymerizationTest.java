package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtendedPolymerizationTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/ExtendedPolymerization_example", 10, 1588),
                Arguments.of("2021/ExtendedPolymerization", 10, 2740),
                Arguments.of("2021/ExtendedPolymerization_example", 40, 2188189693529L),
                Arguments.of("2021/ExtendedPolymerization", 40, 2959788056211L));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int steps, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final ExtendedPolymerization extendedPolymerization = new ExtendedPolymerization(input);
        final long actual = extendedPolymerization.getFormula(steps);
        assertEquals(expected, actual);
    }
}
