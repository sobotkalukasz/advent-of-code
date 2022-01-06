package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NBodyProblem {

    private final List<Moon> moons;

    public NBodyProblem(List<List<Integer>> moons) {
        this.moons = moons.stream()
                .filter(m -> m.size() == 3)
                .map(m -> new Moon(m.get(0), m.get(1), m.get(2)))
                .toList();
    }

    public long getMoonEnergyAfter(int steps) {
        final List<Moon> actual = moons.stream().map(Moon::copy).collect(Collectors.toList());

        while (steps-- > 0) {
            actual.forEach(m -> {
                final ArrayList<Moon> other = new ArrayList<>(actual);
                other.remove(m);
                m.adjustVelocity(other);
            });

            actual.forEach(Moon::applyVelocity);
        }

        return actual.stream().map(Moon::getTotalEnergy).reduce(Long::sum).orElse(0L);
    }

    public long countDaysToRepeat() {
        //TODO: implement

        return 0;
    }

    static class Moon {
        private Point position;
        private Point velocity;

        public Moon(int x, int y, int z) {
            this.position = Point.init(x, y, z);
            this.velocity = Point.empty();
        }

        private Moon(Point position, Point velocity) {
            this.position = position;
            this.velocity = velocity;
        }

        public Moon copy() {
            return new Moon(this.position, this.velocity);
        }

        public void adjustVelocity(final List<Moon> moons) {
            this.velocity = this.velocity.adjust(this.position, moons.stream().map(m -> m.position).toList());
        }

        public void applyVelocity() {
            this.position = this.position.apply(this.velocity);
        }

        public long getTotalEnergy() {
            return potentialEnergy() * kineticEnergy();
        }

        private long potentialEnergy() {
            return Math.abs(position.x()) + Math.abs(position.y()) + Math.abs(position.z());
        }

        private long kineticEnergy() {
            return Math.abs(velocity.x()) + Math.abs(velocity.y()) + Math.abs(velocity.z());
        }

    }

    record Point(int x, int y, int z) {
        public static Point init(final int x, final int y, final int z) {
            return new Point(x, y, z);
        }

        public static Point empty() {
            return new Point(0, 0, 0);
        }

        public Point apply(final Point velocity) {
            return Point.init(this.x + velocity.x(), this.y + velocity.y(), this.z + velocity.z());
        }

        public Point adjust(final Point actualPosition, final List<Point> points) {
            final int x = this.x + points.stream().mapToInt(p -> compare(actualPosition.x(), p.x())).sum();
            final int y = this.y + points.stream().mapToInt(p -> compare(actualPosition.y(), p.y())).sum();
            final int z = this.z + points.stream().mapToInt(p -> compare(actualPosition.z(), p.z())).sum();
            return Point.init(x, y, z);
        }

        private int compare(final int a, final int b) {
            return Integer.compare(b, a);
        }
    }
}
