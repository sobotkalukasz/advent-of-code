package pl.lsobotka.adventofcode.year_2019;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/12
 * */
public class NBodyProblem {

    private final List<Moon> moons;

    public NBodyProblem(List<List<Integer>> moons) {
        this.moons = moons.stream()
                .filter(m -> m.size() == 3)
                .map(m -> new Moon(m.get(0), m.get(1), m.get(2)))
                .toList();
    }

    public long getMoonEnergyAfterSteps(int steps) {
        final List<Moon> actual = moons.stream().map(Moon::copy).collect(Collectors.toList());

        while (steps-- > 0) {
            applyStep(actual);
        }

        return actual.stream().mapToLong(Moon::getTotalEnergy).sum();
    }

    public long countDaysToRepeat() {
        final List<Moon> moonX = moons.stream().map(m -> new Moon(m.position.x(), 0, 0)).collect(Collectors.toList());
        final List<Moon> moonY = moons.stream().map(m -> new Moon(0, m.position.y(), 0)).collect(Collectors.toList());
        final List<Moon> moonZ = moons.stream().map(m -> new Moon(0, 0, m.position.z())).collect(Collectors.toList());

        final long xFreq = getFrequency(moonX);
        final long yFreq = getFrequency(moonY);
        final long zFreq = getFrequency(moonZ);

        return Stream.of(xFreq, yFreq, zFreq).reduce(this::lessCommonMultiple).orElse(0L);
    }

    private long getFrequency(final List<Moon> initialState) {
        final List<Moon> actual = initialState.stream().map(Moon::copy).collect(Collectors.toList());

        long cycle = 0;
        do {
            applyStep(actual);
            cycle++;
        } while (!initialState.equals(actual));

        return cycle;
    }

    private long lessCommonMultiple(long a, long b) {
        long bigger = Math.max(a, b);
        long lower = Math.min(a, b);
        long lcm = bigger;
        while (lcm % lower != 0) {
            lcm += bigger;
        }
        return lcm;
    }

    private void applyStep(final List<Moon> moons) {
        moons.forEach(m -> {
            final ArrayList<Moon> other = new ArrayList<>(moons);
            other.remove(m);
            m.adjustVelocity(other);
        });
        moons.forEach(Moon::applyVelocity);
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
