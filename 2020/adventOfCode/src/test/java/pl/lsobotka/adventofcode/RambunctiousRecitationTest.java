package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RambunctiousRecitationTest {

    private static Stream<Arguments> recitationData() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList(0L, 3L, 6L)), 2020L, 436L),
                Arguments.of(new ArrayList<>(Arrays.asList(1L, 3L, 2L)), 2020L, 1L),
                Arguments.of(new ArrayList<>(Arrays.asList(2L, 1L, 3L)), 2020L, 10L),
                Arguments.of(new ArrayList<>(Arrays.asList(1L, 2L, 3L)), 2020L, 27L),
                Arguments.of(new ArrayList<>(Arrays.asList(2L, 3L, 1L)), 2020L, 78L),
                Arguments.of(new ArrayList<>(Arrays.asList(3L, 2L, 1L)), 2020L, 438L),
                Arguments.of(new ArrayList<>(Arrays.asList(3L, 1L, 2L)), 2020L, 1836L),
                Arguments.of(new ArrayList<>(Arrays.asList(7L, 12L, 1L, 0L, 16L, 2L)), 2020L, 410L),
                Arguments.of(new ArrayList<>(Arrays.asList(0L, 3L, 6L)), 30_000_000L, 175_594L),
                Arguments.of(new ArrayList<>(Arrays.asList(1L, 3L, 2L)), 30_000_000L, 2578L),
                Arguments.of(new ArrayList<>(Arrays.asList(2L, 1L, 3L)), 30_000_000L, 3_544_142L),
                Arguments.of(new ArrayList<>(Arrays.asList(1L, 2L, 3L)), 30_000_000L, 261_214L),
                Arguments.of(new ArrayList<>(Arrays.asList(2L, 3L, 1L)), 30_000_000L, 6_895_259L),
                Arguments.of(new ArrayList<>(Arrays.asList(3L, 2L, 1L)), 30_000_000L, 18L),
                Arguments.of(new ArrayList<>(Arrays.asList(3L, 1L, 2L)), 30_000_000L, 362L),
                Arguments.of(new ArrayList<>(Arrays.asList(7L, 12L, 1L, 0L, 16L, 2L)), 30_000_000L, 238L)
        );
    }

    @ParameterizedTest
    @MethodSource("recitationData")
    public void recitationDataTest(List<Long> data, long find, long expected) {

        RambunctiousRecitation rr = new RambunctiousRecitation();
        long actual = rr.findNumber(find, data);
        assertEquals(expected, actual);
    }
}
