package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class ChronospatialComputerTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/ChronospatialComputer_example_1", "4,2,5,6,7,7,7,7,3,1,0"), //
                Arguments.of("2024/ChronospatialComputer_example_2", "4,6,3,5,6,3,5,2,1,0"), //
                Arguments.of("2024/ChronospatialComputer", "7,4,2,0,5,0,5,3,7"));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final String expected) {
        final List<String> lines = getFileInput(fileName);
        final ChronospatialComputer computer = new ChronospatialComputer(lines);
        final String actual = computer.run();
        assertEquals(expected, actual);
    }

}