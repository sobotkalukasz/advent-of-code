package pl.lsobotka.adventofcode.year_2019;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpaceImageFormatTest extends BaseTest {

    private static Stream<Arguments> imageCodeFile() {
        return Stream.of(Arguments.of("2019/SpaceImageFormat", 25, 6, 2193L));
    }

    @ParameterizedTest
    @MethodSource("imageCodeFile")
    void imageCodeFileTest(String fileName, int wide, int tall, long expected) {
        List<String> input = getFileInput(fileName);
        SpaceImageFormat imageFormat = new SpaceImageFormat(input.get(0), wide, tall);
        long actual = imageFormat.getLayerCode();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> decodeImageFile() {
        return Stream.of(Arguments.of("2019/SpaceImageFormat", 25, 6, List.of( //
                        "1   11111 1  1 1111 1111 ", //
                        "1   11    1  1 1    1    ", //
                        " 1 1 111  1111 111  111  ", //
                        "  1  1    1  1 1    1    ", //
                        "  1  1    1  1 1    1    ", //
                        "  1  1111 1  1 1111 1    "))
                //YEHEF
        );
    }

    @ParameterizedTest
    @MethodSource("decodeImageFile")
    void decodeImageFileTest(String fileName, int wide, int tall, List<String> expected) {
        List<String> input = getFileInput(fileName);
        SpaceImageFormat imageFormat = new SpaceImageFormat(input.get(0), wide, tall);
        List<String> actual = imageFormat.decodeImage();
        assertEquals(expected, actual);
    }
}
