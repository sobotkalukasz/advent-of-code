package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CrossedWires {

    private static final char EMPTY = '.';
    private static final char START = 'O';
    private static final char H_LINE = '-';
    private static final char V_LINE = '|';
    private static final char TURN = '+';
    private static final char CROSS = 'X';

    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';
    private static final char UP = 'U';
    private static final char DOWN = 'D';

    Coordinates start;
    Coordinates current;
    List<Set<Coordinates>> paths;

    private final Supplier<Coordinates> leftStrategy = () -> new Coordinates(current.row, current.column - 1);
    private final Supplier<Coordinates> rightStrategy = () -> new Coordinates(current.row, current.column + 1);
    private final Supplier<Coordinates> upStrategy = () -> new Coordinates(current.row - 1, current.column);
    private final Supplier<Coordinates> downStrategy = () -> new Coordinates(current.row + 1, current.column);

    public int calcIntersectionDistance(List<String> firstPath, List<String> secondPath) {
        Set<Coordinates> crossroads = getCrossroads(firstPath, secondPath);
        return getClosestDistance(crossroads);
    }

    public int calcFewestSteps(List<String> firstPath, List<String> secondPath) {
        Set<Coordinates> crossroads = getCrossroads(firstPath, secondPath);
        return getClosestDistance(crossroads);
    }

    public Set<Coordinates> getCrossroads(List<String> firstPath, List<String> secondPath){
        initBoard();
        drawPath(firstPath);
        drawPath(secondPath);
        return paths.stream().collect(() -> new HashSet<>(paths.get(0)), Set::retainAll, Set::retainAll);
    }

    private void initBoard() {
        start = new Coordinates(0, 0);
        current = start;
        paths = new ArrayList<>();
    }

    private int getClosestDistance(Set<Coordinates> crossRoads) {
        return crossRoads.stream().mapToInt(this::calcDistanceToStart).min().orElse(0);
    }

    private int calcDistanceToStart(Coordinates cords) {
        return Math.abs(cords.row) + Math.abs(cords.column);
    }

    private void drawPath(List<String> path) {
        current = start;
        paths.add(path.stream().map(this::executePath).flatMap(Collection::stream).collect(Collectors.toSet()));
    }

    private Set<Coordinates> executePath(String path) {
        return switch (path.charAt(0)) {
            case LEFT -> execute(Integer.parseInt(path.substring(1)), leftStrategy);
            case RIGHT -> execute(Integer.parseInt(path.substring(1)), rightStrategy);
            case UP -> execute(Integer.parseInt(path.substring(1)), upStrategy);
            default -> execute(Integer.parseInt(path.substring(1)), downStrategy);
        };
    }

    private Set<Coordinates> execute(int size, Supplier<Coordinates> strategy) {
        Set<Coordinates> coordinates = new HashSet<>();
        while (size-- > 0) {
            current = strategy.get();
            coordinates.add(current);
        }

        return coordinates;
    }

    private static record Coordinates(int row, int column) {
    }

}
