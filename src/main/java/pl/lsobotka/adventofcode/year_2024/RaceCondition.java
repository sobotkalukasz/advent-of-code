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

import pl.lsobotka.adventofcode.utils.Board;
import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/20
 * */
public class RaceCondition {

    private final RaceTrack raceTrack;

    public RaceCondition(final List<String> input) {
        this.raceTrack = RaceTrack.from(input);
    }

    long countShortcutsOver(final int cheatTime, final int saveAtLeast) {
        return raceTrack.countShortcutsOver(cheatTime, saveAtLeast);
    }

    private record RaceTrack(Board board) {

        static RaceTrack from(final List<String> input) {
            return new RaceTrack(Board.from(input));
        }

        private List<Coord> getTrack() {
            final List<Coord> track = new ArrayList<>();

            final Queue<Path> paths = new PriorityQueue<>(Comparator.comparingInt(Path::t));
            paths.add(new Path(board.start(), 0));
            final Set<Coord> visited = new HashSet<>();
            visited.add(board.start());

            while (!paths.isEmpty()) {
                final Path path = paths.poll();
                track.add(path.c);

                if (board().isEnd(path.c)) {
                    break;
                }

                for (Coord next : path.c.getDirectAdjacent()) {
                    if (board.isNotWall(next) && visited.add(next)) {
                        paths.add(new Path(next, path.t + 1));
                    }
                }
            }

            return track;
        }

        long countShortcutsOver(final int cheatTime, final int saveAtLeast) {
            final List<Coord> track = this.getTrack();
            final Map<Integer, Integer> shortcuts = new HashMap<>();

            for (int i = 0; i < track.size(); i++) {
                final Coord start = track.get(i);
                for (int j = i + 1; j < track.size(); j++) {
                    final Coord end = track.get(j);
                    final int manhattanDistance = start.distance(end);
                    if (manhattanDistance <= cheatTime) {
                        final int truckDistance = j - i;
                        final int diff = truckDistance - manhattanDistance;
                        if (diff > 0) {
                            shortcuts.put(diff, shortcuts.getOrDefault(diff, 0) + 1);
                        }
                    }
                }
            }

            return shortcuts.entrySet()
                    .stream()
                    .filter(e -> e.getKey() >= saveAtLeast)
                    .mapToLong(Map.Entry::getValue)
                    .sum();
        }

        private record Path(Coord c, int t) {

        }
    }
}
