package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanternfishTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(Arrays.asList(3, 4, 3, 1, 2), 18, 26),
                Arguments.of(Arrays.asList(3, 4, 3, 1, 2), 80, 5934),
                Arguments.of(Arrays.asList(3, 4, 3, 1, 2), 256, 26984457539L));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    public void simpleExampleTest(final List<Integer> fishAge, final int days, final long expected) {
        final Lanternfish lanternfish = new Lanternfish(fishAge);
        final long actual = lanternfish.countFishAfterDays(days);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("Lanternfish", 80, 395627), Arguments.of("Lanternfish", 256, 1767323539209L));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void resourceFileTest(final String fileName, final int days, final long expected) throws Exception {
        final List<Integer> fishAge = getFileInput(fileName).stream()
                .reduce(String::concat)
                .map(input -> input.split(","))
                .map(arr -> Arrays.stream(arr).map(Integer::valueOf).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        final Lanternfish lanternfish = new Lanternfish(fishAge);
        final long actual = lanternfish.countFishAfterDays(days);
        assertEquals(expected, actual);
    }

}
