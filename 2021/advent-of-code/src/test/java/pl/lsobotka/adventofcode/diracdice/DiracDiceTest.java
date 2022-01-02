package pl.lsobotka.adventofcode.diracdice;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiracDiceTest {

    private static Stream<Arguments> testResource() {
        return Stream.of(Arguments.of(4, 8, 739785), Arguments.of(4, 7, 893700));
    }

    @ParameterizedTest
    @MethodSource("testResource")
    public void testResourceTest(final int firstPos, final int secondPos, final long expected) {

        final long actual = DiracDice.playSimpleGame(firstPos, secondPos);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> quantumResource() {
        return Stream.of(Arguments.of(4, 8, 444356092776315L), Arguments.of(4, 7, 568867175661958L));
    }

    @ParameterizedTest
    @MethodSource("quantumResource")
    public void quantumResourceTest(final int firstPos, final int secondPos, final long expected) {

        final long actual = DiracDice.playQuantumDice(firstPos, secondPos);
        assertEquals(expected, actual);
    }
}
