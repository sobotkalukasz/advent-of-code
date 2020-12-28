package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandyHaversacksTest extends BaseTest {

    private static Stream<Arguments> bagCanBeHoldBy() {
        return Stream.of(
                Arguments.of("HandyHaversacks", "shiny gold", 205)
        );
    }

    @ParameterizedTest
    @MethodSource("bagCanBeHoldBy")
    public void bagCanBeHoldByTest(String fileName, String bagType, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(input);
        long uniqueCount = handyHaversacks.countHowManyCanContainType(bagType, bagRules);
        assertEquals(expected, uniqueCount);
    }

    private static Stream<Arguments> bagCanHold() {
        return Stream.of(
                Arguments.of("HandyHaversacks", "shiny gold", 80902)
        );
    }

    @ParameterizedTest
    @MethodSource("bagCanHold")
    public void bagCanHoldTest(String fileName, String bagType, int expected) throws Exception {
        List<String> input = getFileInput(fileName);

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(input);
        long insideCount = handyHaversacks.countRequiredInside(bagType, bagRules);
        assertEquals(expected, insideCount);
    }
}
