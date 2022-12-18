package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BoilingBoulders {

    final Set<Coord> cubes;

    public BoilingBoulders(final List<String> input) {
        this.cubes = input.stream().map(Coord::of).collect(Collectors.toSet());
    }

    public int surfaceArea() {
        return cubes.stream().mapToInt(this::singleCubeSurfaceArea).sum();
    }

    private int singleCubeSurfaceArea(final Coord coord) {
        final Set<Coord> possible = coord.getAdjacent();
        final int count = (int) possible.stream().filter(cubes::contains).count();
        return 6 - count;
    }

    private record Coord(int x, int y, int z) {

        static Coord of(final String input) {
            final String[] split = input.split(",");
            return Coord.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }

        static Coord of(final int x, final int y, final int z) {
            return new Coord(x, y, z);
        }

        Set<Coord> getAdjacent() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.addAll(adjacentX());
            adjacent.addAll(adjacentY());
            adjacent.addAll(adjacentZ());
            return adjacent;
        }

        private Set<Coord> adjacentX() {
            return Set.of(Coord.of(this.x + 1, this.y, this.z), //
                    Coord.of(this.x - 1, this.y, this.z));
        }

        private Set<Coord> adjacentY() {
            return Set.of(Coord.of(this.x, this.y + 1, this.z), //
                    Coord.of(this.x, this.y - 1, this.z));
        }

        private Set<Coord> adjacentZ() {
            return Set.of(Coord.of(this.x, this.y, this.z + 1), //
                    Coord.of(this.x, this.y, this.z - 1));
        }
    }
}
