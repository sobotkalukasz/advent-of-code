package pl.lsobotka.adventofcode;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarePackageTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("CarePackage", 344));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void countBlockTilesTest(String fileName, int expected) {
        long[] program = getFileInput(fileName).stream()
                .map(s -> s.split(","))
                .flatMap(Stream::of)
                .mapToLong(Long::valueOf)
                .toArray();

        final CarePackage carePackage = new CarePackage(program);
        int actual = carePackage.countBlockTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> scoreTestResourceFile() {
        return Stream.of(Arguments.of("CarePackage", 17336));
    }

    @ParameterizedTest
    @MethodSource("scoreTestResourceFile")
    public void getScoreTest(String fileName, long expected) {
        long[] program = getFileInput(fileName).stream()
                .map(s -> s.split(","))
                .flatMap(Stream::of)
                .mapToLong(Long::valueOf)
                .toArray();

        final CarePackage carePackage = new CarePackage(program);
        long actual = carePackage.getTheScore();
        assertEquals(expected, actual);
    }
}
