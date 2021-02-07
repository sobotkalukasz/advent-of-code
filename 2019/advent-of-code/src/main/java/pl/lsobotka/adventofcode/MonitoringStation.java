package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/10
 * */
public class MonitoringStation {

    private static final char asteroid = '#';
    private static final Predicate<Character> isAsteroid = c -> c == asteroid;

    private static final UnaryOperator<Coordinates> increaseN = Coordinates::increaseN;
    private static final UnaryOperator<Coordinates> increaseNE = Coordinates::increaseNE;
    private static final UnaryOperator<Coordinates> increaseE = Coordinates::increaseE;
    private static final UnaryOperator<Coordinates> increaseSE = Coordinates::increaseSE;
    private static final UnaryOperator<Coordinates> increaseS = Coordinates::increaseS;
    private static final UnaryOperator<Coordinates> increaseSW = Coordinates::increaseSW;
    private static final UnaryOperator<Coordinates> increaseW = Coordinates::increaseW;
    private static final UnaryOperator<Coordinates> increaseNW = Coordinates::increaseNW;

    private static final List<UnaryOperator<Coordinates>> increaseStrategies = List.of(increaseN, increaseNE, increaseE,
            increaseSE, increaseS, increaseSW, increaseW, increaseNW);

    private final char[][] asteroidsMap;
    private int sizeX;
    private int sizeY;

    Predicate<Coordinates> isValid = test -> {
        if (test.x < 0 || test.y < 0) return false;
        return test.x <= sizeX - 1 && test.y <= sizeY - 1;
    };


    public MonitoringStation(char[][] asteroidsMap) {
        this.asteroidsMap = asteroidsMap;
        this.sizeY = asteroidsMap.length;
        this.sizeX = sizeY > 0 ? asteroidsMap[0].length : 0;
    }

    public int findBestLocation() {
        Map<Coordinates, Integer> locations = new HashMap<>();

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (asteroidsMap[y][x] == asteroid) {
                    Coordinates coordinates = new Coordinates(x, y);
                    int asteroids = countVisibleAsteroids(coordinates, getCopyOfAsteroidsMap());
                    locations.put(coordinates, asteroids);
                }
            }
        }
        return locations.values().stream().max(Integer::compareTo).orElse(0);
    }

    private char[][] getCopyOfAsteroidsMap() {
        char[][] tempMap = new char[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++) {
            System.arraycopy(asteroidsMap[i], 0, tempMap[i], 0, sizeX);
        }
        return tempMap;
    }

    private int countVisibleAsteroids(Coordinates coordinates, char[][] map) {
        for (UnaryOperator<Coordinates> strategy : increaseStrategies) {
            map = removeNotVisibleAsteroids(coordinates, map, strategy);
        }

        map = removeNotVisibleAsteroids(coordinates, map);
        for (int i = 0; i < 3; i++) {
            map = rotate90(map);
            coordinates = coordinates.rotate90(sizeY - 1);
            map = removeNotVisibleAsteroids(coordinates, map);
        }

        return (int) Stream.of(map)
                .flatMap(arr -> IntStream.rangeClosed(0, arr.length - 1)
                        .mapToObj(i -> arr[i]))
                .filter(isAsteroid).count() - 1;
    }

    private char[][] removeNotVisibleAsteroids(Coordinates coordinates, char[][] map, UnaryOperator<Coordinates> increaseStrategy) {
        Coordinates temp = increaseStrategy.apply(coordinates);
        boolean found = false;
        while (isValid.test(temp)) {
            if (found) {
                map[temp.y][temp.x] = '.';
            } else if (map[temp.y][temp.x] == asteroid) {
                found = true;
            }
            temp = increaseStrategy.apply(temp);
        }
        return map;
    }

    private char[][] removeNotVisibleAsteroids(Coordinates coordinates, char[][] map) {
        int increase = 2;
        Coordinates toTest = new Coordinates(coordinates.x, coordinates.y - increase);
        while (isValid.test(toTest)) {
            for (int i = 1; i < increase; i++) {
                Coordinates temp = new Coordinates(toTest.x + i, toTest.y);
                boolean found = false;
                while (isValid.test(temp)) {
                    if (found) {
                        map[temp.y][temp.x] = '.';
                    } else if (map[temp.y][temp.x] == asteroid) {
                        found = true;
                    }
                    temp = new Coordinates(temp.x + i, temp.y - increase);
                }
                temp = new Coordinates(toTest.x - i, toTest.y);
                found = false;
                while (isValid.test(temp)) {
                    if (found) {
                        map[temp.y][temp.x] = '.';
                    } else if (map[temp.y][temp.x] == asteroid) {
                        found = true;
                    }
                    temp = new Coordinates(temp.x - i, temp.y - increase);
                }
            }
            toTest = new Coordinates(coordinates.x, coordinates.y - ++increase);
        }
        return map;
    }

    private char[][] rotate90(char[][] toRotate) {
        var temp = new char[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                temp[sizeY - 1 - x][y] = toRotate[y][x];
            }
        }
        return temp;
    }

    record Coordinates(int x, int y) {
        Coordinates increaseN() {
            return new Coordinates(x, y - 1);
        }

        Coordinates increaseNE() {
            return new Coordinates(x + 1, y - 1);
        }

        Coordinates increaseE() {
            return new Coordinates(x + 1, y);
        }

        Coordinates increaseSE() {
            return new Coordinates(x + 1, y + 1);
        }

        Coordinates increaseS() {
            return new Coordinates(x, y + 1);
        }

        Coordinates increaseSW() {
            return new Coordinates(x - 1, y + 1);
        }

        Coordinates increaseW() {
            return new Coordinates(x - 1, y);
        }

        Coordinates increaseNW() {
            return new Coordinates(x - 1, y - 1);
        }

        Coordinates rotate90(int maxSize) {
            return new Coordinates(y, maxSize - x);
        }
    }

}
