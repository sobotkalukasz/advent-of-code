package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShuttleSearchTest extends BaseTest {

    private static Stream<Arguments> simpleBusses() {
        return Stream.of(
                Arguments.of(939, new ArrayList<>(Arrays.asList("7", "13", "x", "x", "59", "x", "31", "19")), 295));
    }

    @ParameterizedTest
    @MethodSource("simpleBusses")
    void simpleBussesTest(int timestamp, List<String> data, int expected) {

        ShuttleSearch search = new ShuttleSearch();
        List<Integer> ints = data.stream()
                .filter(s -> !s.equals("x"))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        int actual = search.find(timestamp, ints);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleBussesFile() {
        return Stream.of(Arguments.of("2020/ShuttleSearch", 2238));
    }

    @ParameterizedTest
    @MethodSource("simpleBussesFile")
    void simpleBussesFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);
        int timestamp = Integer.parseInt(input.get(0));
        List<Integer> ints = Stream.of(input.get(1).split(","))
                .filter(s -> !s.equals("x"))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        ShuttleSearch search = new ShuttleSearch();
        int actual = search.find(timestamp, ints);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> findTimestamp() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("7", "13", "x", "x", "59", "x", "31", "19")), 1068781));
    }

    @ParameterizedTest
    @MethodSource("findTimestamp")
    void findTimestampTest(List<String> data, long expected) {

        ShuttleSearch search = new ShuttleSearch();
        long actual = search.findTimestamp(data);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> findTimestampFile() {
        return Stream.of(Arguments.of("2020/ShuttleSearch", 560214575859998L));
    }

    @ParameterizedTest
    @MethodSource("findTimestampFile")
    void findTimestampFileTest(String fileName, long expected) {
        List<String> input = getFileInput(fileName);
        List<String> buses = Stream.of(input.get(1).split(",")).collect(Collectors.toList());

        ShuttleSearch search = new ShuttleSearch();
        long actual = search.findTimestamp(buses);
        assertEquals(expected, actual);
    }
}
