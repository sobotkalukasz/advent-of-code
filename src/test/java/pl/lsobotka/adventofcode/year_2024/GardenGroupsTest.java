package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class GardenGroupsTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/GardenGroups_example_1", 140), //
                Arguments.of("2024/GardenGroups_example_2", 772), //
                Arguments.of("2024/GardenGroups_example_3", 1930), //
                Arguments.of("2024/GardenGroups", 1431316));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final GardenGroups gardenGroups = new GardenGroups(input);
        final long actual = gardenGroups.fencePrice();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/GardenGroups_example_1", 80), //
                Arguments.of("2024/GardenGroups_example_2", 436), //
                Arguments.of("2024/GardenGroups_example_3", 1206), //
                Arguments.of("2024/GardenGroups_example_4", 236), //
                Arguments.of("2024/GardenGroups_example_5", 368), //
                Arguments.of("2024/GardenGroups", 821428));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final GardenGroups gardenGroups = new GardenGroups(input);
        final long actual = gardenGroups.fencePriceBySide();
        assertEquals(expected, actual);
    }

}