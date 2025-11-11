package pl.lsobotka.adventofcode.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record DimensionHolder(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public static DimensionHolder of(final Set<Coord3d> coords) {
        final int maxX = coords.stream().mapToInt(Coord3d::x).max().orElse(0);
        final int minX = coords.stream().mapToInt(Coord3d::x).min().orElse(0);
        final int maxY = coords.stream().mapToInt(Coord3d::y).max().orElse(0);
        final int minY = coords.stream().mapToInt(Coord3d::y).min().orElse(0);
        final int maxZ = coords.stream().mapToInt(Coord3d::z).max().orElse(0);
        final int minZ = coords.stream().mapToInt(Coord3d::z).min().orElse(0);
        return new DimensionHolder(minX, minY, minZ, maxX, maxY, maxZ);
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

    public Set<Coord3d> getNext(final DimensionType dim, final int offset) {
        final Set<Coord3d> next = new HashSet<>();

        int tempMinX = minX;
        int tempMaxX = maxX;
        int tempMinY = minY;
        int tempMaxY = maxY;
        int tempMinZ = minZ;
        int tempMaxZ = maxZ;

        if (dim == DimensionType.X) {
            if (offset > 0) {
                tempMinX = maxX + offset;
                tempMaxX = maxX + offset;
            } else {
                tempMinX = minX + offset;
                tempMaxX = minX + offset;
            }
        } else if (dim == DimensionType.Y) {
            if (offset > 0) {
                tempMinY = maxY + offset;
                tempMaxY = maxY + offset;
            } else {
                tempMinY = minY + offset;
                tempMaxY = minY + offset;
            }
        } else if (dim == DimensionType.Z) {
            if (offset > 0) {
                tempMinZ = maxZ + offset;
                tempMaxZ = maxZ + offset;
            } else {
                tempMinZ = minZ + offset;
                tempMaxZ = minZ + offset;
            }
        }

        for (int x = tempMinX; x <= tempMaxX; x++) {
            for (int y = tempMinY; y <= tempMaxY; y++) {
                for (int z = tempMinZ; z <= tempMaxZ; z++) {
                    next.add(Coord3d.of(x, y, z));
                }
            }
        }
        return next;
    }

    public boolean overLaps(final Collection<Coord3d> coords) {
        return coords.stream().anyMatch(this::overLaps);
    }

    public boolean overLaps(final Coord3d coord) {
        final boolean insideX = minX <= coord.x() && coord.x() <= maxX;
        final boolean insideY = minY <= coord.y() && coord.y() <= maxY;
        final boolean insideZ = minZ <= coord.z() && coord.z() <= maxZ;

        return insideX && insideY && insideZ;
    }

    public DimensionHolder move(final DimensionType dim, final int offset) {
        return switch (dim) {
            case X -> new DimensionHolder(minX + offset, minY, minZ, maxX + offset, maxY, maxZ);
            case Y -> new DimensionHolder(minX, minY + offset, minZ, maxX, maxY + offset, maxZ);
            case Z -> new DimensionHolder(minX, minY, minZ + offset, maxX, maxY, maxZ + offset);
        };
    }
}
