package pl.lsobotka.adventofcode.utils;

import java.util.HashSet;
import java.util.Set;

public record Coord3d(int x, int y, int z) {

    public static Coord3d of(final int x, final int y, final int z) {
        return new Coord3d(x, y, z);
    }

    public static Coord3d of(final String input) {
        final String[] split = input.split(",");
        return Coord3d.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public Set<Coord3d> getAdjacent() {
        final Set<Coord3d> adjacent = new HashSet<>();
        adjacent.addAll(adjacentX());
        adjacent.addAll(adjacentY());
        adjacent.addAll(adjacentZ());
        return adjacent;
    }

    private Set<Coord3d> adjacentX() {
        return Set.of(Coord3d.of(this.x + 1, this.y, this.z), //
                Coord3d.of(this.x - 1, this.y, this.z));
    }

    private Set<Coord3d> adjacentY() {
        return Set.of(Coord3d.of(this.x, this.y + 1, this.z), //
                Coord3d.of(this.x, this.y - 1, this.z));
    }

    private Set<Coord3d> adjacentZ() {
        return Set.of(Coord3d.of(this.x, this.y, this.z + 1), //
                Coord3d.of(this.x, this.y, this.z - 1));
    }

    public long distanceTo(final Coord3d other) {
        long dx = (long) other.x - this.x;
        long dy = (long) other.y - this.y;
        long dz = (long) other.z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }
}
