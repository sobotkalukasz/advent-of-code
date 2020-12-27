package pl.lsobotka.adventofcode;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JurassicJigsawTest {

    private static Stream<Arguments> jigsawFile(){
        return Stream.of(
                Arguments.of("src/test/resources/JurassicJigsaw_example", 20899048083289L),
                Arguments.of("src/test/resources/JurassicJigsaw", 66020135789767L)
        );
    }

    @ParameterizedTest
    @MethodSource("jigsawFile")
    public void jigsawFileTest(String path, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.getCornerCode();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> roughWater(){
        return Stream.of(
                Arguments.of("src/test/resources/JurassicJigsaw", 1537),
                Arguments.of("src/test/resources/JurassicJigsaw_example", 273)
        );
    }

    @ParameterizedTest
    @MethodSource("roughWater")
    public void roughWaterTest(String path, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        JurassicJigsaw jigsaw = new JurassicJigsaw(input);
        long actual = jigsaw.roughWater();
        assertEquals(expected, actual);
    }
}

