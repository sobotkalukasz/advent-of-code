package pl.lsobotka.adventofcode.utils;

import java.util.Set;

public record DimensionHolder(int maxX, int minX, int maxY, int minY, int maxZ, int minZ) {
    public static DimensionHolder of(final Set<Coord3d> coords) {
        final int maxX = coords.stream().mapToInt(Coord3d::x).max().orElse(0);
        final int minX = coords.stream().mapToInt(Coord3d::x).min().orElse(0);
        final int maxY = coords.stream().mapToInt(Coord3d::y).max().orElse(0);
        final int minY = coords.stream().mapToInt(Coord3d::y).min().orElse(0);
        final int maxZ = coords.stream().mapToInt(Coord3d::z).max().orElse(0);
        final int minZ = coords.stream().mapToInt(Coord3d::z).min().orElse(0);
        return new DimensionHolder(maxX, minX, maxY, minY, maxZ, minZ);
    }

    public Coord3d getOverTopCoord() {
        return Coord3d.of(maxX + 1, maxY + 1, maxZ + 1);
    }

    public boolean isInside(final Coord3d coord) {
        final int offset = 1;
        final boolean outsideX = coord.x() > maxX + offset || coord.x() < minX - offset;
        final boolean outsideY = coord.y() > maxY + offset || coord.y() < minY - offset;
        final boolean outsideZ = coord.z() > maxZ + offset || coord.z() < minZ - offset;

        return !(outsideX || outsideY || outsideZ);
    }
}
