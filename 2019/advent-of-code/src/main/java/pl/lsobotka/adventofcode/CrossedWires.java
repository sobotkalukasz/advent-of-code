package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Supplier;

public class CrossedWires {

    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';
    private static final char UP = 'U';

    Coordinates start;
    Coordinates current;
    List<Map<Coordinates, Integer>> paths;

    private final Supplier<Coordinates> leftStrategy = () -> new Coordinates(current.row, current.column - 1);
    private final Supplier<Coordinates> rightStrategy = () -> new Coordinates(current.row, current.column + 1);
    private final Supplier<Coordinates> upStrategy = () -> new Coordinates(current.row - 1, current.column);
    private final Supplier<Coordinates> downStrategy = () -> new Coordinates(current.row + 1, current.column);

    public CrossedWires() {
        start = new Coordinates(0, 0);
        current = start;
        paths = new ArrayList<>();
    }

    public int calcIntersectionDistance(List<String> firstPath, List<String> secondPath) {
        Set<Coordinates> crossroads = getCrossroads(firstPath, secondPath);
        return getClosestDistance(crossroads);
    }

    public int calcFewestSteps(List<String> firstPath, List<String> secondPath) {
        Set<Coordinates> crossroads = getCrossroads(firstPath, secondPath);
        return getFewestSteps(crossroads);
    }

    public Set<Coordinates> getCrossroads(List<String> firstPath, List<String> secondPath) {
        drawPath(firstPath);
        drawPath(secondPath);
        return paths.stream().map(Map::keySet).collect(() -> new HashSet<>(paths.get(0).keySet()), Set::retainAll, Set::retainAll);
    }

    private void drawPath(List<String> path) {
        current = start;
        Map<Coordinates, Integer> wirePath = new HashMap<>();
        int stepCounter = 0;
        for (String p : path) {
            stepCounter = executePath(p, wirePath, stepCounter);
        }
        paths.add(wirePath);
    }

    private int executePath(String path, Map<Coordinates, Integer> wirePath, int stepCounter) {
        return switch (path.charAt(0)) {
            case LEFT -> execute(Integer.parseInt(path.substring(1)), leftStrategy, wirePath, stepCounter);
            case RIGHT -> execute(Integer.parseInt(path.substring(1)), rightStrategy, wirePath, stepCounter);
            case UP -> execute(Integer.parseInt(path.substring(1)), upStrategy, wirePath, stepCounter);
            default -> execute(Integer.parseInt(path.substring(1)), downStrategy, wirePath, stepCounter);
        };
    }

    private int execute(int size, Supplier<Coordinates> strategy, Map<Coordinates, Integer> wirePath, int stepCounter) {
        while (size-- > 0) {
            current = strategy.get();
            wirePath.put(current, ++stepCounter);
        }
        return stepCounter;
    }

    private int getClosestDistance(Set<Coordinates> crossRoads) {
        return crossRoads.stream().mapToInt(this::calcDistanceToStart).min().orElse(0);
    }

    private int calcDistanceToStart(Coordinates cords) {
        return Math.abs(cords.row) + Math.abs(cords.column);
    }

    private int getFewestSteps(Set<Coordinates> crossRoads) {
        return crossRoads.stream()
                .map(cross -> paths.stream().mapToInt(m -> m.get(cross)).sum())
                .reduce(Integer::min).orElse(0);
    }

    private static record Coordinates(int row, int column) {
    }

}
