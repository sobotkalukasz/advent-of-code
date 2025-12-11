package pl.lsobotka.adventofcode.year_2025;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;
import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReactorTest extends BaseTest {

    private static Stream<Arguments> firstStar() {
        return Stream.of(Arguments.of("2025/Reactor_example", 5), //
                Arguments.of("2025/Reactor", 590));
    }

    @ParameterizedTest
    @MethodSource("firstStar")
    void firstStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Reactor reactor = new Reactor(input);
        final long actual = reactor.countPaths();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> secondStar() {
        return Stream.of(Arguments.of("2025/Reactor_example2", 2), //
                Arguments.of("2025/Reactor", 319473830844560L));
    }

    @ParameterizedTest
    @MethodSource("secondStar")
    void secondStarTest(final String fileName, final long expected) {
        final List<String> input = getFileInput(fileName);
        final Reactor reactor = new Reactor(input);
        final long actual = reactor.countPathsDag();
        assertEquals(expected, actual);
    }

}