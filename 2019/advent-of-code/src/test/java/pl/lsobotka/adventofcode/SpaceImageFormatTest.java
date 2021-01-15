package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceImageFormatTest extends BaseTest {

    private static Stream<Arguments> imageCodeFile() {
        return Stream.of(
                Arguments.of("SpaceImageFormat", 25, 6, 2193L)
        );
    }

    @ParameterizedTest
    @MethodSource("imageCodeFile")
    public void imageCodeFileTest(String fileName, int wide, int tall, long expected) throws Exception {
        List<String> input = getFileInput(fileName);
        SpaceImageFormat imageFormat = new SpaceImageFormat(input.get(0), wide, tall);
        long actual = imageFormat.getLayerCode();
        assertEquals(expected, actual);
    }
}
