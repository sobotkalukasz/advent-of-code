package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreetopTreeHouseTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/TreetopTreeHouse_simple", 21), //
                Arguments.of("2022/TreetopTreeHouse", 1_851));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse(input);
        final long actual = treetopTreeHouse.countVisibleTrees();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2022/TreetopTreeHouse_simple", 8), //
                Arguments.of("2022/TreetopTreeHouse", 574_080));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final TreetopTreeHouse treetopTreeHouse = new TreetopTreeHouse(input);
        final long actual = treetopTreeHouse.highestScenicScore();
        assertEquals(expected, actual);
    }

}
