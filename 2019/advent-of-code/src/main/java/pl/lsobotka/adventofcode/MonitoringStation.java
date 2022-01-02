package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2019/day/10
 * */
public class MonitoringStation {

    private static final char asteroid = '#';
    private static final Predicate<Character> isAsteroid = c -> c == asteroid;

    private static final UnaryOperator<Coordinate> increaseN = Coordinate::increaseN;
    private static final UnaryOperator<Coordinate> increaseNE = Coordinate::increaseNE;
    private static final UnaryOperator<Coordinate> increaseE = Coordinate::increaseE;
    private static final UnaryOperator<Coordinate> increaseSE = Coordinate::increaseSE;
    private static final UnaryOperator<Coordinate> increaseS = Coordinate::increaseS;
    private static final UnaryOperator<Coordinate> increaseSW = Coordinate::increaseSW;
    private static final UnaryOperator<Coordinate> increaseW = Coordinate::increaseW;
    private static final UnaryOperator<Coordinate> increaseNW = Coordinate::increaseNW;

    private static final List<UnaryOperator<Coordinate>> increaseStrategies = List.of(increaseN, increaseNE, increaseE,
            increaseSE, increaseS, increaseSW, increaseW, increaseNW);

    private final Set<Coordinate> asteroids;
    private int sizeColumn;
    private int sizeRow;

    final Predicate<Coordinate> isValid = test -> {
        if (test.column < 0 || test.row < 0) return false;
        return test.column <= sizeColumn && test.row <= sizeRow;
    };

    public MonitoringStation(char[][] asteroidsMap) {
        this.asteroids = initAsteroids(asteroidsMap);
        this.sizeRow = asteroidsMap.length - 1;
        this.sizeColumn = sizeRow > 0 ? asteroidsMap[0].length - 1 : 0;
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

    public int getValueForAsteroid(final int asteroidNumber) {

        return 0;
    }

    private Map<Coordinate, Integer> getCountOfAsteroids() {
        return this.asteroids.stream().collect(Collectors.toMap(
                Function.identity(), c -> countVisibleAsteroids(c.copy(), new HashSet<>(asteroids))));
    }

    private int countVisibleAsteroids(Coordinate coordinate, Set<Coordinate> asteroids) {
        asteroids.remove(coordinate);

        for (UnaryOperator<Coordinate> strategy : increaseStrategies) {
            removeNotVisibleAsteroids(coordinate, asteroids, strategy);
        }

        removeNotVisibleAsteroids(coordinate, asteroids);
        for (int i = 0; i < 3; i++) {
            asteroids = asteroids.stream().map(c -> c.rotate90(sizeRow)).collect(Collectors.toSet());
            coordinate = coordinate.rotate90(sizeRow);
            removeNotVisibleAsteroids(coordinate, asteroids);
        }

        return asteroids.size();
    }

    private void removeNotVisibleAsteroids(Coordinate coordinate, final Set<Coordinate> asteroids, UnaryOperator<Coordinate> increaseStrategy) {
        Coordinate temp = increaseStrategy.apply(coordinate);
        boolean found = false;
        while (isValid.test(temp)) {
            if (found) {
                asteroids.remove(temp);
            } else if (asteroids.contains(temp)) {
                found = true;
            }
            temp = increaseStrategy.apply(temp);
        }
    }

    private void removeNotVisibleAsteroids(final Coordinate coordinate, final Set<Coordinate> asteroids) {
        int increase = 2;
        Coordinate toTest = new Coordinate(coordinate.row - increase, coordinate.column);
        while (isValid.test(toTest)) {
            for (int i = 1; i < increase; i++) {
                Coordinate temp = new Coordinate(toTest.row, toTest.column + i);
                boolean found = false;
                while (isValid.test(temp)) {
                    if (found) {
                        asteroids.remove(temp);
                    } else if (asteroids.contains(temp)) {
                        found = true;
                    }
                    temp = new Coordinate(temp.row - increase, temp.column + i);
                }
                temp = new Coordinate(toTest.row, toTest.column - i);
                found = false;
                while (isValid.test(temp)) {
                    if (found) {
                        asteroids.remove(temp);
                    } else if (asteroids.contains(temp)) {
                        found = true;
                    }
                    temp = new Coordinate(temp.row - increase, temp.column - i);
                }
            }
            toTest = new Coordinate(coordinate.row - ++increase, coordinate.column);
        }
    }

    record Coordinate(int row, int column) {

        public static Coordinate of(final int row, final int column) {
            return new Coordinate(row, column);
        }

        public Coordinate copy() {
            return new Coordinate(row, column);
        }

        Coordinate increaseN() {
            return new Coordinate(row - 1, column);
        }

        Coordinate increaseNE() {
            return new Coordinate(row - 1, column + 1);
        }

        Coordinate increaseE() {
            return new Coordinate(row, column + 1);
        }

        Coordinate increaseSE() {
            return new Coordinate(row + 1, column + 1);
        }

        Coordinate increaseS() {
            return new Coordinate(row + 1, column);
        }

        Coordinate increaseSW() {
            return new Coordinate(row + 1, column - 1);
        }

        Coordinate increaseW() {
            return new Coordinate(row, column - 1);
        }

        Coordinate increaseNW() {
            return new Coordinate(row - 1, column - 1);
        }

        Coordinate rotate90(int maxSize) {
            return new Coordinate(maxSize - column, row);
        }
    }
}
