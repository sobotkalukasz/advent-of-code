package pl.lsobotka.adventofcode.year_2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2019/day/10
 * */
public class MonitoringStation {

    private static final char asteroid = '#';
    private static final Predicate<Character> isAsteroid = c -> c == asteroid;

    private final Set<Coordinate> asteroids;

    public MonitoringStation(char[][] asteroidsMap) {
        this.asteroids = initAsteroids(asteroidsMap);
    }

    private Set<Coordinate> initAsteroids(char[][] asteroidsMap) {
        final Set<Coordinate> asteroids = new HashSet<>();
        for (int row = 0; row < asteroidsMap.length; row++) {
            char[] currentRow = asteroidsMap[row];
            for (int col = 0; col < currentRow.length; col++) {
                if (isAsteroid.test(currentRow[col])) {
                    asteroids.add(Coordinate.of(row, col));
                }
            }
        }
        return asteroids;
    }

    public int findBestLocation() {
        final Map<Coordinate, Integer> locations = getCountOfAsteroids();
        return locations.values().stream().max(Integer::compareTo).orElse(0);
    }

    public int getValueForDestroyedAsteroid(final int asteroidNumber) {
        final Map<Coordinate, Integer> visibleAsteroidCount = getCountOfAsteroids();
        final Coordinate bestLocation = visibleAsteroidCount.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseGet(() -> new Coordinate(0, 0));

        final List<Coordinate> destroyed = destroyAllAsteroids(bestLocation);

        int result = 0;
        if (destroyed.size() >= asteroidNumber) {
            final Coordinate asteroid = destroyed.get(asteroidNumber - 1);
            result = asteroid.column() * 100 + asteroid.row();
        }
        return result;
    }

    private List<Coordinate> destroyAllAsteroids(final Coordinate start) {

        final Map<Double, List<Coordinate>> byAngle = asteroids.stream()
                .filter(c -> !c.equals(start))
                .sorted(Comparator.comparing(start::distance))
                .collect(Collectors.groupingBy(start::getAngle, Collectors.toList()));
        final List<Double> angles = byAngle.keySet().stream().sorted().collect(Collectors.toList());

        final List<Coordinate> removed = new ArrayList<>();

        while (removed.size() < asteroids.size() - 1) {
            angles.forEach(angle -> {
                final List<Coordinate> coordinates = byAngle.get(angle);
                if (!coordinates.isEmpty()) {
                    final Coordinate toRemove = coordinates.get(0);
                    coordinates.remove(toRemove);
                    removed.add(toRemove);
                }
            });
        }

        return removed;
    }

    private Map<Coordinate, Integer> getCountOfAsteroids() {
        return this.asteroids.stream()
                .collect(Collectors.toMap(Function.identity(),
                        c -> countVisibleAsteroids(c.copy(), new HashSet<>(asteroids))));
    }

    private int countVisibleAsteroids(Coordinate coordinate, Set<Coordinate> asteroids) {
        return (int) asteroids.stream().filter(c -> !c.equals(coordinate)).map(coordinate::getAngle).distinct().count();

    }

    record Coordinate(int row, int column) {

        public static Coordinate of(final int row, final int column) {
            return new Coordinate(row, column);
        }

        public double getAngle(final Coordinate other) {
            final double angle = 90 - Math.toDegrees(Math.atan2(row - other.row(), other.column() - column));
            return angle < 0 ? angle + 360 : angle;
        }

        public int distance(final Coordinate other) {
            return Math.abs(this.row - other.row()) + Math.abs(this.column - other.column());
        }

        public Coordinate copy() {
            return new Coordinate(row, column);
        }

    }
}
