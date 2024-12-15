package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2024/day/15
 * */
public class WarehouseWoes {
    private final Warehouse warehouse;

    private WarehouseWoes(final Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    static WarehouseWoes small(final List<String> input) {
        return new WarehouseWoes(Warehouse.small(input));
    }

    static WarehouseWoes big(final List<String> input) {
        return new WarehouseWoes(Warehouse.big(input));
    }

    long sumOfBoxesCoordinate() {
        final Warehouse actual = warehouse.applyMoves();
        return actual.sumBoxesCoordinates();
    }

    record Warehouse(Set<Coord> walls, Set<Box> boxes, Robot robot) {
        static Warehouse small(final List<String> input) {
            return createWarehouse(input, 1);
        }

        static Warehouse big(final List<String> input) {
            return createWarehouse(input, 2);
        }

        private static Warehouse createWarehouse(final List<String> input, final int scalingFactor) {
            final Set<Coord> walls = new HashSet<>();
            final Set<Box> boxes = new HashSet<>();
            final Queue<Dir> moves = new ArrayDeque<>();
            Coord robotPos = null;

            for (int row = 0; row < input.size(); row++) {
                final String rowS = input.get(row);
                for (int col = 0; col < rowS.length(); col++) {
                    final int scaledCol = col * scalingFactor;
                    switch (rowS.charAt(col)) {
                    case '#' -> {
                        for (int i = 0; i < scalingFactor; i++) {
                            walls.add(Coord.of(row, scaledCol + i));
                        }
                    }
                    case 'O' -> {
                        final Box box = Box.from(Coord.of(row, scaledCol), scalingFactor);
                        boxes.add(box);
                    }
                    case '@' -> robotPos = Coord.of(row, scaledCol);
                    case '<' -> moves.add(Dir.LEFT);
                    case '>' -> moves.add(Dir.RIGHT);
                    case '^' -> moves.add(Dir.UP);
                    case 'v' -> moves.add(Dir.DOWN);
                    default -> { // Do nothing
                    }
                    }
                }
            }

            return new Warehouse(walls, boxes, new Robot(robotPos, moves));
        }

        Warehouse applyMoves() {
            final Map<Coord, Box> boxCoords = computeBoxCoords();
            Robot current = robot.copy();

            while (current.moveAvailable()) {
                final Dir dir = current.next();
                final Set<Box> boxesToMove = new HashSet<>();
                Set<Coord> next = new HashSet<>();
                next.add(current.pos().getAdjacent(dir));
                boolean wall = false;

                while (!next.isEmpty()) {
                    if (next.stream().anyMatch(walls::contains)) {
                        wall = true;
                        break;
                    }
                    final Set<Box> stepBoxes = next.stream()
                            .filter(boxCoords::containsKey)
                            .map(boxCoords::get)
                            .collect(Collectors.toSet());

                    if (!stepBoxes.isEmpty()) {
                        boxesToMove.addAll(stepBoxes);
                        next = new HashSet<>();
                        for (Box b : stepBoxes) {
                            switch (dir) {
                            case UP, DOWN -> {
                                next.add(b.left().getAdjacent(dir));
                                next.add(b.right().getAdjacent(dir));
                            }
                            case LEFT -> next.add(b.left().getAdjacent(dir));
                            case RIGHT -> next.add(b.right().getAdjacent(dir));
                            }
                        }
                    } else {
                        next = Collections.emptySet();
                    }
                }

                if (!wall) {
                    boxesToMove.forEach(b -> {
                        boxCoords.remove(b.left());
                        boxCoords.remove(b.right());
                    });

                    boxesToMove.forEach(b -> {
                        final Box moved = b.move(dir);
                        boxCoords.put(moved.left(), moved);
                        boxCoords.put(moved.right(), moved);
                    });

                    current = current.move(dir);
                }
            }

            return new Warehouse(walls, new HashSet<>(boxCoords.values()), current);
        }

        long sumBoxesCoordinates() {
            return boxes.stream().mapToLong(Box::gpsPosition).sum();
        }

        private Map<Coord, Box> computeBoxCoords() {
            Map<Coord, Box> boxMap = new HashMap<>();
            for (Box b : boxes) {
                boxMap.put(b.left(), b);
                boxMap.put(b.right(), b);
            }
            return boxMap;
        }
    }

    record Robot(Coord pos, Queue<Dir> moves) {

        boolean moveAvailable() {
            return !moves.isEmpty();
        }

        Dir next() {
            return moves.poll();
        }

        Robot copy() {
            return new Robot(pos(), new ArrayDeque<>(moves));
        }

        Robot move(Dir dir) {
            return new Robot(pos().getAdjacent(dir), moves);
        }

    }

    record Box(Coord left, Coord right) {
        static Box from(Coord coord, int scalingFactor) {
            if (scalingFactor == 2) {
                return new Box(coord, coord.getAdjacent(Dir.RIGHT));
            }
            return new Box(coord, coord);
        }

        long gpsPosition() {
            return left.row() * 100L + left.col();
        }

        Box move(final Dir dir) {
            return new Box(left.getAdjacent(dir), right.getAdjacent(dir));
        }
    }
}
