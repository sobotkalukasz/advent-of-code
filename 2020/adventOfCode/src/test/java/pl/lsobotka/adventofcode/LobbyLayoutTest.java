package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyLayoutTest {

    private static Stream<Arguments> tiles() {
        return Stream.of(
                Arguments.of(Arrays.asList("sesenwnenenewseeswwswswwnenewsewsw",
                        "neeenesenwnwwswnenewnwwsewnenwseswesw",
                        "seswneswswsenwwnwse",
                        "nwnwneseeswswnenewneswwnewseswneseene",
                        "swweswneswnenwsewnwneneseenw",
                        "eesenwseswswnenwswnwnwsewwnwsene",
                        "sewnenenenesenwsewnenwwwse",
                        "wenwwweseeeweswwwnwwe",
                        "wsweesenenewnwwnwsenewsenwwsesesenwne",
                        "neeswseenwwswnwswswnw",
                        "nenwswwsewswnenenewsenwsenwnesesenew",
                        "enewnwewneswsewnwswenweswnenwsenwsw",
                        "sweneswneswneneenwnewenewwneswswnese",
                        "swwesenesewenwneswnwwneseswwne",
                        "enesenwswwswneneswsenwnewswseenwsese",
                        "wnwnesenesenenwwnenwsewesewsesesew",
                        "nenewswnwewswnenesenwnesewesw",
                        "eneswnwswnwsenenwnwnwwseeswneewsenese",
                        "neswnwewnwnwseenwseesewsenwsweewe",
                        "wseweeenwnesenwwwswnew"), 10)
        );
    }

    @ParameterizedTest
    @MethodSource("tiles")
    public void tilesTest(List<String> input, int expected) {
        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesFile() {
        return Stream.of(
                Arguments.of("src/test/resources/LobbyLayout", 450)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesFile")
    public void tilesFileTest(String path, int expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.flipTiles();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDay() {
        return Stream.of(
                Arguments.of(Arrays.asList("sesenwnenenewseeswwswswwnenewsewsw",
                        "neeenesenwnwwswnenewnwwsewnenwseswesw",
                        "seswneswswsenwwnwse",
                        "nwnwneseeswswnenewneswwnewseswneseene",
                        "swweswneswnenwsewnwneneseenw",
                        "eesenwseswswnenwswnwnwsewwnwsene",
                        "sewnenenenesenwsewnenwwwse",
                        "wenwwweseeeweswwwnwwe",
                        "wsweesenenewnwwnwsenewsenwwsesesenwne",
                        "neeswseenwwswnwswswnw",
                        "nenwswwsewswnenenewsenwsenwnesesenew",
                        "enewnwewneswsewnwswenweswnenwsenwsw",
                        "sweneswneswneneenwnewenewwneswswnese",
                        "swwesenesewenwneswnwwneseswwne",
                        "enesenwswwswneneswsenwnewswseenwsese",
                        "wnwnesenesenenwwnenwsewesewsesesew",
                        "nenewswnwewswnenesenwnesewesw",
                        "eneswnwswnwsenenwnwnwwseeswneewsenese",
                        "neswnwewnwnwseenwseesewsenwsweewe",
                        "wseweeenwnesenwwwswnew"), 100, 2208L)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesDay")
    public void tilesDayTest(List<String> input, int days, long expected) {
        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> tilesDayFile() {
        return Stream.of(
                Arguments.of("src/test/resources/LobbyLayout", 100, 4059L)
        );
    }

    @ParameterizedTest
    @MethodSource("tilesDayFile")
    public void tilesDayFileTest(String path, int days, long expected) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        LobbyLayout layout = new LobbyLayout(input);
        long actual = layout.applyDayRule(days);
        assertEquals(expected, actual);
    }
}
