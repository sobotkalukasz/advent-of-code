package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComboBreakerTest {

    private static Stream<Arguments> encryption() {
        return Stream.of(
                Arguments.of(5764801, 17807724, 14897079L),
                Arguments.of(10705932, 12301431, 11328376L)
        );
    }

    @ParameterizedTest
    @MethodSource("encryption")
    void encryptionTest(int cardKey, int doorKey, long expected) {
        ComboBreaker breaker = new ComboBreaker();
        long actual = breaker.getEncryptionKey(cardKey, doorKey);
        assertEquals(expected, actual);
    }
}
