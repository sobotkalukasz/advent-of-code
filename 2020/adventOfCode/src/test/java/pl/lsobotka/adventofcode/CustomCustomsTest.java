package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomCustomsTest {

    private static Stream<Arguments> countUniqueAnswers(){
        return Stream.of(
                Arguments.of("src/test/resources/CustomCustoms", 6809)
        );
    }

    @ParameterizedTest
    @MethodSource("countUniqueAnswers")
    public void uniqueAnswersTest(String path, int expectedUniqueAnswers) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> uniqueAnswers = customCustoms.mapRawDataAnswers(rawData, CustomCustoms.UNIQUE);
        long uniqueCount = customCustoms.countAllAnswers(uniqueAnswers);

        assertEquals(expectedUniqueAnswers, uniqueCount);

    }

    private static Stream<Arguments> countSameAnswers(){
        return Stream.of(
                Arguments.of("src/test/resources/CustomCustoms", 3394)
        );
    }

    @ParameterizedTest
    @MethodSource("countSameAnswers")
    public void sameAnswersTest(String path, int expectedSameAnswers) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> sameAnswers = customCustoms.mapRawDataAnswers(rawData, CustomCustoms.SAME);
        long sameCount = customCustoms.countAllAnswers(sameAnswers);

        assertEquals(expectedSameAnswers, sameCount);

    }


}
