package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/8
 * */
public class ResonantCollinearity {

    private final Map<Character, List<Coord>> antenaMap;
    private final Coord mapCorner;

    public ResonantCollinearity(final List<String> lines) {
        antenaMap = new HashMap<>();
        Coord corner = null;
        for (int row = 0; row < lines.size(); row++) {
            final String rowStr = lines.get(row);
            for (int col = 0; col < rowStr.length(); col++) {
                final char ch = rowStr.charAt(col);
                if (ch != '.') {
                    antenaMap.computeIfAbsent(ch, key -> new ArrayList<>()).add(Coord.of(row, col));
                }
                corner = Coord.of(row, col);
            }
        }
        this.mapCorner = corner;
    }

    long antinodeLocations() {
        return calculate(this::calculateAntinodes).size();
    }

    long resonantAntinodeLocations() {
        return calculate(this::resonantAntinodes).size();
    }

    private Set<Coord> calculate(final BiFunction<Coord, Coord, Set<Coord>> calculateAntinodeFunction) {
        final Set<Coord> antinodes = new HashSet<>();
        for (List<Coord> coords : antenaMap.values()) {
            for (int i = 0; i < coords.size(); i++) {
                for (int j = i + 1; j < coords.size(); j++) {
                    final Coord actual = coords.get(i);
                    final Coord next = coords.get(j);
                    antinodes.addAll(calculateAntinodeFunction.apply(actual, next));
                    antinodes.addAll(calculateAntinodeFunction.apply(next, actual));
                }
            }
        }
        return antinodes;
    }

    private Set<Coord> calculateAntinodes(final Coord actual, final Coord next) {
        final Set<Coord> antinodes = new HashSet<>();
        final Coord diff = next.calculateDiff(actual);
        final Coord antinode = next.moveBy(diff);

        if (isOnMap(antinode)) {
            antinodes.add(antinode);
        }
        return antinodes;
    }

    private Set<Coord> resonantAntinodes(final Coord actual, final Coord next) {
        final Set<Coord> antinodes = new HashSet<>();
        final Coord diff = next.calculateDiff(actual);

        Coord antinode = next;
        while (isOnMap(antinode)) {
            antinodes.add(antinode);
            antinode = antinode.moveBy(diff);
        }

        return antinodes;
    }

    private boolean isOnMap(final Coord pos) {
        return pos.row() >= 0 && pos.row() <= mapCorner.row() && pos.col() >= 0 && pos.col() <= mapCorner.col();
    }
}
