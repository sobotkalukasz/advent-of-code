package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class MirageMaintenanceTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("MirageMaintenance_example", 114), //
                Arguments.of("MirageMaintenance", 1696140818));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MirageMaintenance mirageMaintenance = new MirageMaintenance(input);
        final long actual = mirageMaintenance.solveIt();
        assertEquals(expected, actual);
    }

}