package pl.lsobotka.adventofcode.smokebasin;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SmokeBasinTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("SmokeBasin_example", 15), Arguments.of("SmokeBasin", 545));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final SmokeBasin smokeBasin = new SmokeBasin(input);
        final long actual = smokeBasin.getRiskLevel();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceLargeBasinFile() {
        return Stream.of(Arguments.of("SmokeBasin_example", 3, 1134), Arguments.of("SmokeBasin", 3, 950600));
    }

    @ParameterizedTest
    @MethodSource("testResourceLargeBasinFile")
    public void testResourceLargeBasinFileTest(final String fileName, final int basinToCount, final int expected)
            throws Exception {
        final List<String> input = getFileInput(fileName);

        final SmokeBasin smokeBasin = new SmokeBasin(input);
        final long actual = smokeBasin.getSizeOfLargestBasins(basinToCount);
        assertEquals(expected, actual);
    }
}
