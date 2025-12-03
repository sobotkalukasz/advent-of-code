package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LobbyTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/Lobby_example", 2, 357), //
                Arguments.of("2025/Lobby", 2, 17452), //
                Arguments.of("2025/Lobby_example", 12, 3121910778619L), //
                Arguments.of("2025/Lobby", 12, 173300819005913L));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final int digits, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Lobby lobby = new Lobby(input);
        final long actual = lobby.sumOutput(digits);
        assertEquals(expected, actual);
    }
}