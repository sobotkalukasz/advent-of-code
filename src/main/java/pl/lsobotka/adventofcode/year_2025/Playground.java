package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import pl.lsobotka.adventofcode.utils.Coord3d;

/*
 * https://adventofcode.com/2025/day/8
 * */
public class Playground {

    final List<Coord3d> coords;
    final List<Distance> distances;

    public Playground(final List<String> input) {
        coords = input.stream().map(Coord3d::of).toList();
        distances = toDistances(coords);
    }

    private List<Distance> toDistances(final List<Coord3d> coords) {
        final List<Distance> dist = new ArrayList<>();
        for (int i = 0; i < coords.size(); i++) {
            for (int j = i + 1; j < coords.size(); j++) {
                dist.add(Distance.of(coords.get(i), coords.get(j)));
            }
        }
        dist.sort(Comparator.comparingLong(Distance::distance));
        return dist;
    }

    private List<Circuit> toCircuits(final List<Coord3d> coords) {
        return coords.stream()
                .map(c -> new Circuit(new HashSet<>(Set.of(c))))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    long makeConnections(final int conQty) {
        final List<Circuit> circuits = toCircuits(coords);
        for (int i = 0; i < conQty; i++) {
            final Distance actual = distances.get(i);

            final Circuit from = circuits.stream()
                    .filter(c -> c.coords.contains(actual.from))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            final Circuit to = circuits.stream()
                    .filter(c -> c.coords.contains(actual.to))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            if (from != to) {
                circuits.remove(from);
                circuits.remove(to);
                circuits.add(from.connect(to));
            }
        }

        return circuits.stream()
                .map(c -> c.coords().size())
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    long distanceByLastConnection() {
        final List<Circuit> circuits = toCircuits(coords);

        Optional<Distance> last = Optional.empty();
        for (Distance actual : distances) {
            final Circuit from = circuits.stream()
                    .filter(c -> c.coords.contains(actual.from))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
            final Circuit to = circuits.stream()
                    .filter(c -> c.coords.contains(actual.to))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            if (from != to) {
                circuits.remove(from);
                circuits.remove(to);
                circuits.add(from.connect(to));

                if (circuits.size() == 1) {
                    last = Optional.of(actual);
                    break;
                }
            }

        }

        return last.map(l -> (long) l.from.x() * l.to.x()).orElse(0L);
    }

    record Distance(Coord3d from, Coord3d to, long distance) {

        static Distance of(Coord3d a, Coord3d b) {
            final long distance = a.distanceTo(b);
            int cmp = Comparator.comparingInt(Coord3d::x)
                    .thenComparingInt(Coord3d::y)
                    .thenComparingInt(Coord3d::z)
                    .compare(a, b);
            return (cmp <= 0) ? new Distance(a, b, distance) : new Distance(b, a, distance);
        }
    }

    record Circuit(Set<Coord3d> coords) {

        Circuit connect(final Circuit other) {
            final HashSet<Coord3d> connected = new HashSet<>(this.coords);
            connected.addAll(other.coords);
            return new Circuit(connected);
        }
    }
}
