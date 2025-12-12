package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChristmasTreeFarmTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/ChristmasTreeFarm_example", 2), //
                Arguments.of("2025/ChristmasTreeFarm", 479));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final ChristmasTreeFarm christmasTreeFarm = new ChristmasTreeFarm(input);
        final long actual = christmasTreeFarm.countRegions();
        assertEquals(expected, actual);
    }

}