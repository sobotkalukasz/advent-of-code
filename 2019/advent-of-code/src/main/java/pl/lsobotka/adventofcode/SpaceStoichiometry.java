package pl.lsobotka.adventofcode;

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

    public long determineAmountOfOre() {
        final Map<Chemical, Long> reaction = new HashMap<>();
        final Chemical fuel = getChemicalByType(FUEL);
        completeReaction(reaction, fuel, fuel.qty());

        final List<Chemical> chemicalsFromOre = reaction.entrySet()
                .stream()
                .filter(e -> instructions.get(e.getKey()).stream().anyMatch(c -> c.type().equals(ORE)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return chemicalsFromOre.stream().mapToLong(c -> getOreForChemical(c.type(), reaction.get(c))).sum();
    }

    private Chemical getChemicalByType(final String type) {
        return instructions.keySet()
                .stream()
                .filter(c -> c.type().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void completeReaction(final Map<Chemical, Long> reaction, final Chemical chemical, final long multiply) {
        if (chemical.type.equals(ORE)) {
            return;
        }

        if (Objects.isNull(reaction.computeIfPresent(chemical, (k, v) -> v = Long.sum(v, chemical.qty() * multiply)))) {
            reaction.put(chemical, chemical.qty() * multiply);
        }
        final Chemical chemicalByType = getChemicalByType(chemical.type());
        final long nextLevelMultiplier = ((chemical.qty() * multiply) / chemicalByType.qty()) + (
                (chemical.qty() * multiply) % chemicalByType.qty() == 0 ? 0 : 1);

        this.instructions.get(chemical).forEach(chem -> completeReaction(reaction, chem, nextLevelMultiplier));
    }

    private long getOreForChemical(final String type, final long min) {
        final Chemical chemical = getChemicalByType(type);
        final long qty = chemical.qty();
        final long multiply = (min / qty) + (min % qty == 0 ? 0 : 1);
        final long oreQty = instructions.get(chemical)
                .stream()
                .filter(c -> c.type().equals(ORE))
                .map(Chemical::qty)
                .findFirst()
                .orElse(0L);
        return oreQty * multiply;
    }

    record Chemical(String type, long qty) {

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
