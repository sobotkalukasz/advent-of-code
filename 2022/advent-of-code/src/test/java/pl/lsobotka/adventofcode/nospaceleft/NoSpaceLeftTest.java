package pl.lsobotka.adventofcode.nospaceleft;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class NoSpaceLeftTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("NoSpaceLeft_simple", 100_000, 95_437), //
                Arguments.of("NoSpaceLeft", 100_000, 1_644_735));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final long atMostSize, final long expected) {
        final List<String> input = getFileInput(fileName);
        final NoSpaceLeft noSpaceLeft = new NoSpaceLeft(input);
        final long actual = noSpaceLeft.getSizeOfAllDirectoriesAtMost(atMostSize);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("NoSpaceLeft_simple", 70_000_000, 30_000_000, 24_933_642), //
                Arguments.of("NoSpaceLeft", 70_000_000, 30_000_000, 1_300_850));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    public void secondStarTest(final String fileName, final long totalSize, final long requiredSize,
            final long expected) {
        final List<String> input = getFileInput(fileName);
        final NoSpaceLeft noSpaceLeft = new NoSpaceLeft(input);
        final long actual = noSpaceLeft.getSizeOfDirectoryToDelete(totalSize, requiredSize);
        assertEquals(expected, actual);
    }

}