package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2019/day/15
 * */
public class OxygenSystem {

    final long[] program;
    final Map<Coordinate, Status> spaceMap;

    public OxygenSystem(long[] program) {
        this.program = program;
        this.spaceMap = new HashMap<>();
    }

    public int drawMapAndFindOxygen() {
        this.spaceMap.put(Coordinate.start(), Status.START);

        final PriorityQueue<State> states = new PriorityQueue<>();
        states.add(new State(Collections.emptyList(), Coordinate.start(), new IntCode(program)));

        int oxygenPath = 0;

        while (!states.isEmpty()) {
            final State actualState = states.poll();
            final IntStream values = Arrays.stream(Direction.values())
                    .mapToInt(dir -> checkPath(states, actualState, dir));
            if (oxygenPath == 0) {
                oxygenPath = values.filter(v -> v > 0).min().orElse(0);
            }
        }
        //printMap(spaceMap);
        return oxygenPath;
    }

    private int checkPath(final PriorityQueue<State> states, final State actualState, final Direction direction) {
        final Coordinate actual = actualState.actual().apply(direction);

        if (!this.spaceMap.containsKey(actual)) {
            final IntCode intCode = actualState.intCode();
            final Status status = applyMove(intCode, direction);
            this.spaceMap.put(actual, status);
            if (status != Status.WALL) {
                final List<Direction> path = actualState.path();
                path.add(direction);
                states.add(new State(path, actual, intCode));
            }
            if (status == Status.OXYGEN) {
                return actualState.path.size() + 1;
            }
        }
        return 0;
    }

    private Status applyMove(final IntCode intCode, final Direction direction) {
        intCode.addInput(direction.value);
        final List<Long> output = intCode.executeUntilOutput();
        return Status.getStatusByCode(output.get(0));
    }

    private void printMap(final Map<Coordinate, Status> spaceMap) {

        final int minRow = spaceMap.keySet().stream().mapToInt(Coordinate::row).min().orElse(0);
        final int maxRow = spaceMap.keySet().stream().mapToInt(Coordinate::row).max().orElse(0);
        final int minCol = spaceMap.keySet().stream().mapToInt(Coordinate::column).min().orElse(0);
        final int maxCol = spaceMap.keySet().stream().mapToInt(Coordinate::column).max().orElse(0);

        final StringBuilder printout = new StringBuilder();
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                final Status status = spaceMap.getOrDefault(new Coordinate(row, col), Status.TO_CHECK);
                printout.append("[").append(status.print).append("] ");
            }
            printout.append("\n");
        }
        System.out.println(printout);
    }

    private record State(List<Direction> path, Coordinate actual, IntCode intCode) implements Comparable<State> {

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.path.size(), other.path.size());
        }

        @Override
        public List<Direction> path() {
            return new ArrayList<>(path);
        }

        @Override
        public IntCode intCode() {
            return intCode.copy();
        }
    }

    enum Direction {
        NORTH(1), SOUTH(2), WEST(3), EAST(4);

        final int value;

        Direction(int value) {
            this.value = value;
        }
    }

    enum Status {
        WALL(0, "#"), EMPTY(1, "."), OXYGEN(2, "O"), TO_CHECK(-1, " "), START(-2, "X");

        final int code;
        final String print;

        Status(int code, String print) {
            this.code = code;
            this.print = print;
        }

        public static Status getStatusByCode(final long code) {
            return Arrays.stream(Status.values())
                    .filter(s -> s.code == code)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    record Coordinate(int row, int column) {
        public static Coordinate start() {
            return new Coordinate(0, 0);
        }

        public static Coordinate of(final List<Direction> dir) {
            Coordinate actual = Coordinate.start();
            for (Direction direction : dir) {
                actual = actual.apply(direction);
            }
            return actual;
        }

        public Coordinate apply(final Direction dir) {
            return switch (dir) {
                case NORTH -> new Coordinate(row - 1, column);
                case EAST -> new Coordinate(row, column + 1);
                case SOUTH -> new Coordinate(row + 1, column);
                case WEST -> new Coordinate(row, column - 1);
            };
        }
    }
}
