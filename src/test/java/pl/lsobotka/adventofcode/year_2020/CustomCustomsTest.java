package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomCustomsTest extends BaseTest {

    private static Stream<Arguments> countUniqueAnswers() {
        return Stream.of(Arguments.of("2020/CustomCustoms", 6809));
    }

    @ParameterizedTest
    @MethodSource("countUniqueAnswers")
    void uniqueAnswersTest(String fileName, int expectedUniqueAnswers) {
        List<String> input = getFileInput(fileName);

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> uniqueAnswers = customCustoms.mapRawDataAnswers(input, CustomCustoms.UNIQUE);
        long uniqueCount = customCustoms.countAllAnswers(uniqueAnswers);

        assertEquals(expectedUniqueAnswers, uniqueCount);

    }

    private static Stream<Arguments> countSameAnswers() {
        return Stream.of(Arguments.of("2020/CustomCustoms", 3394));
    }

    @ParameterizedTest
    @MethodSource("countSameAnswers")
    void sameAnswersTest(String fileName, int expectedSameAnswers) {
        List<String> input = getFileInput(fileName);

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> sameAnswers = customCustoms.mapRawDataAnswers(input, CustomCustoms.SAME);
        long sameCount = customCustoms.countAllAnswers(sameAnswers);

        assertEquals(expectedSameAnswers, sameCount);

    }

}
