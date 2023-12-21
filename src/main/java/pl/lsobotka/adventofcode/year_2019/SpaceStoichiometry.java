package pl.lsobotka.adventofcode.year_2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2019/day/14
 * */
public class SpaceStoichiometry {

    final private static String FUEL = "FUEL";
    final private static String ORE = "ORE";

    final private Map<Chemical, List<Chemical>> instructions;

    public SpaceStoichiometry(List<String> rawInput) {
        this.instructions = new HashMap<>();

        rawInput.forEach(row -> {
            final String[] recipeSplit = row.split("=>");
            final List<Chemical> input = Arrays.stream(recipeSplit[0].split(","))
                    .map(Chemical::init)
                    .collect(Collectors.toList());
            final Chemical output = Chemical.init(recipeSplit[1]);
            this.instructions.put(output, input);
        });
    }

    public long determineMinimalOreToProduce(final long fuelCount) {
        final Chemical fuel = getChemicalByType(FUEL);
        final HashMap<String, Long> leftover = new HashMap<>();
        return instructions.get(fuel).stream().map(c -> countOre(c, leftover, fuelCount)).reduce(Long::sum).orElse(0L);
    }

    public long determineMaxFuelFromOre(final long totalOre) {
        long min = 1;
        long max = totalOre;

        while (min <= max) {
            final long mid = (max - min) / 2 + min;
            final long oreCount = determineMinimalOreToProduce(mid);
            if (oreCount < totalOre) {
                min = mid + 1;
            } else if (oreCount > totalOre) {
                max = mid - 1;
            } else {
                min = max = mid + 1;
            }
        }

        return min - 1;
    }

    private Chemical getChemicalByType(final String type) {
        return instructions.keySet()
                .stream()
                .filter(c -> c.type().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private long countOre(final Chemical chemical, final Map<String, Long> leftovers, final long toProduce) {
        if (chemical.type.equals(ORE)) {
            return toProduce * chemical.required();
        } else {
            final Chemical chemicalToProduce = getChemicalByType(chemical.type());
            final long required = toProduce * chemical.required() - leftovers.getOrDefault(chemical.type(), 0L);
            final long multiply = chemicalToProduce.required();
            final long create = (long) Math.ceil(required / (double) multiply);
            final long leftover = multiply * create - required;
            leftovers.compute(chemical.type(), (key, oldValue) -> leftover == 0 ? null : leftover);
            return instructions.get(chemical)
                    .stream()
                    .map(c -> countOre(c, leftovers, create))
                    .reduce(Long::sum)
                    .orElse(0L);
        }
    }

    record Chemical(String type, long required) {

        public static Chemical init(final String rawInput) {
            final String[] split = rawInput.trim().split(" ");
            final int qty = Integer.parseInt(split[0]);
            final String type = split[1];
            return new Chemical(type, qty);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Chemical that = (Chemical) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
