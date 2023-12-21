package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JurassicJigsawTest extends BaseTest {

    private static Stream<Arguments> jigsawFile() {
        return Stream.of(Arguments.of("2020/JurassicJigsaw_example", 20899048083289L), //
                Arguments.of("2020/JurassicJigsaw", 66020135789767L));
    }

    @ParameterizedTest
    @MethodSource("jigsawFile")
    void jigsawFileTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.getCornerCode();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> roughWater() {
        return Stream.of(Arguments.of("2020/JurassicJigsaw", 1537), //
                Arguments.of("2020/JurassicJigsaw_example", 273));
    }

    @ParameterizedTest
    @MethodSource("roughWater")
    void roughWaterTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.roughWater();
        assertEquals(expected, actual);
    }
}

