package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandyHaversacksTest {

    private static Stream<Arguments> bagCanBeHoldBy(){
        return Stream.of(
                Arguments.of("src/test/resources/HandyHaversacks", "shiny gold", 205)
        );
    }

    @ParameterizedTest
    @MethodSource("bagCanBeHoldBy")
    public void bagCanBeHoldByTest(String path, String bagType, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(rawData);
        long uniqueCount = handyHaversacks.countHowManyCanContainType(bagType, bagRules);
        assertEquals(expected, uniqueCount);
    }

    private static Stream<Arguments> bagCanHold(){
        return Stream.of(
                Arguments.of("src/test/resources/HandyHaversacks", "shiny gold", 80902)
        );
    }

    @ParameterizedTest
    @MethodSource("bagCanHold")
    public void bagCanHoldTest(String path, String bagType, int expected) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> rawData = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        HandyHaversacks handyHaversacks = new HandyHaversacks();
        Set<HandyHaversacks.Bag> bagRules = handyHaversacks.createBagFromRules(rawData);
        long insideCount = handyHaversacks.countRequiredInside(bagType, bagRules);
        assertEquals(expected, insideCount);
    }
}
