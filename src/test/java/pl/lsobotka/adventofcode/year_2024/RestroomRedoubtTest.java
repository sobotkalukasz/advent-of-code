package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestroomRedoubtTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/RestroomRedoubt_example", 7, 11, 100, 12), //
                Arguments.of("2024/RestroomRedoubt", 103, 101, 100, 216772608));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, int tall, int wide, int time, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final RestroomRedoubt restroomRedoubt = new RestroomRedoubt(lines);
        final long actual = restroomRedoubt.determineSafetyFactor(tall, wide, time);
        assertEquals(expected, actual);
    }
}
