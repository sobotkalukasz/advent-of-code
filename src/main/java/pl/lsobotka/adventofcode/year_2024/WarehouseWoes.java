package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2024/day/15
 * */
public class WarehouseWoes {
    private final Warehouse warehouse;

    WarehouseWoes(final List<String> input) {
        this.warehouse = Warehouse.from(input);
    }

    long sumOfBoxesCoordinate() {
        warehouse.applyMoves();
        return warehouse.sumBoxesCoordinates();
    }

    record Warehouse(Set<Coord> walls, Set<Coord> boxes, Robot robot) {
        static Warehouse from(final List<String> input) {
            final Set<Coord> walls = new HashSet<>();
            final Set<Coord> boxes = new HashSet<>();
            final Queue<Dir> moves = new ArrayDeque<>();
            Coord robotPos = null;

            for (int row = 0; row < input.size(); row++) {
                final String rowS = input.get(row);
                for (int col = 0; col < rowS.length(); col++) {
                    switch (rowS.charAt(col)) {
                    case '#' -> walls.add(Coord.of(row, col));
                    case 'O' -> boxes.add(Coord.of(row, col));
                    case '@' -> robotPos = Coord.of(row, col);
                    case '<' -> moves.add(Dir.LEFT);
                    case '>' -> moves.add(Dir.RIGHT);
                    case '^' -> moves.add(Dir.UP);
                    case 'v' -> moves.add(Dir.DOWN);
                    default -> { //do nothing
                    }
                    }

                }
            }

            return new Warehouse(walls, boxes, new Robot(robotPos, moves));
        }

        void applyMoves() {
            Robot current = robot;

            while (current.moveAvailable()) {
                final Dir dir = current.next();
                final Set<Coord> boxesToMove = new HashSet<>();
                Coord next = current.pos().getAdjacent(dir);
                Coord wall = null;

                while (next != null) {
                    if (boxes.contains(next)) {
                        boxesToMove.add(next);
                        next = next.getAdjacent(dir);
                    } else if (walls.contains(next)) {
                        wall = next;
                        next = null;
                    } else {
                        next = null;
                    }
                }

                if (wall == null) {
                    boxes.removeIf(boxesToMove::contains);
                    boxesToMove.forEach(b -> boxes.add(b.getAdjacent(dir)));
                    current = new Robot(current.pos().getAdjacent(dir), current.moves());
                }
                //printWarehouseMap(dir, current);
            }

            new Warehouse(walls, boxes, current);
        }

        long sumBoxesCoordinates() {
            return boxes.stream().mapToLong(c -> c.row() * 100L + c.col()).sum();
        }

        void printWarehouseMap(final Dir dir, final Robot robot) {
            final int maxCol = walls.stream().mapToInt(Coord::col).max().orElseGet(() -> 0);
            final int maxRow = walls.stream().mapToInt(Coord::row).max().orElseGet(() -> 0);
            final StringBuilder map = new StringBuilder();
            map.append("Warehouse after move: ").append(dir.name()).append("\n");
            for (int row = 0; row <= maxRow; row++) {
                for (int col = 0; col <= maxCol; col++) {
                    final Coord c = Coord.of(row, col);
                    if (robot.pos.equals(c)) {
                        map.append("@");
                    } else if (walls.contains(c)) {
                        map.append("#");
                    } else if (boxes.contains(c)) {
                        map.append("O");
                    } else {
                        map.append(".");
                    }
                }
                map.append("\n");
            }
            map.append("\n");
            System.out.println(map);
        }
    }

    record Robot(Coord pos, Queue<Dir> moves) {

        boolean moveAvailable() {
            return !moves.isEmpty();
        }

        Dir next() {
            return moves.poll();
        }

    }
}
