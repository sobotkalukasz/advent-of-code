package pl.lsobotka.adventofcode.year_2021;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AluTest extends BaseTest {

    private static Stream<Arguments> biggestTestResourceFile() {
        return Stream.of(Arguments.of("2021/Alu", 99299513899971L));
    }

    @ParameterizedTest
    @MethodSource("biggestTestResourceFile")
    void biggestTestResourceFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Alu alu = new Alu(input);
        final long actual = alu.findBiggestNumber();

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> lowestTestResourceFile() {
        return Stream.of(Arguments.of("2021/Alu", 93185111127911L));
    }

    @ParameterizedTest
    @MethodSource("lowestTestResourceFile")
    void lowestTestResourceFileTest(final String fileName, final long expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final Alu alu = new Alu(input);
        final long actual = alu.findLowestNumber();

        assertEquals(expected, actual);
    }

}
