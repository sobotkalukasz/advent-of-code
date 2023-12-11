package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;

import pl.lsobotka.adventofcode.utils.Coord;

public class CosmicExpansion {

    private final GalaxyHolder galaxies;

    public CosmicExpansion(final List<String> input) {
        galaxies = GalaxyHolder.from(input);
    }

    long sumOfDistances() {
        final GalaxyHolder expanded = galaxies.expand();
        final Set<GalaxyDistance> distances = expanded.getGalaxyDistances();
        return distances.stream().map(GalaxyDistance::distance).reduce(0, Integer::sum);
    }

    record GalaxyHolder(Map<Coord, Galaxy> galaxies, int row, int col) {

        static GalaxyHolder of(Map<Coord, Galaxy> galaxies) {
            int row = galaxies.keySet().stream().map(Coord::row).max(Comparator.naturalOrder()).orElse(0);
            int col = galaxies.keySet().stream().map(Coord::col).max(Comparator.naturalOrder()).orElse(0);
            return new GalaxyHolder(galaxies, row, col);
        }

        static GalaxyHolder from(final List<String> input) {
            final HashMap<Coord, Galaxy> galaxies = new HashMap<>();
            long idCounter = 0;

            for (int row = 0; row < input.size(); row++) {
                final String colString = input.get(row);
                for (int col = 0; col < colString.length(); col++) {
                    if (colString.charAt(col) == '#') {
                        final Coord coord = Coord.of(row, col);
                        galaxies.put(coord, new Galaxy(++idCounter, coord));
                    }
                }
            }

            return GalaxyHolder.of(galaxies);
        }

        GalaxyHolder expand() {
            final Map<Coord, Galaxy> expandedGalaxies = new HashMap<>();
            final Map<Integer, Integer> rowOffsets = getExpandValue(Coord::row, row);
            final Map<Integer, Integer> colOffsets = getExpandValue(Coord::col, col);

            galaxies.keySet().forEach(coord -> {
                final Galaxy galaxy = galaxies.get(coord)
                        .expand(rowOffsets.get(coord.row()), colOffsets.get(coord.col()));
                expandedGalaxies.put(galaxy.coord, galaxy);
            });
            return GalaxyHolder.of(expandedGalaxies);
        }

        private Map<Integer, Integer> getExpandValue(final Function<Coord, Integer> function, final int maxValue) {
            Map<Integer, Integer> expandBy = new HashMap<>();
            int offset = 0;
            for (int i = 0; i <= maxValue; i++) {
                final int comparison = i;
                if (galaxies.keySet().stream().map(function).anyMatch(c -> c == comparison)) {
                    expandBy.put(i, offset);
                } else {
                    offset++;
                }
            }
            return expandBy;
        }

        public Set<GalaxyDistance> getGalaxyDistances() {
            final Set<GalaxyDistance> distances = new HashSet<>();
            final List<Coord> coords = new ArrayList<>(galaxies.keySet());

            for (int i = 0; i < coords.size(); i++) {
                final Galaxy from = galaxies.get(coords.get(i));
                for (int j = i + 1; j < coords.size(); j++) {
                    final Galaxy to = galaxies.get(coords.get(j));
                    final int distance = findLessStepsToGalaxy2(from.coord, to.coord);

                    distances.add(new GalaxyDistance(from.id, to.id, distance));
                }
            }
            return distances;
        }

        private int findLessStepsToGalaxy2(final Coord from, final Coord to) {
            return Math.abs(from.row() - to.row()) + Math.abs(from.col() - to.col());
        }

    }

    record Galaxy(long id, Coord coord) {

        Galaxy expand(final int rowOffset, final int colOffset) {
            return new Galaxy(id, Coord.of(coord.row() + rowOffset, coord.col() + colOffset));
        }
    }

    record GalaxyDistance(long from, long to, int distance) {
    }

}
