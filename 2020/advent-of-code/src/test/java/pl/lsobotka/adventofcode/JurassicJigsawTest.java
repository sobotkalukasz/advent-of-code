package pl.lsobotka.adventofcode;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JurassicJigsawTest extends BaseTest {

    private static Stream<Arguments> jigsawFile() {
        return Stream.of(
                Arguments.of("JurassicJigsaw_example", 20899048083289L),
                Arguments.of("JurassicJigsaw", 66020135789767L)
        );
    }

    @ParameterizedTest
    @MethodSource("jigsawFile")
    public void jigsawFileTest(String fileName, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.getCornerCode();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> roughWater() {
        return Stream.of(
                Arguments.of("JurassicJigsaw", 1537),
                Arguments.of("JurassicJigsaw_example", 273)
        );
    }

    @ParameterizedTest
    @MethodSource("roughWater")
    public void roughWaterTest(String fileName, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.roughWater();
        assertEquals(expected, actual);
    }
}

