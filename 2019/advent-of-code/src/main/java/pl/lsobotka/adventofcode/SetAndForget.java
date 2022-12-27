package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * https://adventofcode.com/2019/day/17
 * */
public class SetAndForget {

    final long[] program;
    final Set<Coord> map;

    SetAndForget(final List<String> input) {
        this.program = input.stream()
                .flatMapToLong(row -> Arrays.stream(row.split(",")).mapToLong(Long::valueOf))
                .toArray();
        this.map = drawMap(program);
    }

    long getAlignmentPattern() {
        return map.stream().filter(this::isIntersection).mapToLong(c -> (long) c.row * c.col).sum();
    }

    private boolean isIntersection(final Coord coord) {
        final Set<Coord> adjacent = coord.getAdjacent();
        return map.containsAll(adjacent);
    }

    private record Coord(int row, int col) {
        static Coord of(final int row, final int col) {
            return new Coord(row, col);
        }

        private Set<Coord> getAdjacent() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(this.row + 1, this.col));
            adjacent.add(Coord.of(this.row - 1, this.col));
            adjacent.add(Coord.of(this.row, this.col + 1));
            adjacent.add(Coord.of(this.row, this.col - 1));
            return adjacent;
        }
    }

    private Set<Coord> drawMap(final long[] program) {
        final List<Long> asciiCodes = new IntCode(program).execute();
        final Set<Coord> map = new HashSet<>();

        int row = 0;
        int col = 0;
        for (Long code : asciiCodes) {
            if (code == '#') {
                map.add(Coord.of(row, col));
                col++;
            } else if (code == '\n') {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return map;
    }

}
