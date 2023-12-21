package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LobbyLayoutTest extends BaseTest {

    private static Stream<Arguments> tiles() {
        return Stream.of(Arguments.of("2020/LobbyLayout_example", 10));
    }

    @ParameterizedTest
    @MethodSource("tiles")
    void tilesTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesFile() {
        return Stream.of(Arguments.of("2020/LobbyLayout", 450));
    }

    @ParameterizedTest
    @MethodSource("tilesFile")
    void tilesFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDay() {
        return Stream.of(Arguments.of("2020/LobbyLayout_example", 100, 2208L));
    }

    @ParameterizedTest
    @MethodSource("tilesDay")
    void tilesDayTest(String fileName, int days, long expected) {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDayFile() {
        return Stream.of(Arguments.of("2020/LobbyLayout", 100, 4059L));
    }

    @ParameterizedTest
    @MethodSource("tilesDayFile")
    void tilesDayFileTest(String fileName, int days, long expected) {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }
}
