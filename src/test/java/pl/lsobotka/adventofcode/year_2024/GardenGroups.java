package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;

public class GardenGroups {

    Map<Character, Set<Coord>> gardenMap;

    GardenGroups(final List<String> lines) {

        this.gardenMap = new HashMap<>();

        for (int row = 0; row < lines.size(); row++) {
            final String rowStr = lines.get(row);
            for (int col = 0; col < rowStr.length(); col++) {
                gardenMap.computeIfAbsent(rowStr.charAt(col), v -> new HashSet<>()).add(new Coord(row, col));
            }
        }
    }

    long fencePrice() {
        final Map<Character, Set<Set<Coord>>> gardenSubregions = createSubregions(gardenMap);

        return gardenSubregions.values()
                .stream()
                .mapToLong(set -> set.stream().mapToLong(this::subregionFencePrice).sum())
                .sum();
    }

    private Map<Character, Set<Set<Coord>>> createSubregions(final Map<Character, Set<Coord>> garden) {
        final Map<Character, Set<Set<Coord>>> gardenSubregions = new HashMap<>();

        for (final var entry : garden.entrySet()) {
            final Set<Coord> visited = new HashSet<>();
            final Character key = entry.getKey();
            final Set<Coord> coords = entry.getValue();

            for (final Coord coord : coords) {
                if (!visited.contains(coord)) {
                    final Set<Coord> adjacent = new HashSet<>();
                    final Queue<Coord> stack = new ArrayDeque<>();
                    stack.add(coord);
                    visited.add(coord);

                    while (!stack.isEmpty()) {
                        final Coord current = stack.poll();
                        adjacent.add(current);
                        for (Coord neighbor : current.getDirectAdjacent()) {
                            if (coords.contains(neighbor) && visited.add(neighbor)) {
                                stack.add(neighbor);
                            }
                        }
                    }
                    gardenSubregions.computeIfAbsent(key, v -> new HashSet<>()).add(adjacent);
                }
            }

        }

        return gardenSubregions;
    }

    private long subregionFencePrice(final Set<Coord> subregion) {
        final int fenceCount = subregion.stream().mapToInt(c -> {
            final Set<Coord> directAdjacent = c.getDirectAdjacent();
            directAdjacent.removeIf(subregion::contains);
            return directAdjacent.size();
        }).sum();

        return (long) fenceCount * subregion.size();
    }
}
