package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/3
 * */
public class GearRatios {

    private final Schema schema;

    public GearRatios(List<String> input) {
        schema = Schema.of(input);
    }

    int sumOfEnginePartNumbers() {
        return schema.enginePartNumbers().stream().mapToInt(Integer::intValue).sum();
    }

    int sumOfGearRatio() {
        return schema.gears().stream().mapToInt(Gear::getRatio).sum();
    }

    record Schema(Map<Coord, Character> schemaMap, List<Digit> digits, List<Gear> gears) {
        static Schema of(final List<String> input) {
            final Map<Coord, Character> schemaMap = new HashMap<>();

            for (int row = 0; row < input.size(); row++) {
                final char[] charRow = input.get(row).toCharArray();
                for (int col = 0; col < charRow.length; col++) {
                    if (charRow[col] != '.') {
                        schemaMap.put(Coord.of(row, col), charRow[col]);
                    }
                }
            }
            final List<Digit> digits = getDigits(schemaMap);
            final List<Gear> gears = Gear.of(schemaMap, digits);
            return new Schema(schemaMap, digits, gears);
        }

        public List<Integer> enginePartNumbers() {
            return digits.stream()
                    .filter(d -> d.isNearOtherSymbol(schemaMap))
                    .map(Digit::digit)
                    .collect(Collectors.toList());
        }

        private static List<Digit> getDigits(final Map<Coord, Character> schemaMap) {
            return schemaMap.entrySet()
                    .stream()
                    .filter(e -> Character.isDigit(e.getValue()))
                    .filter(e -> isFirstDigit(e.getKey(), schemaMap))
                    .map(e -> Digit.of(e.getKey(), schemaMap))
                    .toList();
        }

        private static boolean isFirstDigit(Coord coord, Map<Coord, Character> schemaMap) {
            final Character previous = schemaMap.get(coord.getAdjacent(Dir.LEFT));
            return Objects.isNull(previous) || !Character.isDigit(previous);
        }
    }

    record Digit(int digit, List<Coord> coords) {

        static Digit of(final Coord start, final Map<Coord, Character> schemaMap) {

            final StringBuilder digitString = new StringBuilder();
            List<Coord> coords = new ArrayList<>();

            Coord current = start;
            do {
                coords.add(current);
                digitString.append(schemaMap.get(current));
                current = current.getAdjacent(Dir.RIGHT);
            } while (schemaMap.containsKey(current) && Character.isDigit(schemaMap.get(current)));

            return new Digit(Integer.parseInt(digitString.toString()), coords);
        }

        public boolean isNearOtherSymbol(final Map<Coord, Character> schemaMap) {
            for (Coord coord : getAllAdjacent()) {
                if (schemaMap.containsKey(coord) && !Character.isDigit(schemaMap.get(coord))) {
                    return true;
                }
            }
            return false;
        }

        private Set<Coord> getAllAdjacent() {
            final Set<Coord> adjacent = coords.stream()
                    .map(Coord::getAllAdjacent)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
            coords.forEach(adjacent::remove);
            return adjacent;
        }
    }

    record Gear(Coord coord, List<Digit> digits) {

        static List<Gear> of(Map<Coord, Character> schemaMap, List<Digit> digits) {
            List<Gear> gears = new ArrayList<>();
            schemaMap.entrySet().stream().filter(e -> e.getValue().equals('*')).forEach(e -> {
                final Set<Coord> gearAdjacent = e.getKey().getAllAdjacent();
                final List<Digit> gearDigits = digits.stream().filter(d -> {
                    final Set<Coord> digitCoords = new HashSet<>(d.coords());
                    digitCoords.retainAll(gearAdjacent);
                    return !digitCoords.isEmpty();
                }).toList();

                if (gearDigits.size() > 1) {
                    gears.add(new Gear(e.getKey(), gearDigits));
                }
            });

            return gears;
        }

        public int getRatio() {
            return digits.stream().map(Digit::digit).reduce(1, (a, b) -> a * b);
        }
    }
}

