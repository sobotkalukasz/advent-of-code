package pl.lsobotka.adventofcode;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OxygenSystemTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("OxygenSystem", 424));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void getLessMoveToOxygen(String fileName, int expected) throws Exception {
        long[] program = getFileInput(fileName).stream()
                .map(s -> s.split(","))
                .flatMap(Stream::of)
                .mapToLong(Long::valueOf)
                .toArray();

        final OxygenSystem oxygenSystem = new OxygenSystem(program);
        int actual = oxygenSystem.drawMapAndFindOxygen();
        assertEquals(expected, actual);
    }
}
