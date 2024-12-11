package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class PlutonianPebblesTest extends BaseTest {

    private static Stream<Arguments> stars() {
        return Stream.of(Arguments.of("2024/PlutonianPebbles_example", 25, 55_312), //
                Arguments.of("2024/PlutonianPebbles", 25, 199986), //
                Arguments.of("2024/PlutonianPebbles", 75, 236804088748754L));
    }

    @ParameterizedTest
    @MethodSource("stars")
    void starsTest(final String fileName, final int times, final long expected) {
        final List<Integer> input = getFileInputAsInteger(fileName, "\\s");
        final PlutonianPebbles plutonianPebbles = new PlutonianPebbles();
        final long actual = plutonianPebbles.blink(input, times);
        assertEquals(expected, actual);
    }

}