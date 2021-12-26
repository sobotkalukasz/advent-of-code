package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrenchMapTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("TrenchMap_example", 2, 35), Arguments.of("TrenchMap", 2, 5391),
                Arguments.of("TrenchMap_example", 50, 3351), Arguments.of("TrenchMap", 50, 16383));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int times, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final TrenchMap trenchMap = new TrenchMap(input);
        final int actual = trenchMap.enhancePictureAndCountLightPixels(times);

        assertEquals(expected, actual);
    }
}
