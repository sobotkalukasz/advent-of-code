package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiantSquidTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/GiantSquid_example", 4512), Arguments.of("2021/GiantSquid", 23177));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void starOneFileTest(final String fileName, final int expected) {
        final List<String> inputRows = getFileInput(fileName);

        final GiantSquid giantSquid = new GiantSquid(inputRows);
        final int actual = giantSquid.firstBoardWin();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFileStarTwo() {
        return Stream.of(Arguments.of("2021/GiantSquid_example", 1924), Arguments.of("2021/GiantSquid", 6804));
    }

    @ParameterizedTest
    @MethodSource("testResourceFileStarTwo")
    void starTwoFileTest(final String fileName, final int expected) {
        final List<String> inputRows = getFileInput(fileName);

        final GiantSquid giantSquid = new GiantSquid(inputRows);
        final int actual = giantSquid.lastBoardWin();
        assertEquals(expected, actual);
    }

}
