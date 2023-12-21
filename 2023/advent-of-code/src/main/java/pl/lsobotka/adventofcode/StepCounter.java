package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2023/day/21
 * */
public class StepCounter {

    private final Set<Coord> walls;
    private final int size;
    private Coord start;

    public StepCounter(final List<String> input) {
        this.walls = new HashSet<>();

        size = input.size();

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

        final int[] deltaDiffs = new int[size];
        final int[] deltaDiffsChange = new int[size];

        int previousStepSize = 0;
        int previosDiff = 0;
        long count = 0;

        for (int step = 1; step <= steps; step++) {
            final int modulo = step % size;

            if (count > 0) {
                final int change = deltaDiffsChange[modulo];
                final int delta = deltaDiffs[modulo] + change;
                final int diff = previosDiff + delta;

                count += diff;
                previosDiff = diff;
                deltaDiffs[modulo] = delta;

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

                final int change = deltaDiff - deltaDiffs[modulo];
                deltaDiffs[modulo] = deltaDiff;

                if (deltaDiffsChange[modulo] != change) {
                    deltaDiffsChange[modulo] = change;
                } else if (modulo == 0) {
                    count = actualSteps.size();
                }
            }
        }

        return count > 0 ? count : actualSteps.size();
    }

    private Set<Coord> nextPositions(final Coord actual) {
        final Set<Coord> direct = actual.getDirectAdjacent();
        direct.removeIf(walls::contains);
        direct.removeIf(c -> c.row() < 0 || c.row() >= size || c.col() < 0 || c.col() >= size);
        return direct;
    }

    private Set<Coord> nextInfinitePosition(final Coord actual) {
        final Set<Coord> direct = actual.getDirectAdjacent();
        direct.removeIf(c -> walls.contains(translate(c)));
        return direct;
    }

    private Coord translate(final Coord c) {
        final Coord translated;
        if (c.row() >= 0 && c.row() < size && c.col() >= 0 && c.col() < size) {
            translated = c;
        } else {
            final int row;
            final int col;
            if (c.row() < 0) {
                row = size + c.row() % size;
            } else {
                row = c.row() % size;
            }
            if (c.col() < 0) {
                col = size + c.col() % size;
            } else {
                col = c.col() % size;
            }
            translated = Coord.of(row, col);
        }
        return translated;
    }

}
