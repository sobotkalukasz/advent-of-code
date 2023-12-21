package pl.lsobotka.adventofcode.year_2019;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecureContainerTest {

    private static Stream<Arguments> passRange() {
        return Stream.of(Arguments.of(372037, 905157, 481));
    }

    @ParameterizedTest
    @MethodSource("passRange")
    void passRangeTest(int min, int max, int expected) {
        SecureContainer container = new SecureContainer();
        long actual = container.countPasswords(min, max);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> passRangeExtended() {
        return Stream.of(Arguments.of(372037, 905157, 299));
    }

    @ParameterizedTest
    @MethodSource("passRangeExtended")
    void passRangeExtendedTest(int min, int max, int expected) {
        SecureContainer container = new SecureContainer();
        long actual = container.countPasswordsExtendedRule(min, max);
        assertEquals(expected, actual);
    }

}
