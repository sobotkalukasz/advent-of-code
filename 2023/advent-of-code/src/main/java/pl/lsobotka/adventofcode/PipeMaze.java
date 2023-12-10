package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2023/day/10
 * */
public class PipeMaze {
    private final Maze maze;

    public PipeMaze(List<String> input) {
        this.maze = Pipe.maze(input);
    }

    int solveIt() {
        final int stepsToSolve = maze.getRequiredStepsToSolve();
        return stepsToSolve / 2;
    }

    record Maze(Map<Coord, Pipe> mazeMap, Pipe start) {

        int getRequiredStepsToSolve() {
            final List<Coord> possible = start.next().stream().filter(to -> isConnected(start.coord, to)).toList();

            final Set<Coord> solved = new HashSet<>();

            for (Coord next : possible) {
                if (!solved.contains(next)) {
                    final List<Coord> solveHistory = getHistory(next,
                            new ArrayList<>(Collections.singletonList(start.coord)));
                    solved.addAll(solveHistory);
                }
            }
            return solved.size();
        }

        private List<Coord> getHistory(final Coord from, final List<Coord> history) {

            Pipe actual = mazeMap.get(from);
            Coord previous = history.getFirst();
            while (actual != null && actual != start) {
                history.add(actual.coord);
                final Coord next = actual.next(previous);

                previous = actual.coord;
                actual = mazeMap.get(next);
            }

            return start.equals(actual) ? history : Collections.emptyList();
        }

        private boolean isConnected(Coord from, Coord to) {
            return mazeMap.getOrDefault(to, Pipe.empty(to)).next().contains(from);
        }

    }

    record Pipe(Symbol symbol, Coord coord) {

        static Maze maze(final List<String> input) {
            final Map<Coord, Pipe> maze = new HashMap<>();
            Pipe start = null;

            for (int row = 0; row < input.size(); row++) {
                final char[] rowArray = input.get(row).toCharArray();
                for (int col = 0; col < rowArray.length; col++) {
                    final Symbol symbol = Symbol.from(rowArray[col]);
                    if (symbol != Symbol.SPACE) {
                        final Coord coord = new Coord(row, col);
                        final Pipe value = new Pipe(symbol, coord);
                        if (symbol == Symbol.S) {
                            start = value;
                        }
                        maze.put(coord, value);
                    }
                }
            }

            return new Maze(maze, start);
        }

        static Pipe empty(final Coord coord) {
            return new Pipe(Symbol.SPACE, coord);
        }

        Coord next(Coord from) {
            final List<Coord> next = this.next();
            next.remove(from);
            return next.getFirst();
        }

        List<Coord> next() {
            return coord.getAdjacent(symbol.directions);
        }
    }

    enum Symbol {
        S('S', List.of(Dir.values())), //
        UP_DOWN('|', List.of(Dir.UP, Dir.DOWN)), //
        UP_RIGHT('L', List.of(Dir.UP, Dir.RIGHT)), //
        LEFT_RIGHT('-', List.of(Dir.LEFT, Dir.RIGHT)), //
        LEFT_UP('J', List.of(Dir.LEFT, Dir.UP)), //
        LEFT_DOWN('7', List.of(Dir.LEFT, Dir.DOWN)), //
        RIGHT_DOWN('F', List.of(Dir.RIGHT, Dir.DOWN)), //
        SPACE('.', Collections.emptyList());

        private final char symbol;
        private final List<Dir> directions;

        Symbol(char symbol, List<Dir> directions) {
            this.symbol = symbol;
            this.directions = directions;
        }

        static Symbol from(final char c) {
            return Stream.of(Symbol.values())
                    .filter(s -> s.symbol == c)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find Symbol for: " + c));
        }
    }
}
