package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class MirageMaintenanceTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/MirageMaintenance_example", 114), //
                Arguments.of("2023/MirageMaintenance", 1696140818));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MirageMaintenance mirageMaintenance = new MirageMaintenance(input);
        final long actual = mirageMaintenance.sumOfNext();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2023/MirageMaintenance_example_2", 5), //
                Arguments.of("2023/MirageMaintenance", 1152));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final MirageMaintenance mirageMaintenance = new MirageMaintenance(input);
        final long actual = mirageMaintenance.sumOfPrevious();
        assertEquals(expected, actual);
    }

}