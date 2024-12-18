package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;

public class RamRun {

    final List<Coord> bytes;
    final int size;

    RamRun(final List<String> input, int size) {
        this.bytes = new ArrayList<>();
        this.size = size;

        for (String line : input) {
            final String[] split = line.split(",");
            bytes.add(new Coord(Integer.parseInt(split[1]), Integer.parseInt(split[0])));
        }
    }

    int countStepsAfter(final int time) {
        final Queue<Path> paths = new PriorityQueue<>(Comparator.comparing(Path::steps));
        paths.add(new Path(Coord.of(0, 0), 0));

        final Map<Coord, Integer> cache = new HashMap<>();
        final Coord finish = Coord.of(size, size);
        final Set<Coord> fallenBytes = new HashSet<>(bytes.subList(0, time));

        int steps = 0;
        while (!paths.isEmpty()) {
            final Path current = paths.poll();
            if (current.coord().equals(finish)) {
                steps = current.steps();
                break;
            }

            if (cache.getOrDefault(current.coord(), Integer.MAX_VALUE) > current.steps()) {
                cache.put(current.coord(), current.steps());
                for (Coord next : current.coord().getDirectAdjacent()) {
                    if (onBoard(next) && !fallenBytes.contains(next)) {
                        paths.add(current.next(next));
                    }
                }
            }
        }

        return steps;
    }

    private boolean onBoard(final Coord coord) {
        return coord.row() >= 0 && coord.row() <= size && coord.col() >= 0 && coord.col() <= size;
    }

    record Path(Coord coord, int steps) {
        Path next(Coord next) {
            return new Path(next, steps + 1);
        }
    }

}
