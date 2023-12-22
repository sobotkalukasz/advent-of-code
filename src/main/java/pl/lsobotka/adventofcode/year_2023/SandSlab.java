package pl.lsobotka.adventofcode.year_2023;

import java.util.*;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.DimensionType;
import pl.lsobotka.adventofcode.utils.Coord3d;
import pl.lsobotka.adventofcode.utils.DimensionHolder;

public class SandSlab {

    private final List<Brick> bricks;

    public SandSlab(final List<String> input) {
        this.bricks = input.stream().map(Brick::of).toList();
    }

    long countBricksToRemove() {
        final List<Brick> fallenBricks = getFallenBricks();

        final Map<String, Set<String>> supportsMap = new HashMap<>();
        final Map<String, Set<String>> supportedByMap = new HashMap<>();

        for (int i = 0; i < fallenBricks.size(); i++) {
            final Brick actual = fallenBricks.get(i);
            final int actualHeight = actual.getMaxZ();
            final Set<Coord3d> above = actual.dimensions().getNext(DimensionType.Z, 1);
            fallenBricks.stream().filter(b -> b.getMinZ() == actualHeight + 1).forEach(next -> {
                final boolean supported = next.dimensions.overLaps(above);
                if (supported) {
                    supportsMap.computeIfAbsent(actual.id, v -> new HashSet<>()).add(next.id);
                    supportedByMap.computeIfAbsent(next.id, v -> new HashSet<>()).add(actual.id);
                }
            });
        }

        int count = 0;
        for (Brick brick : fallenBricks) {
            final Set<String> supports = supportsMap.getOrDefault(brick.id, Collections.emptySet());
            final boolean supportedByMoreBricks = supports.stream()
                    .map(b -> supportedByMap.getOrDefault(b, Collections.emptySet()))
                    .allMatch(set -> set.size() > 1);
            if (supportedByMoreBricks) {
                count++;
            }
        }

        return count;
    }

    private List<Brick> getFallenBricks() {
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
            fallenBricks.add(brick.move(DimensionType.Z, offset));
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

        Brick move(final DimensionType dim, final int offset) {
            return new Brick(id, dimensions.move(dim, offset));
        }

        @Override
        public int compareTo(Brick o) {
            return Comparator.comparingInt(Brick::getMinZ).compare(this, o);
        }
    }
}
