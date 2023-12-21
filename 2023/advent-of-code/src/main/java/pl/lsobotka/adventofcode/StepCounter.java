package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2023/day/21
 * */
public class StepCounter {

    private final Set<Coord> walls;
    private final int max;
    private Coord start;

    public StepCounter(final List<String> input) {
        this.walls = new HashSet<>();

        max = input.size();

        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            for (int col = 0; col < rowString.length(); col++) {
                if (rowString.charAt(col) == '#') {
                    walls.add(Coord.of(row, col));
                } else if (rowString.charAt(col) == 'S') {
                    this.start = Coord.of(row, col);
                }
            }
        }
    }

    long howManyReached(final long steps) {
        final Set<Coord> actualSteps = new HashSet<>(Set.of(start));
        for (int step = 1; step <= steps; step++) {
            final Set<Coord> nextSteps = new HashSet<>();
            for (Coord coord : actualSteps) {
                nextSteps.addAll(nextPositions(coord));
            }
            actualSteps.clear();
            actualSteps.addAll(nextSteps);
        }

        return actualSteps.size();
    }

    long howManyReachedInfinite(final long steps) {
        final Set<Coord> actualSteps = new HashSet<>(Set.of(start));

        final Map<Integer, Integer> diffs = IntStream.range(0, max)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> 0));

        final Map<Integer, Integer> deltaDiffs = IntStream.range(0, max)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> 0));

        final Map<Integer, Integer> deltaDiffsChange = IntStream.range(0, max)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), v -> 0));

        int previousStepSize = 0;
        int previosDiff = 0;
        long size = 0;

        for (int step = 1; step <= steps; step++) {
            final int modulo = step % (max);

            if (size > 0) {
                final int change = deltaDiffsChange.get(modulo);
                final int delta = deltaDiffs.get(modulo) + change;
                final int diff = diffs.get((step - 1) % (max)) + delta;

                size += diff;
                diffs.put(modulo, diff);
                deltaDiffs.put(modulo, delta);

            } else {
                final Set<Coord> nextSteps = new HashSet<>();
                for (Coord coord : actualSteps) {
                    nextSteps.addAll(nextInfinitePosition(coord));
                }
                actualSteps.clear();
                actualSteps.addAll(nextSteps);

                final int diff = actualSteps.size() - previousStepSize;
                final int deltaDiff = diff - previosDiff;
                previousStepSize = actualSteps.size();
                previosDiff = diff;

                diffs.put(modulo, diff);
                final int change = deltaDiff - deltaDiffs.get(modulo);
                deltaDiffs.put(modulo, deltaDiff);

                if (deltaDiffsChange.get(modulo) != change) {
                    deltaDiffsChange.put(modulo, change);
                } else if (modulo == max - 1) {
                    size = actualSteps.size();
                }
            }
        }

        return size > 0 ? size : actualSteps.size();
    }

    private Set<Coord> nextPositions(final Coord actual) {
        final Set<Coord> direct = actual.getDirectAdjacent();
        direct.removeIf(walls::contains);
        direct.removeIf(c -> c.row() < 0 || c.row() >= max || c.col() < 0 || c.col() >= max);
        return direct;
    }

    private Set<Coord> nextInfinitePosition(final Coord actual) {
        final Set<Coord> direct = actual.getDirectAdjacent();
        direct.removeIf(c -> walls.contains(translate(c)));
        return direct;
    }

    private Coord translate(final Coord c) {
        final Coord translated;
        if (c.row() >= 0 && c.row() < max && c.col() >= 0 && c.col() < max) {
            translated = c;
        } else {
            final int row;
            final int col;
            if (c.row() < 0) {
                row = max + c.row() % max;
            } else {
                row = c.row() % max;
            }
            if (c.col() < 0) {
                col = max + c.col() % max;
            } else {
                col = c.col() % max;
            }
            translated = Coord.of(row, col);
        }
        return translated;
    }

}
