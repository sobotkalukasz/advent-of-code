package pl.lsobotka.adventofcode.year_2024;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

/*
 * https://adventofcode.com/2024/day/16
 * */
public class ReindeerMaze {

    private final Maze maze;

    ReindeerMaze(final List<String> input) {
        maze = Maze.from(input);
    }

    long getBestScoreFromMaze() {
        return maze.getBestScore();
    }

    record Maze(Set<Coord> walls, Coord start, Coord finish) {

        static Maze from(List<String> input) {
            final Set<Coord> walls = new HashSet<>();
            Coord start = null;
            Coord finish = null;

            for (int row = 0; row < input.size(); row++) {
                final String rowS = input.get(row);
                for (int col = 0; col < rowS.length(); col++) {
                    switch (rowS.charAt(col)) {
                    case '#' -> walls.add(Coord.of(row, col));
                    case 'E' -> finish = Coord.of(row, col);
                    case 'S' -> start = Coord.of(row, col);
                    default -> { // Do nothing
                    }
                    }
                }
            }
            return new Maze(walls, start, finish);
        }

        long getBestScore() {
            final Queue<MazePath> paths = new PriorityQueue<>(Comparator.comparingLong(MazePath::score));
            paths.add(new MazePath(start, Dir.RIGHT, 0));
            final Map<Coord, Long> scoreCache = new HashMap<>();

            long score = 0;
            while (!paths.isEmpty()) {
                final MazePath actual = paths.poll();
                if (actual.coord().equals(finish)) {
                    score = actual.score();
                    break;
                }

                if (scoreCache.getOrDefault(actual.coord(), Long.MAX_VALUE) > actual.score()) {
                    scoreCache.put(actual.coord(), actual.score());
                    for (Dir dir : Dir.values()) {
                        final Coord adjacent = actual.coord().getAdjacent(dir);
                        if (!walls.contains(adjacent)) {
                            final MazePath mazePath = new MazePath(adjacent, dir,
                                    actual.score() + 1 + rotateCost(actual.dir(), dir));
                            paths.add(mazePath);
                        }
                    }
                }

            }

            return score;
        }

        private long rotateCost(final Dir from, final Dir to) {
            return switch (from) {
                case UP -> switch (to) {
                    case DOWN -> 2000;
                    case LEFT, RIGHT -> 1000;
                    default -> 0;
                };
                case DOWN -> switch (to) {
                    case UP -> 2000;
                    case LEFT, RIGHT -> 1000;
                    default -> 0;
                };
                case LEFT -> switch (to) {
                    case RIGHT -> 2000;
                    case UP, DOWN -> 1000;
                    default -> 0;
                };
                case RIGHT -> switch (to) {
                    case LEFT -> 2000;
                    case UP, DOWN -> 1000;
                    default -> 0;
                };
            };
        }
    }

    record MazePath(Coord coord, Dir dir, long score) {

    }
}
