package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2025/day/4
 * */
public class PrintingDepartment {

    private final Set<Coord> papper;

    public PrintingDepartment(List<String> input) {
        papper = init(input);
    }

    private Set<Coord> init(final List<String> input) {
        final Set<Coord> found = new HashSet<>();

        for (int row = 0; row < input.size(); row++) {
            final String rowS = input.get(row);
            for (int col = 0; col < rowS.length(); col++) {
                if (rowS.charAt(col) == '@') {
                    found.add(Coord.of(row, col));
                }
            }
        }
        return found;
    }

    long countAccessible() {
        return papper.stream().filter(this::canRemove).count();
    }

    private boolean canRemove(final Coord p) {
        return p.getAllAdjacent().stream().filter(papper::contains).count() < 4;
    }

    long removeAllPossible() {
        final Set<Coord> removed = new HashSet<>();
        final Deque<Coord> queue = new ArrayDeque<>();

        for (Coord p : papper) {
            if (canRemove(p)) {
                queue.add(p);
            }
        }

        while (!queue.isEmpty()) {
            final Coord toRemove = queue.removeFirst();
            if (papper.remove(toRemove)) {
                removed.add(toRemove);

                for (Coord next : toRemove.getAllAdjacent()) {
                    if (papper.contains(next) && canRemove(next)) {
                        queue.add(next);
                    }
                }
            }
        }

        return removed.size();
    }

}
