package pl.lsobotka.adventofcode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/14
 * */
public class ParabolicReflectorDish {

    private final Dish dish;

    public ParabolicReflectorDish(final List<String> input) {

        this.dish = Dish.of(input);

    }

    long calculateLoad() {
        final Dish tilted = dish.tilt();
        return tilted.calculateLoad();
    }

    long calculateLoadAfterCycles(final long cycle) {
        final Dish tilted = dish.tiltCycles(cycle);
        return tilted.calculateLoad();
    }

    record Dish(Set<Coord> roundRocks, Set<Coord> cubeRocks, Coord max) {
        static Dish of(final List<String> input) {
            final Set<Coord> roundRocks = new HashSet<>();
            final Set<Coord> cubeRocks = new HashSet<>();

            int maxRow = input.size() - 1;
            int maxCol = input.get(0).length() - 1;

            for (int row = 0; row < input.size(); row++) {
                final String rowString = input.get(row);
                for (int col = 0; col < rowString.length(); col++) {
                    final char c = rowString.charAt(col);
                    if (c == 'O') {
                        roundRocks.add(Coord.of(row, col));
                    } else if (c == '#') {
                        cubeRocks.add(Coord.of(row, col));
                    }
                }
            }
            return new Dish(roundRocks, cubeRocks, Coord.of(maxRow, maxCol));
        }

        long calculateLoad() {
            final Map<Integer, Long> rocksByRow = roundRocks.stream()
                    .map(Coord::row)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            long load = 0;
            for (Map.Entry<Integer, Long> entry : rocksByRow.entrySet()) {
                final int actualRow = entry.getKey();
                final int multiplyBy = max.row() + 1 - actualRow;
                load += entry.getValue() * multiplyBy;
            }
            return load;
        }

        Dish tilt() {
            final Set<Coord> tiltedRocks = tilt(this.roundRocks, Dir.UP,
                    Comparator.comparingInt(Coord::row).thenComparing(Coord::col));
            return new Dish(tiltedRocks, cubeRocks, max);
        }

        Dish tiltCycles(long cycle) {
            final Map<Set<Coord>, Long> cache = new HashMap<>();
            Set<Coord> tilted = this.roundRocks;

            while (cycle-- > 0) {
                Set<Coord> temp = tilted;
                temp = tilt(temp, Dir.UP, Comparator.comparingInt(Coord::row));
                temp = tilt(temp, Dir.LEFT, Comparator.comparingInt(Coord::col));
                temp = tilt(temp, Dir.DOWN, Comparator.comparingInt(Coord::row).reversed());
                temp = tilt(temp, Dir.RIGHT, Comparator.comparingInt(Coord::col).reversed());
                tilted = temp;

                if (cache.containsKey(tilted)) {
                    final long previous = cache.get(tilted);
                    final long diff = previous - cycle;
                    if (diff < cycle) {
                        cycle = cycle % diff;
                    }
                } else {
                    cache.put(tilted, cycle);
                }
            }

            return new Dish(tilted, cubeRocks, max);
        }

        private Set<Coord> tilt(final Set<Coord> roundRocks, Dir dir, Comparator<Coord> comparator) {
            final Set<Coord> cubeRocks = this.cubeRocks;
            final Set<Coord> tiltedRocks = new HashSet<>();
            final List<Coord> sortedRocks = roundRocks.stream().sorted(comparator).toList();

            for (Coord rock : sortedRocks) {
                Coord actual = rock;
                while (canMove(actual, dir, cubeRocks, tiltedRocks)) {
                    actual = actual.getAdjacent(dir);
                }
                tiltedRocks.add(actual);
            }

            return tiltedRocks;
        }

        private boolean canMove(final Coord coord, final Dir dir, final Set<Coord> cubeRocks,
                final Set<Coord> tiltedRocks) {
            final Coord next = coord.getAdjacent(dir);

            if (cubeRocks.contains(next) || tiltedRocks.contains(next)) {
                return false;
            }
            return next.row() >= 0 && next.row() <= max.row() && next.col() >= 0 && next.col() <= max.col();
        }
    }
}
