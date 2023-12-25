package pl.lsobotka.adventofcode.year_2023;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class SnowverloadTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2023/Snowverload_example", 54), //
                Arguments.of("2023/Snowverload", 554_064));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Snowverload snowverload = new Snowverload(input);
        final long actual = snowverload.disconnectThreeWires();
        assertEquals(expected, actual);
    }

}