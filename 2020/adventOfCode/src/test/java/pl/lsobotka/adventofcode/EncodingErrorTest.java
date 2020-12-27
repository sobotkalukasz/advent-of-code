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

public class EncodingErrorTest {

    private static Stream<Arguments> numbers(){
        return Stream.of(
                Arguments.of("src/test/resources/EncodingError", 25, 1038347917)
        );
    }

    @ParameterizedTest
    @MethodSource("numbers")
    public void encodingErrorTest(String path, int preambleSize, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<Long> input = bufferedReader.lines().map(Long::valueOf).collect(Collectors.toList());
        bufferedReader.close();

        EncodingError encodingError = new EncodingError();
        long invalidNumber = encodingError.findInvalidNumber(input, preambleSize);
        assertEquals(expected, invalidNumber);
    }

    private static Stream<Arguments> numbersSum(){
        return Stream.of(
                Arguments.of("src/test/resources/EncodingError", 25, 137394018)
        );
    }

    @ParameterizedTest
    @MethodSource("numbersSum")
    public void encodingErrorSumTest(String path, int preambleSize, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<Long> input = bufferedReader.lines().map(Long::valueOf).collect(Collectors.toList());
        bufferedReader.close();

        EncodingError encodingError = new EncodingError();
        List<Long> sumOfInvalidNumber = encodingError.findSumOfInvalidNumber(input, preambleSize);
        long min = sumOfInvalidNumber.stream().min(Long::compareTo).orElse(0L);
        long max = sumOfInvalidNumber.stream().max(Long::compareTo).orElse(0L);

        assertEquals(expected, min+max);
    }
}
