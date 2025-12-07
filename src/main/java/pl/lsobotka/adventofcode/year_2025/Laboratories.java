package pl.lsobotka.adventofcode.year_2025;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

public class Laboratories {

    private Coord start;
    private final Map<Integer, Set<Coord>> splitters = new HashMap<>();

    public Laboratories(final List<String> input) {

        for (int row = 0; row < input.size(); row++) {
            final String rowS = input.get(row);
            for (int col = 0; col < rowS.length(); col++) {
                switch (rowS.charAt(col)) {
                case '^' -> splitters.computeIfAbsent(row, _ -> new HashSet<>()).add(Coord.of(row, col));
                case 'S' -> start = Coord.of(row, col);
                default -> { /* do nothing */}
                }
            }
        }

    }

    long countBeamSplits() {
        final AtomicLong splitCounter = new AtomicLong(0);
        final Map<Integer, Set<Coord>> beams = new HashMap<>();

        final int rows = splitters.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        beams.computeIfAbsent(start.row(), _ -> new HashSet<>()).add(start);

        for (int row = 0; row <= rows; row++) {
            final Set<Coord> actualSplitters = splitters.getOrDefault(row + 1, Set.of());
            final Set<Coord> actualBeams = beams.getOrDefault(row, Set.of());

            actualBeams.forEach(beam -> {
                final Coord next = beam.getAdjacent(Dir.DOWN);
                if (actualSplitters.contains(next)) {
                    splitCounter.incrementAndGet();
                    beams.computeIfAbsent(next.row(), _ -> new HashSet<>())
                            .addAll(next.getAdjacent(List.of(Dir.LEFT, Dir.RIGHT)));
                } else {
                    beams.computeIfAbsent(next.row(), _ -> new HashSet<>()).add(next);
                }
            });

        }

        return splitCounter.get();
    }

    long countTimelines() {
        final Map<Integer, Map<Coord, Long>> beams = new HashMap<>();

        final int rows = splitters.keySet().stream().max(Comparator.naturalOrder()).orElse(0);
        beams.computeIfAbsent(start.row(), _ -> new HashMap<>()).put(start, 1L);

        for (int row = 0; row <= rows; row++) {
            final Set<Coord> actualSplitters = splitters.getOrDefault(row + 1, Set.of());
            final Map<Coord, Long> actualBeams = beams.getOrDefault(row, Map.of());

            actualBeams.forEach((beam, count) -> {
                final Coord next = beam.getAdjacent(Dir.DOWN);
                if (actualSplitters.contains(next)) {
                    final Coord left = next.getAdjacent(Dir.LEFT);
                    final Coord right = next.getAdjacent(Dir.RIGHT);
                    beams.computeIfAbsent(next.row(), _ -> new HashMap<>()).merge(left, count, Long::sum);
                    beams.computeIfAbsent(next.row(), _ -> new HashMap<>()).merge(right, count, Long::sum);
                } else {
                    beams.computeIfAbsent(next.row(), _ -> new HashMap<>()).merge(next, count, Long::sum);
                }
            });

        }
        return beams.get(rows + 1).values().stream().mapToLong(Long::longValue).sum();
    }
}
