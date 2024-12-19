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
import java.util.stream.IntStream;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/18
 * */
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

    String determineWhenNotPossible() {
        final int time = testRange(IntStream.range(0, bytes.size()).boxed().toList());
        final Coord coord = bytes.get(time - 1);
        return String.format("%d,%d", coord.col(), coord.row());
    }

    int testRange(final List<Integer> range) {

        final int middleIndex = Math.max(0, (range.size() / 2) - 1);
        final int testTimeValue = range.get(middleIndex);
        final int steps = countStepsAfter(testTimeValue);

        if (range.size() == 1 || (middleIndex == 0 && steps == 0)) {
            return range.getFirst();
        }

        return steps == 0
                ? testRange(range.subList(0, middleIndex + 1)) // Left half
                : testRange(range.subList(middleIndex + 1, range.size())); // Right half
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
