package pl.lsobotka.adventofcode.year_2022;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord3d;
import pl.lsobotka.adventofcode.utils.DimensionHolder;

import static java.util.function.Predicate.not;

/*
 * https://adventofcode.com/2022/day/18
 * */
public class BoilingBoulders {

    final Set<Coord3d> cubes;
    final DimensionHolder dimension;
    final AirCache cache;

    public BoilingBoulders(final List<String> input) {
        this.cubes = input.stream().map(Coord3d::of).collect(Collectors.toSet());
        this.dimension = DimensionHolder.of(cubes);
        this.cache = AirCache.empty();
    }

    public int surfaceArea() {
        return cubes.stream().mapToInt(this::simpleCubeSurfaceArea).sum();
    }

    private int simpleCubeSurfaceArea(final Coord3d coord) {
        final Set<Coord3d> possible = coord.getAdjacent();
        final int count = (int) possible.stream().filter(cubes::contains).count();
        return 6 - count;
    }

    public int outsideSurfaceArea() {
        return cubes.stream().mapToInt(this::outsideCubeSurfaceArea).sum();
    }

    private int outsideCubeSurfaceArea(final Coord3d coord) {
        final Set<Coord3d> possible = coord.getAdjacent();
        final int otherCubeCount = (int) possible.stream().filter(cubes::contains).count();

        final Set<Coord3d> air = possible.stream().filter(not(cubes::contains)).collect(Collectors.toSet());
        final int insideAirCount = (int) air.stream().filter(not(this::canReachFromOutside)).count();

        return 6 - otherCubeCount - insideAirCount;
    }

    private boolean canReachFromOutside(final Coord3d coord) {

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

    private boolean determineIfCanReachFromOutside(Coord3d coord) {
        final Coord3d target = dimension.getOverTopCoord();

        final Queue<AirPath> paths = new PriorityQueue<>();
        final Set<Coord3d> visited = new HashSet<>();

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

    private record AirPath(Coord3d coord, int moves) implements Comparable<AirPath> {

        @Override
        public int compareTo(AirPath o) {
            return Comparator.comparing(AirPath::moves).compare(o, this);
        }
    }

    private record AirCache(Set<Coord3d> inside, Set<Coord3d> outside) {

        static AirCache empty() {
            return new AirCache(new HashSet<>(), new HashSet<>());
        }

        void addToCache(final boolean canReachFromOutside, final Set<Coord3d> toAdd) {
            if (canReachFromOutside) {
                this.outside.addAll(toAdd);
            } else {
                this.inside.addAll(toAdd);
            }
        }
    }
}
