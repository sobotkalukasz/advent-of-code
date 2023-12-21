package pl.lsobotka.adventofcode.year_2022;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/*
 * https://adventofcode.com/2022/day/18
 * */
public class BoilingBoulders {

    final Set<Coord> cubes;
    final DimensionHolder dimension;
    final AirCache cache;

    public BoilingBoulders(final List<String> input) {
        this.cubes = input.stream().map(Coord::of).collect(Collectors.toSet());
        this.dimension = DimensionHolder.of(cubes);
        this.cache = AirCache.empty();
    }

    public int surfaceArea() {
        return cubes.stream().mapToInt(this::simpleCubeSurfaceArea).sum();
    }

    private int simpleCubeSurfaceArea(final Coord coord) {
        final Set<Coord> possible = coord.getAdjacent();
        final int count = (int) possible.stream().filter(cubes::contains).count();
        return 6 - count;
    }

    public int outsideSurfaceArea() {
        return cubes.stream().mapToInt(this::outsideCubeSurfaceArea).sum();
    }

    private int outsideCubeSurfaceArea(final Coord coord) {
        final Set<Coord> possible = coord.getAdjacent();
        final int otherCubeCount = (int) possible.stream().filter(cubes::contains).count();

        final Set<Coord> air = possible.stream().filter(not(cubes::contains)).collect(Collectors.toSet());
        final int insideAirCount = (int) air.stream().filter(not(this::canReachFromOutside)).count();

        return 6 - otherCubeCount - insideAirCount;
    }

    private boolean canReachFromOutside(final Coord coord) {

        final boolean canReachFromOutside;
        if (cache.outside.contains(coord)) {
            canReachFromOutside = true;
        } else if (cache.inside.contains(coord)) {
            canReachFromOutside = false;
        } else {
            canReachFromOutside = determineIfCanReachFromOutside(coord);
        }
        return canReachFromOutside;

    }

    private boolean determineIfCanReachFromOutside(Coord coord) {
        final Coord target = dimension.getOverTopCoord();

        final Queue<AirPath> paths = new PriorityQueue<>();
        final Set<Coord> visited = new HashSet<>();

        paths.add(new AirPath(coord, 0));
        visited.add(coord);

        while (!paths.isEmpty() && !paths.peek().coord.equals(target)) {
            final AirPath current = paths.poll();
            current.coord.getAdjacent()
                    .stream()
                    .filter(not(visited::contains))
                    .filter(not(cubes::contains))
                    .filter(dimension::isInside)
                    .map(c -> new AirPath(c, current.moves + 1))
                    .forEach(next -> {
                        visited.add(next.coord);
                        paths.add(next);
                    });
        }

        final boolean canReachFromOutside = !paths.isEmpty();
        cache.addToCache(canReachFromOutside, visited);
        return canReachFromOutside;
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

    private record DimensionHolder(int maxX, int minX, int maxY, int minY, int maxZ, int minZ) {
        private static DimensionHolder of(final Set<Coord> coords) {
            final int maxX = coords.stream().mapToInt(Coord::x).max().orElse(0);
            final int minX = coords.stream().mapToInt(Coord::x).min().orElse(0);
            final int maxY = coords.stream().mapToInt(Coord::y).max().orElse(0);
            final int minY = coords.stream().mapToInt(Coord::y).min().orElse(0);
            final int maxZ = coords.stream().mapToInt(Coord::z).max().orElse(0);
            final int minZ = coords.stream().mapToInt(Coord::z).min().orElse(0);
            return new DimensionHolder(maxX, minX, maxY, minY, maxZ, minZ);
        }

        Coord getOverTopCoord() {
            return Coord.of(maxX + 1, maxY + 1, maxZ + 1);
        }

        boolean isInside(final Coord coord) {
            final int offset = 1;
            final boolean outsideX = coord.x > maxX + offset || coord.x < minX - offset;
            final boolean outsideY = coord.y > maxY + offset || coord.y < minY - offset;
            final boolean outsideZ = coord.z > maxZ + offset || coord.z < minZ - offset;

            return !(outsideX || outsideY || outsideZ);
        }
    }

    private record AirPath(Coord coord, int moves) implements Comparable<AirPath> {

        @Override
        public int compareTo(AirPath o) {
            return Comparator.comparing(AirPath::moves).compare(o, this);
        }
    }

    private record AirCache(Set<Coord> inside, Set<Coord> outside) {

        static AirCache empty() {
            return new AirCache(new HashSet<>(), new HashSet<>());
        }

        void addToCache(final boolean canReachFromOutside, final Set<Coord> toAdd) {
            if (canReachFromOutside) {
                this.outside.addAll(toAdd);
            } else {
                this.inside.addAll(toAdd);
            }
        }
    }
}
