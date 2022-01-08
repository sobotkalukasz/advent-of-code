package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceStoichiometryTest extends BaseTest {

    private static Stream<Arguments> simpleExample() {
        return Stream.of(
/*                Arguments.of(
                List.of("10 ORE => 10 A", "1 ORE => 1 B", "7 A, 1 B => 1 C", "7 A, 1 C => 1 D", "7 A, 1 D => 1 E",
                        "7 A, 1 E => 1 FUEL"), 31), Arguments.of(
                List.of("9 ORE => 2 A", "8 ORE => 3 B", "7 ORE => 5 C", "3 A, 4 B => 1 AB", "5 B, 7 C => 1 BC",
                        "4 C, 1 A => 1 CA", "2 AB, 3 BC, 4 CA => 1 FUEL"), 165), Arguments.of(
                List.of("157 ORE => 5 NZVS", "165 ORE => 6 DCFZ",
                        "44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL",
                        "12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ", "179 ORE => 7 PSHF", "177 ORE => 5 HKGWZ",
                        "7 DCFZ, 7 PSHF => 2 XJWVT", "165 ORE => 2 GPVTF",
                        "3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT"), 13312),*/
                Arguments.of(
                List.of("2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG", "17 NVRVD, 3 JNWZP => 8 VPVL",
                        "53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL",
                        "22 VJHF, 37 MNCFX => 5 FWMGM", "139 ORE => 4 NVRVD", "144 ORE => 7 JNWZP",
                        "5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC",
                        "5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV", "145 ORE => 6 MNCFX", "1 NVRVD => 8 CXFTF",
                        "1 VJHF, 6 MNCFX => 4 RFSQX", "176 ORE => 6 VJHF"), 180697));
    }

    @ParameterizedTest
    @MethodSource("simpleExample")
    public void simpleExampleTest(List<String> input, long expected) {
        final SpaceStoichiometry spaceStoichiometry = new SpaceStoichiometry(input);

        spaceStoichiometry.determineAmountOfOre();

        long actual = spaceStoichiometry.determineAmountOfOre();
        assertEquals(expected, actual);
    }
}
