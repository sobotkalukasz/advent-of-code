package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandyHaversacksTest extends BaseTest {

    private static Stream<Arguments> bagCanBeHoldBy() {
        return Stream.of(Arguments.of("2020/HandyHaversacks", "shiny gold", 205));
    }

    @ParameterizedTest
    @MethodSource("bagCanBeHoldBy")
    void bagCanBeHoldByTest(String fileName, String bagType, int expected) {
        List<String> input = getFileInput(fileName);

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(input);
        long uniqueCount = handyHaversacks.countHowManyCanContainType(bagType, bagRules);
        assertEquals(expected, uniqueCount);
    }

    private static Stream<Arguments> bagCanHold() {
        return Stream.of(Arguments.of("2020/HandyHaversacks", "shiny gold", 80902));
    }

    @ParameterizedTest
    @MethodSource("bagCanHold")
    void bagCanHoldTest(String fileName, String bagType, int expected) {
        List<String> input = getFileInput(fileName);

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(input);
        long insideCount = handyHaversacks.countRequiredInside(bagType, bagRules);
        assertEquals(expected, insideCount);
    }
}
