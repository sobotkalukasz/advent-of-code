package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreacheryOfWhalesTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(Arguments.of(Arrays.asList(16, 1, 2, 0, 4, 2, 7, 1, 2, 14), 37));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    public void simpleExampleTest(final List<Integer> positions, final long expected) {
        final TreacheryOfWhales treacheryOfWhales = new TreacheryOfWhales.Simple();
        final long actual = treacheryOfWhales.getMinFuelConsumption(positions);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("TreacheryOfWhales", 328318));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void resourceFileTest(final String fileName, final long expected) throws Exception {
        final List<Integer> positions = getFileInputAsIntegerList(fileName);

        final TreacheryOfWhales treacheryOfWhales = new TreacheryOfWhales.Simple();
        final long actual = treacheryOfWhales.getMinFuelConsumption(positions);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> simpleExampleComplexCost() {
        return Stream.of(Arguments.of(Arrays.asList(16, 1, 2, 0, 4, 2, 7, 1, 2, 14), 168));
    }

    @ParameterizedTest
    @MethodSource("simpleExampleComplexCost")
    public void simpleExampleComplexCostTest(final List<Integer> positions, final long expected) {
        final TreacheryOfWhales treacheryOfWhales =  new TreacheryOfWhales.Complex();
        final long actual = treacheryOfWhales.getMinFuelConsumption(positions);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceComplexCostFile() {
        return Stream.of(Arguments.of("TreacheryOfWhales", 89791146));
    }

    @ParameterizedTest
    @MethodSource("testResourceComplexCostFile")
    public void resourceFileComplexCostTest(final String fileName, final long expected) throws Exception {
        final List<Integer> positions = getFileInputAsIntegerList(fileName);

        final TreacheryOfWhales treacheryOfWhales =  new TreacheryOfWhales.Complex();
        final long actual = treacheryOfWhales.getMinFuelConsumption(positions);
        assertEquals(expected, actual);
    }
}
