package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomCustomsTest extends BaseTest {

    private static Stream<Arguments> countUniqueAnswers() {
        return Stream.of(
                Arguments.of("CustomCustoms", 6809)
        );
    }

    @ParameterizedTest
    @MethodSource("countUniqueAnswers")
    public void uniqueAnswersTest(String fileName, int expectedUniqueAnswers) throws Exception {
        List<String> input = getFileInput(fileName);

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> uniqueAnswers = customCustoms.mapRawDataAnswers(input, CustomCustoms.UNIQUE);
        long uniqueCount = customCustoms.countAllAnswers(uniqueAnswers);

        assertEquals(expectedUniqueAnswers, uniqueCount);

    }

    private static Stream<Arguments> countSameAnswers() {
        return Stream.of(
                Arguments.of("CustomCustoms", 3394)
        );
    }

    @ParameterizedTest
    @MethodSource("countSameAnswers")
    public void sameAnswersTest(String fileName, int expectedSameAnswers) throws Exception {
        List<String> input = getFileInput(fileName);

        CustomCustoms customCustoms = new CustomCustoms();
        List<List<Character>> sameAnswers = customCustoms.mapRawDataAnswers(input, CustomCustoms.SAME);
        long sameCount = customCustoms.countAllAnswers(sameAnswers);

        assertEquals(expectedSameAnswers, sameCount);

    }


}
