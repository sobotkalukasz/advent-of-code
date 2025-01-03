package pl.lsobotka.adventofcode.year_2024;

import java.util.*;

import pl.lsobotka.adventofcode.utils.Board;
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

    long countBestTiles() {
        return maze.countTiles();
    }

    record Maze(Board board) {

        static Maze from(List<String> input) {
            return new Maze(Board.from(input));
        }

        long getBestScore() {
            final Queue<MazePath> paths = new PriorityQueue<>(Comparator.comparingLong(MazePath::score));
            paths.add(MazePath.of(board.start(), Dir.RIGHT, 0));
            final Map<Coord, Long> scoreCache = new HashMap<>();

            long score = 0;
            while (!paths.isEmpty()) {
                final MazePath actual = paths.poll();
                final Coord actualCoord = actual.state().coord();
                if (board.isEnd(actualCoord)) {
                    score = actual.score();
                    break;
                }

                if (scoreCache.getOrDefault(actualCoord, Long.MAX_VALUE) > actual.score()) {
                    scoreCache.put(actualCoord, actual.score());
                    for (Dir dir : Dir.values()) {
                        final Coord adjacent = actualCoord.getAdjacent(dir);
                        if (!board().isWall(adjacent)) {
                            final MazePath mazePath = actual.next(adjacent, dir,
                                    rotateCost(actual.state().dir(), dir) + 1);
                            paths.add(mazePath);
                        }
                    }
                }

            }

            return score;
        }

        long countTiles() {
            final Queue<MazePath> paths = new PriorityQueue<>(Comparator.comparingLong(MazePath::score));
            paths.add(MazePath.of(board.start(), Dir.RIGHT, 0));

            final Map<State, Long> scoreCache = new HashMap<>();
            final Set<Coord> path = new HashSet<>();

            final long bestScore = getBestScore();

            while (!paths.isEmpty()) {
                final MazePath actual = paths.poll();
                if (board().isEnd(actual.state().coord())) {
                    if (actual.score() == bestScore) {
                        path.addAll(actual.path());
                    }
                    continue;
                }

                if (actual.score() < bestScore
                        && scoreCache.getOrDefault(actual.state(), Long.MAX_VALUE) >= actual.score()) {
                    scoreCache.put(actual.state(), actual.score());
                    for (Dir dir : Dir.values()) {
                        final Coord adjacent = actual.state().coord().getAdjacent(dir);
                        if (!board.isWall(adjacent)) {
                            final MazePath mazePath = actual.next(adjacent, dir,
                                    rotateCost(actual.state().dir(), dir) + 1);
                            paths.add(mazePath);
                        }
                    }
                }
            }

            return path.size();
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

    record State(Coord coord, Dir dir) {
    }

    record MazePath(State state, long score, Set<Coord> path) {
        static MazePath of(Coord coord, Dir dir, long score) {
            final Set<Coord> path = new HashSet<>();
            path.add(coord);
            return new MazePath(new State(coord, dir), score, path);
        }

        MazePath next(Coord coord, Dir dir, long score) {
            final Set<Coord> coords = new HashSet<>(path);
            coords.add(coord);
            return new MazePath(new State(coord, dir), this.score + score, coords);
        }
    }
}
