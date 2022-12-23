package pl.lsobotka.adventofcode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/23
 * */
public class UnstableDiffusion {

    Map<Integer, Coord> initialPositions;

    public UnstableDiffusion(final List<String> input) {
        this.initialPositions = getInitialPositions(input);
    }

    public long countEmptySpaceAfterMoves() {
        return execute(Result.EMPTY_SIZE);
    }

    public long countMovesToNoAdjacent() {
        return execute(Result.ROUND_COUNT);
    }

    public long execute(final Result type) {
        int roundCounter = 1;
        final Map<Integer, Coord> actualPositions = new HashMap<>(initialPositions);
        final LinkedList<Dir> dirs = new LinkedList<>(Dir.getDirs());

        boolean searching = true;
        while (searching) {
            final Collection<Coord> occupied = actualPositions.values();

            final List<Integer> toMove = actualPositions.entrySet()
                    .stream()
                    .filter(e -> e.getValue().getAllAdjacent().stream().anyMatch(occupied::contains))
                    .map(Map.Entry::getKey)
                    .toList();
            if (toMove.isEmpty()) {
                break;
            }

            final Map<Integer, Coord> possible = new HashMap<>();
            for (Integer elf : toMove) {
                final Coord move = getMove(occupied, actualPositions.get(elf), dirs);
                if (move != null) {
                    possible.put(elf, move);
                }
            }

            final Map<Coord, Long> occur = possible.values()
                    .stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            possible.forEach((elf, c) -> {
                if (occur.get(c) == 1) {
                    actualPositions.put(elf, c);
                }
            });

            dirs.addLast(dirs.removeFirst());
            roundCounter++;
            if (type == Result.EMPTY_SIZE && roundCounter > 10) {
                searching = false;
            }
        }

        final long result;
        if (type == Result.ROUND_COUNT) {
            result = roundCounter;
        } else {
            final List<Coord> positions = actualPositions.values().stream().toList();
            final long minRow = positions.stream().mapToLong(Coord::row).min().orElse(0);
            final long maxRow = positions.stream().mapToLong(Coord::row).max().orElse(0);
            final long minCol = positions.stream().mapToLong(Coord::col).min().orElse(0);
            final long maxCol = positions.stream().mapToLong(Coord::col).max().orElse(0);
            final long size = (Math.abs(minRow - maxRow) + 1) * (Math.abs(minCol - maxCol) + 1);

            result = size - actualPositions.size();
        }
        return result;
    }

    private Coord getMove(final Collection<Coord> occupied, Coord current, final List<Dir> dirs) {
        Coord coord = null;
        for (Dir dir : dirs) {
            final boolean canMove = current.getAdjacentFor(dir).stream().noneMatch(occupied::contains);
            if (canMove) {
                coord = current.getFor(dir);
                break;
            }
        }

        return coord;
    }

    record Coord(int row, int col) {

        static Coord of(final int row, final int col) {
            return new Coord(row, col);
        }

        Coord getFor(final Dir dir) {
            return switch (dir) {
                case N -> Coord.of(row - 1, col);
                case S -> Coord.of(row + 1, col);
                case W -> Coord.of(row, col - 1);
                case E -> Coord.of(row, col + 1);
            };
        }

        Set<Coord> getAllAdjacent() {
            final Set<Coord> adjacent = getNorth();
            adjacent.addAll(getEast());
            adjacent.addAll(getSouth());
            adjacent.addAll(getWest());
            return adjacent;
        }

        Set<Coord> getAdjacentFor(final Dir dir) {
            return switch (dir) {
                case N -> getNorth();
                case S -> getSouth();
                case W -> getWest();
                case E -> getEast();
            };
        }

        Set<Coord> getNorth() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(row - 1, col - 1));
            adjacent.add(Coord.of(row - 1, col));
            adjacent.add(Coord.of(row - 1, col + 1));
            return adjacent;
        }

        Set<Coord> getEast() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(row + 1, col + 1));
            adjacent.add(Coord.of(row, col + 1));
            adjacent.add(Coord.of(row - 1, col + 1));
            return adjacent;
        }

        Set<Coord> getSouth() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(row + 1, col - 1));
            adjacent.add(Coord.of(row + 1, col));
            adjacent.add(Coord.of(row + 1, col + 1));
            return adjacent;
        }

        Set<Coord> getWest() {
            final Set<Coord> adjacent = new HashSet<>();
            adjacent.add(Coord.of(row + 1, col - 1));
            adjacent.add(Coord.of(row, col - 1));
            adjacent.add(Coord.of(row - 1, col - 1));
            return adjacent;
        }
    }

    enum Dir {
        N, S, W, E;

        static List<Dir> getDirs() {
            return List.of(N, S, W, E);
        }
    }

    enum Result {
        EMPTY_SIZE, ROUND_COUNT
    }

    private Map<Integer, Coord> getInitialPositions(List<String> input) {
        final Map<Integer, Coord> initialPositions = new HashMap<>();
        int elfCounter = 0;
        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            for (int col = 0; col < rowString.length(); col++) {
                if (rowString.charAt(col) == '#') {
                    initialPositions.put(elfCounter++, Coord.of(row, col));
                }
            }
        }
        return initialPositions;
    }
}
