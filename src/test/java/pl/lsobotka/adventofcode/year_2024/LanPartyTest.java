package pl.lsobotka.adventofcode.year_2024;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

class LanPartyTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2024/LanParty_example", 7), //
                Arguments.of("2024/LanParty", 1184));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> lines = getFileInput(fileName);
        final LanParty lanParty = new LanParty(lines);
        final long actual = lanParty.countConnected();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2024/LanParty_example", "co,de,ka,ta"), //
                Arguments.of("2024/LanParty", "hf,hz,lb,lm,ls,my,ps,qu,ra,uc,vi,xz,yv"));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final String expected) {
        final List<String> lines = getFileInput(fileName);
        final LanParty lanParty = new LanParty(lines);
        final String actual = lanParty.getPassword();
        assertEquals(expected, actual);
    }

}