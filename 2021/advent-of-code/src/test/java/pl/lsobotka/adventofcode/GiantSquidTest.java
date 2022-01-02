package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GiantSquidTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("GiantSquid_example", 4512), Arguments.of("GiantSquid", 23177));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void starOneFileTest(final String fileName, final int expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final GiantSquid giantSquid = new GiantSquid(inputRows);
        final int actual = giantSquid.firstBoardWin();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFileStarTwo() {
        return Stream.of(Arguments.of("GiantSquid_example", 1924), Arguments.of("GiantSquid", 6804));
    }

    @ParameterizedTest
    @MethodSource("testResourceFileStarTwo")
    public void starTwoFileTest(final String fileName, final int expected) throws Exception {
        final List<String> inputRows = getFileInput(fileName);

        final GiantSquid giantSquid = new GiantSquid(inputRows);
        final int actual = giantSquid.lastBoardWin();
        assertEquals(expected, actual);
    }

}
