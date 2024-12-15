package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseWoesTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/WarehouseWoes_example_1", 2028), //
                Arguments.of("2024/WarehouseWoes_example_2", 10092), //
                Arguments.of("2024/WarehouseWoes", 1509863));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final WarehouseWoes warehouseWoes = new WarehouseWoes(lines);
        final long actual = warehouseWoes.sumOfBoxesCoordinate();
        assertEquals(expected, actual);
    }

}