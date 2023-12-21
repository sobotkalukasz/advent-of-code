package pl.lsobotka.adventofcode.year_2021;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrickShotTest {

    private static Stream<Arguments> testResource() {
        return Stream.of(Arguments.of("x=20..30, y=-10..-5", 45, 112),//
                Arguments.of("x=241..275, y=-75..-49", 2775, 1566));
    }

    @ParameterizedTest
    @MethodSource("testResource")
    void testResourceTest(final String target, final int expectedHighest, final int expectedHits) {

        final TrickShot trickShot = new TrickShot(target);
        final int actualHighest = trickShot.getTheHighestPosition();
        final int actualHits = trickShot.getNumberOfHits();
        assertEquals(expectedHighest, actualHighest);
        assertEquals(expectedHits, actualHits);
    }
}
