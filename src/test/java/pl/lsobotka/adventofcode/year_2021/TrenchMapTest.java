package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrenchMapTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/TrenchMap_example", 2, 35), //
                Arguments.of("2021/TrenchMap", 2, 5391),//
                Arguments.of("2021/TrenchMap_example", 50, 3351), //
                Arguments.of("2021/TrenchMap", 50, 16383));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final int times, final int expected) {
        final List<String> input = getFileInput(fileName);

        final TrenchMap trenchMap = new TrenchMap(input);
        final int actual = trenchMap.enhancePictureAndCountLightPixels(times);

        assertEquals(expected, actual);
    }
}
