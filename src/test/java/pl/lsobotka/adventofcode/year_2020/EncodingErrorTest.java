package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EncodingErrorTest extends BaseTest {

    private static Stream<Arguments> numbers() {
        return Stream.of(Arguments.of("2020/EncodingError", 25, 1038347917));
    }

    @ParameterizedTest
    @MethodSource("numbers")
    void encodingErrorTest(String fileName, int preambleSize, int expected) {
        List<Long> input = getFileInput(fileName).stream().map(Long::valueOf).collect(Collectors.toList());

        EncodingError encodingError = new EncodingError();
        long invalidNumber = encodingError.findInvalidNumber(input, preambleSize);
        assertEquals(expected, invalidNumber);
    }

    private static Stream<Arguments> numbersSum() {
        return Stream.of(Arguments.of("2020/EncodingError", 25, 137394018));
    }

    @ParameterizedTest
    @MethodSource("numbersSum")
    void encodingErrorSumTest(String fileName, int preambleSize, int expected) {
        List<Long> input = getFileInput(fileName).stream().map(Long::valueOf).collect(Collectors.toList());

        EncodingError encodingError = new EncodingError();
        List<Long> sumOfInvalidNumber = encodingError.findSumOfInvalidNumber(input, preambleSize);
        long min = sumOfInvalidNumber.stream().min(Long::compareTo).orElse(0L);
        long max = sumOfInvalidNumber.stream().max(Long::compareTo).orElse(0L);

        assertEquals(expected, min + max);
    }
}
