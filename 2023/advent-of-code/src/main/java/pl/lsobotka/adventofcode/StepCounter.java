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
    private final Coord max;
    private Coord start;

    public StepCounter(final List<String> input) {
        this.walls = new HashSet<>();

        int maxRow = input.size() - 1;
        int maxCol = input.get(0).length() - 1;
        this.max = Coord.of(maxRow, maxCol);

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

    private Set<Coord> nextPositions(final Coord actual) {
        final Set<Coord> direct = actual.getDirectAdjacent();
        direct.removeIf(walls::contains);
        direct.removeIf(c -> c.row() < 0 || c.row() > max.row() || c.col() < 0 || c.col() > max.col());
        return direct;
    }

}
