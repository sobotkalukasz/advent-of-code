package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyLayoutTest extends BaseTest {

    private static Stream<Arguments> tiles() {
        return Stream.of(
                Arguments.of("LobbyLayout_example", 10)
        );
    }

    @ParameterizedTest
    @MethodSource("tiles")
    public void tilesTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesFile() {
        return Stream.of(
                Arguments.of("LobbyLayout", 450)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesFile")
    public void tilesFileTest(String fileName, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDay() {
        return Stream.of(
                Arguments.of("LobbyLayout_example", 100, 2208L)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesDay")
    public void tilesDayTest(String fileName, int days, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDayFile() {
        return Stream.of(
                Arguments.of("LobbyLayout", 100, 4059L)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesDayFile")
    public void tilesDayFileTest(String fileName, int days, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }
}
