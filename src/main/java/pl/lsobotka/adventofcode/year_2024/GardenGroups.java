package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToIntFunction;

import pl.lsobotka.adventofcode.utils.Coord;

public class GardenGroups {

    private final Map<Character, Set<Coord>> gardenMap;

    GardenGroups(final List<String> lines) {
        this.gardenMap = new HashMap<>();

        for (int row = 0; row < lines.size(); row++) {
            final String rowStr = lines.get(row);
            for (int col = 0; col < rowStr.length(); col++) {
                gardenMap.computeIfAbsent(rowStr.charAt(col), v -> new HashSet<>()).add(new Coord(row, col));
            }
        }
    }

    int fencePrice() {
        return calculatePrice(this::subregionFencePrice);
    }

    int fencePriceBySide() {
        return calculatePrice(this::subregionFencePriceBySide);
    }

    private int calculatePrice(final ToIntFunction<Set<Coord>> strategy) {
        final Map<Character, Set<Set<Coord>>> gardenSubregions = createSubregions(gardenMap);
        return gardenSubregions.values().stream().mapToInt(set -> set.stream().mapToInt(strategy).sum()).sum();
    }

    private Map<Character, Set<Set<Coord>>> createSubregions(final Map<Character, Set<Coord>> garden) {
        final Map<Character, Set<Set<Coord>>> gardenSubregions = new HashMap<>();

        for (final var entry : garden.entrySet()) {
            final Set<Coord> visited = new HashSet<>();
            final Character key = entry.getKey();
            final Set<Coord> coords = entry.getValue();

            for (final Coord coord : coords) {
                if (!visited.contains(coord)) {
                    final Set<Coord> subregion = new HashSet<>();
                    final Queue<Coord> stack = new ArrayDeque<>();
                    stack.add(coord);
                    visited.add(coord);

                    while (!stack.isEmpty()) {
                        final Coord current = stack.poll();
                        subregion.add(current);
                        for (Coord neighbor : current.getDirectAdjacent()) {
                            if (coords.contains(neighbor) && visited.add(neighbor)) {
                                stack.add(neighbor);
                            }
                        }
                    }
                    gardenSubregions.computeIfAbsent(key, v -> new HashSet<>()).add(subregion);
                }
            }
        }

        return gardenSubregions;
    }

    private int subregionFencePrice(final Set<Coord> subregion) {
        final int fenceCount = subregion.stream()
                .mapToInt(c -> (int) c.getDirectAdjacent()
                        .stream()
                        .filter(neighbor -> !subregion.contains(neighbor))
                        .count())
                .sum();
        return fenceCount * subregion.size();
    }

    private int subregionFencePriceBySide(final Set<Coord> subregion) {
        final int sides = calculateVertices(subregion);
        return sides * subregion.size();

    }

    public int calculateVertices(final Set<Coord> centers) {
        final Map<CoordVertex, Integer> vertexCount = new HashMap<>();
        for (Coord center : centers) {
            for (CoordVertex vertex : CoordVertex.toVertex(center)) {
                vertexCount.put(vertex, vertexCount.getOrDefault(vertex, 0) + 1);
            }
        }
        int result = 0;
        for (var entry : vertexCount.entrySet()) {
            if (entry.getValue() == 1 || entry.getValue() == 3) {
                result++;
            } else if (entry.getValue() == 2 && isCornerTouch(centers, entry.getKey())) {
                result += 2;
            }
        }
        return result;
    }

    private boolean isCornerTouch(Set<Coord> centers, CoordVertex vertex) {
        final List<Coord> coords = CoordVertex.toCenter(vertex);
        coords.removeIf(centers::contains);
        return !coords.getFirst().getDirectAdjacent().contains(coords.get(1));
    }

    record CoordVertex(double row, double col) {
        static Set<CoordVertex> toVertex(final Coord center) {
            final Set<CoordVertex> vertex = new HashSet<>();
            vertex.add(new CoordVertex(center.row() - 0.5, center.col() - 0.5));
            vertex.add(new CoordVertex(center.row() + 0.5, center.col() - 0.5));
            vertex.add(new CoordVertex(center.row() + 0.5, center.col() + 0.5));
            vertex.add(new CoordVertex(center.row() - 0.5, center.col() + 0.5));
            return vertex;
        }

        static List<Coord> toCenter(final CoordVertex vertex) {
            List<Coord> center = new ArrayList<>();
            center.add(new Coord((int) (vertex.row() - 0.5), (int) (vertex.col() - 0.5)));
            center.add(new Coord((int) (vertex.row() + 0.5), (int) (vertex.col() - 0.5)));
            center.add(new Coord((int) (vertex.row() + 0.5), (int) (vertex.col() + 0.5)));
            center.add(new Coord((int) (vertex.row() - 0.5), (int) (vertex.col() + 0.5)));
            return center;
        }
    }

}
