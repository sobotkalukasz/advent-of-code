package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;
import pl.lsobotka.adventofcode.utils.Rotate;

public class GuardGallivant {
    private final GuardMap guardMap;

    public GuardGallivant(final List<String> input) {
        guardMap = GuardMap.create(input);
    }

    int countVisitedPositions() {
        return guardMap.getVisitedPositions().size();
    }

    int possibleObstaclesToLoopGuard() {
        return guardMap.getLoopObstacles().size();
    }

}

record GuardMap(Set<Coord> walls, Coord corner, Coord guard) {

    static GuardMap create(final List<String> lines) {
        final Set<Coord> walls = new HashSet<>();
        Coord corner = null;
        Coord guard = null;
        for (int row = 0; row < lines.size(); row++) {
            final String rowStr = lines.get(row);
            for (int col = 0; col < rowStr.length(); col++) {
                final char ch = rowStr.charAt(col);
                if (ch == '#') {
                    walls.add(Coord.of(row, col));
                } else if (ch == '^') {
                    guard = Coord.of(row, col);
                }
                corner = Coord.of(row, col);
            }
        }
        return new GuardMap(walls, corner, guard);
    }

    Set<Coord> getVisitedPositions() {
        final Set<Coord> visited = new HashSet<>();
        Coord current = guard;
        Dir dir = Dir.UP;
        while (isOnMap(current)) {
            visited.add(current);
            final Coord next = current.getAdjacent(dir);
            if (walls.contains(next)) {
                dir = dir.rotate(Rotate.R);
            } else {
                current = next;
            }
        }

        return visited;
    }

    Set<Coord> getLoopObstacles() {
        final Set<Coord> obstacles = new HashSet<>();
        final Set<Coord> visited = getVisitedPositions();
        visited.remove(guard);

        for (Coord coord : visited) {
            if (cantLeave(coord)) {
                obstacles.add(coord);
            }
        }

        return obstacles;
    }

    private boolean cantLeave(final Coord obstacle) {
        final Set<Coord> tempWalls = new HashSet<>(walls);
        tempWalls.add(obstacle);

        final List<Coord> visited = new ArrayList<>();
        final Map<Coord, List<Integer>> visitedIndexes = new HashMap<>();

        Coord current = guard;
        Dir dir = Dir.UP;

        while (isOnMap(current) && !isLoop(visited, visitedIndexes)) {
            final Coord next = current.getAdjacent(dir);
            if (tempWalls.contains(next)) {
                dir = dir.rotate(Rotate.R);
            } else {
                visited.add(current);
                visitedIndexes.computeIfAbsent(current, k -> new ArrayList<>()).add(visited.size() - 1);
                current = next;
            }
        }

        return isOnMap(current);
    }

    private boolean isLoop(final List<Coord> visited, final Map<Coord, List<Integer>> visitedIndexes) {
        if (visited.isEmpty() || visitedIndexes.get(visited.getLast()).size() != 3) {
            return false;
        }

        final List<Integer> indexes = visitedIndexes.get(visited.getLast());
        final List<Coord> firstList = visited.subList(indexes.getFirst() + 1, indexes.get(1));
        final List<Coord> secondList = visited.subList(indexes.get(1) + 1, indexes.getLast());

        return firstList.equals(secondList);
    }

    private boolean isOnMap(final Coord pos) {
        return pos.row() >= 0 && pos.row() <= corner.row() && pos.col() >= 0 && pos.col() <= corner.col();
    }
}
