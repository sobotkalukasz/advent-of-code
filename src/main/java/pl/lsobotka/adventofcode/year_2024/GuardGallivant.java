package pl.lsobotka.adventofcode.year_2024;

import java.util.HashSet;
import java.util.List;
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

}

record GuardMap(Set<Coord> walls, Coord corner, Coord guard) {

    static GuardMap create(List<String> lines) {
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

    private boolean isOnMap(final Coord pos) {
        return pos.row() >= 0 && pos.row() <= corner.row() && pos.col() >= 0 && pos.col() <= corner.col();
    }
}
