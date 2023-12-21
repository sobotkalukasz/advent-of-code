package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdapterArrayTest extends BaseTest {

    private static Stream<Arguments> array() {
        return Stream.of(Arguments.of("2020/AdapterArray", 2240));
    }

    @ParameterizedTest
    @MethodSource("array")
    void adapterArrayTest(String fileName, int expected) {
        List<Integer> input = getFileInput(fileName).stream().map(Integer::valueOf).collect(Collectors.toList());

        AdapterArray adapterArray = new AdapterArray();
        AdapterArray.JoltRecord chain = adapterArray.findChain(input);
        int actual = chain.oneJoltCount() * chain.threeJoltCount();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> possibleAdapters() {
        return Stream.of(Arguments.of(new ArrayList<>(List.of(16, 10, 15, 5, 1, 11, 7, 19, 6, 12, 4)), 8), Arguments.of(
                new ArrayList<>(
                        List.of(28, 33, 18, 42, 31, 14, 46, 20, 48, 47, 24, 23, 49, 45, 19, 38, 39, 11, 1, 32, 25, 35,
                                8, 17, 7, 9, 4, 2, 34, 10, 3)), 19208));
    }

    @ParameterizedTest
    @MethodSource("possibleAdapters")
    void possibleAdaptersTest(List<Integer> adapters, int expected) {
        AdapterArray adapterArray = new AdapterArray();
        long actual = adapterArray.countChains(adapters);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> possibleAdaptersFromFile() {
        return Stream.of(Arguments.of("2020/AdapterArray", 99214346656768L));
    }

    @ParameterizedTest
    @MethodSource("possibleAdaptersFromFile")
    void possibleAdaptersFromFileTest(String fileName, long expected) {
        List<Integer> input = getFileInput(fileName).stream().map(Integer::valueOf).collect(Collectors.toList());

        AdapterArray adapterArray = new AdapterArray();
        long actual = adapterArray.countChains(input);
        assertEquals(expected, actual);
    }
}
