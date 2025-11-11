package pl.lsobotka.adventofcode.year_2023;

import java.util.*;
import java.util.stream.Collectors;
import pl.lsobotka.adventofcode.utils.Coord3d;
import pl.lsobotka.adventofcode.utils.DimensionHolder;
import pl.lsobotka.adventofcode.utils.DimensionType;

public class SandSlab {
    private final List<Brick> bricks;
    private final Map<String, Set<String>> supports;
    private final Map<String, Set<String>> supportedBy;

    public SandSlab(final List<String> input) {
        this.bricks = getFallenBricks(input.stream().map(Brick::of).toList());
        this.supports = new HashMap<>();
        this.supportedBy = new HashMap<>();

        for (int i = 0; i < this.bricks.size(); i++) {
            final Brick actual = this.bricks.get(i);
            final int actualHeight = actual.getMaxZ();
            final Set<Coord3d> above = actual.dimensions().getNext(DimensionType.Z, 1);
            this.bricks.stream().filter(b -> b.getMinZ() == actualHeight + 1).forEach(next -> {
                final boolean supported = next.dimensions.overLaps(above);
                if (supported) {
                    this.supports.computeIfAbsent(actual.id, v -> new HashSet<>()).add(next.id);
                    this.supportedBy.computeIfAbsent(next.id, v -> new HashSet<>()).add(actual.id);
                }
            });
        }
    }

    long countBricksPossibleRemove() {
        int count = 0;
        for (Brick brick : this.bricks) {
            final Set<String> itSupports = this.supports.getOrDefault(brick.id, Collections.emptySet());
            final boolean supportedByMoreBricks = itSupports.stream()
                    .map(b -> this.supportedBy.getOrDefault(b, Collections.emptySet()))
                    .allMatch(set -> set.size() > 1);
            if (supportedByMoreBricks) {
                count++;
            }
        }
        return count;
    }

    int countBricksThatWouldFall() {
        final Map<String, Integer> willFall = new HashMap<>();
        for (Brick brick : bricks) {
            final Set<String> strings = determineFallen(brick.id, new HashSet<>());
            willFall.put(brick.id, strings.size());
        }
        return willFall.values().stream().reduce(0, Integer::sum);
    }

    private Set<String> determineFallen(final String id, final Set<String> willFall) {

        final Set<String> fallThisIteration = new HashSet<>();
        final Set<String> itSupports = this.supports.getOrDefault(id, Collections.emptySet());
        for (String top : itSupports) {
            final Set<String> down = new HashSet<>(this.supportedBy.getOrDefault(top, Collections.emptySet()));
            down.removeIf(i -> willFall.contains(i) || i.equals(id));
            if (down.isEmpty()) {
                fallThisIteration.add(top);
                willFall.add(top);
            }
        }

        fallThisIteration.forEach(s -> determineFallen(s, willFall));

        return willFall;
    }

    private List<Brick> getFallenBricks(final List<Brick> bricks) {
        final List<Brick> fallenBricks = new ArrayList<>();

        bricks.stream().sorted().forEach(brick -> {
            final int maxZ = fallenBricks.stream().map(Brick::getMaxZ).reduce(0, Integer::max);
            int offset = brick.getMinZ() > maxZ ? maxZ - brick.getMinZ() + 1 : 0;
            boolean falling = true;

            while (falling && brick.getMinZ() + offset > 1) {
                final Set<Coord3d> next = brick.dimensions.getNext(DimensionType.Z, offset - 1);
                falling = fallenBricks.stream().noneMatch(b -> b.dimensions().overLaps(next));
                if (falling) {
                    --offset;
                }
            }
            fallenBricks.add(brick.moveDown(offset));
        });

        return fallenBricks;
    }

    record Brick(String id, DimensionHolder dimensions) implements Comparable<Brick> {
        static Brick of(final String input) {
            final Set<Coord3d> tips = Arrays.stream(input.split("~")).map(Coord3d::of).collect(Collectors.toSet());
            return new Brick(UUID.randomUUID().toString(), DimensionHolder.of(tips));
        }

        int getMinZ() {
            return dimensions().minZ();
        }

        int getMaxZ() {
            return dimensions().maxZ();
        }

        Brick moveDown(final int offset) {
            return new Brick(id, dimensions.move(DimensionType.Z, offset));
        }

        @Override
        public int compareTo(Brick o) {
            return Comparator.comparingInt(Brick::getMinZ).compare(this, o);
        }
    }
}
