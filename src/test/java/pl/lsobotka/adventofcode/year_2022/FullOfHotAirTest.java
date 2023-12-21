package pl.lsobotka.adventofcode.year_2022;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class FullOfHotAirTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2022/FullOfHotAir_simple", "2=-1=0"), //
                Arguments.of("2022/FullOfHotAir", "2-2=21=0021=-02-1=-0"));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    public void firstStarTest(final String fileName, final String expected) {
        final List<String> input = getFileInput(fileName);
        final FullOfHotAir hotAir = new FullOfHotAir(input);
        final String actual = hotAir.sumSnafu();
        assertEquals(expected, actual);
    }

}