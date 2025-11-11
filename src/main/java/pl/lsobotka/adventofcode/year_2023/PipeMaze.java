package pl.lsobotka.adventofcode.year_2023;

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

    int farthestStep() {
        final List<Coord> loop = maze.getLoop();
        return loop.size() / 2;
    }

    int countEnclosedTiles() {
        final HashMap<Coord, Pipe> mazeMap = new HashMap<>(maze.mazeMap);
        final List<Coord> loop = maze.getLoop();

        final Pipe start = mazeMap.get(loop.getFirst());
        mazeMap.put(start.coord, start.replaceStarSymbol(loop.get(1), loop.getLast()));

        final NestFinder nestFinder = NestFinder.fromLoop(loop, mazeMap);
        return nestFinder.enclosed.size();
    }

    record NestFinder(Set<Coord> loop, Set<Coord> enclosed, Set<Coord> canEscape, int maxRow, int maxCol,
                      Map<Coord, Pipe> mazeMap) {

        private NestFinder(List<Coord> loop, int maxRow, int maxCol, Map<Coord, Pipe> mazeMap) {
            this(new HashSet<>(loop), new HashSet<>(), new HashSet<>(), maxRow, maxCol, mazeMap);
            evaluate();
        }

        static NestFinder fromLoop(final List<Coord> loop, Map<Coord, Pipe> mazeMap) {
            final int maxRow = loop.stream().map(Coord::row).reduce(0, Integer::max);
            final int maxCol = loop.stream().map(Coord::col).reduce(0, Integer::max);
            return new NestFinder(loop, maxRow, maxCol, mazeMap);
        }

        private void evaluate() {
            for (int row = 0; row <= maxRow; row++) {
                for (int col = 0; col <= maxCol; col++) {
                    final Coord toEvaluate = new Coord(row, col);
                    if (loop.contains(toEvaluate) || enclosed.contains(toEvaluate) || canEscape.contains(toEvaluate)) {
                        continue;
                    }
                    evaluate(toEvaluate);
                }
            }
        }

        private void evaluate(final Coord coord) {

            final List<Coord> next = new ArrayList<>(List.of(coord));
            final Set<Coord> evaluated = new HashSet<>();

            while (!next.isEmpty()) {
                final Coord actual = next.removeFirst();
                evaluated.add(actual);

                final Set<Coord> directAdjacent = actual.getDirectAdjacent();
                for (Coord adjacent : directAdjacent) {
                    if (!isWithinMazeRange(adjacent) || canEscape.contains(adjacent)) {
                        canEscape.addAll(evaluated);
                        return;
                    } else if (enclosed.contains(adjacent)) {
                        enclosed.addAll(evaluated);
                        return;
                    } else if (loop.contains(adjacent) || evaluated.contains(adjacent) || next.contains(adjacent)) {
                        continue;
                    }
                    next.add(adjacent);
                }
            }

            final boolean insideLoop = evaluated.stream().anyMatch(this::isInsideLoop);
            if (insideLoop) {
                enclosed.addAll(evaluated);
            } else {
                canEscape.addAll(evaluated);
            }

        }

        private boolean isWithinMazeRange(final Coord coord) {
            final int row = coord.row();
            final int col = coord.col();

            return row >= 0 && row <= maxRow && col >= 0 && col <= maxCol;
        }

        private boolean isInsideLoop(final Coord coord) {
            return testIfInside(coord, Dir.LEFT)  //
                    && testIfInside(coord, Dir.RIGHT) //
                    && testIfInside(coord, Dir.UP) //
                    && testIfInside(coord, Dir.DOWN);
        }

        private boolean testIfInside(final Coord coord, Dir dir) {
            final List<Symbol> symbols = getPipes(coord, dir);

            Symbol lastCorner = Symbol.SPACE;
            boolean inside = false;

            for (Symbol actual : symbols) {
                if (Symbol.UP_DOWN == actual && !lastCorner.corner) {
                    if (Dir.LEFT == dir || Dir.RIGHT == dir) {
                        inside = !inside;
                    }
                } else if (Symbol.LEFT_RIGHT == actual && !lastCorner.corner) {
                    if (Dir.UP == dir || Dir.DOWN == dir) {
                        inside = !inside;
                    }
                } else if (actual.corner) {
                    if (lastCorner.corner) {
                        if (lastCorner == Symbol.LEFT_DOWN && actual == Symbol.UP_RIGHT
                                || lastCorner == Symbol.UP_RIGHT && actual == Symbol.LEFT_DOWN
                                || lastCorner == Symbol.RIGHT_DOWN && actual == Symbol.LEFT_UP
                                || lastCorner == Symbol.LEFT_UP && actual == Symbol.RIGHT_DOWN) {
                            inside = !inside;
                        }
                        lastCorner = Symbol.SPACE;
                    } else {
                        lastCorner = actual;
                    }
                }
            }

            return inside;

        }

        private List<Symbol> getPipes(final Coord coord, final Dir dir) {
            final List<Coord> coordToMazeEdge = new ArrayList<>();
            Coord actual = coord;
            while (isWithinMazeRange(actual)) {
                final Coord next = actual.getAdjacent(dir);
                coordToMazeEdge.add(next);
                actual = next;
            }

            coordToMazeEdge.removeIf(c -> !loop.contains(c));
            return coordToMazeEdge.stream().map(mazeMap::get).map(Pipe::symbol).toList();
        }

    }

    record Maze(Map<Coord, Pipe> mazeMap, Pipe start) {

        List<Coord> getLoop() {
            final List<Coord> possible = start.next().stream().filter(to -> isConnected(start.coord, to)).toList();

            for (Coord next : possible) {
                final List<Coord> solveHistory = getHistory(next,
                        new ArrayList<>(Collections.singletonList(start.coord)));
                if (!solveHistory.isEmpty()) {
                    return solveHistory;
                }
            }

            return Collections.emptyList();
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
            final List<Coord> next = new ArrayList<>(this.next());
            next.remove(from);
            return next.getFirst();
        }

        List<Coord> next() {
            return coord.getAdjacent(symbol.directions);
        }

        Pipe replaceStarSymbol(final Coord from, final Coord to) {
            final Symbol newSymbol = symbol.determineCommonSymbol(coord, from, to);
            return new Pipe(newSymbol, coord);
        }
    }

    enum Symbol {
        S('S', List.of(Dir.values()), false), //
        UP_DOWN('|', List.of(Dir.UP, Dir.DOWN), false), //
        UP_RIGHT('L', List.of(Dir.UP, Dir.RIGHT), true), //
        LEFT_RIGHT('-', List.of(Dir.LEFT, Dir.RIGHT), false), //
        LEFT_UP('J', List.of(Dir.LEFT, Dir.UP), true), //
        LEFT_DOWN('7', List.of(Dir.LEFT, Dir.DOWN), true), //
        RIGHT_DOWN('F', List.of(Dir.RIGHT, Dir.DOWN), true), //
        SPACE('.', Collections.emptyList(), false);

        private final char symbol;
        private final List<Dir> directions;
        private final boolean corner;

        Symbol(char symbol, List<Dir> directions, boolean corner) {
            this.symbol = symbol;
            this.directions = directions;
            this.corner = corner;
        }

        static Symbol from(final char c) {
            return Stream.of(Symbol.values())
                    .filter(s -> s.symbol == c)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unable to find Symbol for: " + c));
        }

        Symbol determineCommonSymbol(final Coord actual, final Coord from, final Coord to) {

            final List<Coord> coords = List.of(from, to);
            if (new HashSet<>(actual.getAdjacent(UP_DOWN.directions)).containsAll(coords)) {
                return UP_DOWN;
            } else if (new HashSet<>(actual.getAdjacent(LEFT_RIGHT.directions)).containsAll(coords)) {
                return LEFT_RIGHT;
            } else if (new HashSet<>(actual.getAdjacent(UP_RIGHT.directions)).containsAll(coords)) {
                return UP_RIGHT;
            } else if (new HashSet<>(actual.getAdjacent(LEFT_UP.directions)).containsAll(coords)) {
                return LEFT_UP;
            } else if (new HashSet<>(actual.getAdjacent(LEFT_DOWN.directions)).containsAll(coords)) {
                return LEFT_DOWN;
            } else if (new HashSet<>(actual.getAdjacent(RIGHT_DOWN.directions)).containsAll(coords)) {
                return RIGHT_DOWN;
            }

            throw new IllegalArgumentException("Unable to determine common symbol");
        }
    }
}
