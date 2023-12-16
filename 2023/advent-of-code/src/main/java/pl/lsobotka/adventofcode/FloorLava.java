package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

public class FloorLava {

    private final Cave cave;

    public FloorLava(final List<String> input) {
        this.cave = Cave.form(input);
    }

    long countEnergizedTiles() {
        return cave.beam(Coord.of(0, 0), Dir.RIGHT);
    }

    long maxEnergizedTiles() {
        return cave.countMaxTiles();
    }

    record Cave(Map<Coord, Tile> caveMap, Coord max) {

        static Cave form(final List<String> input) {
            final Map<Coord, Tile> caveMap = new HashMap<>();
            int maxRow = input.size() - 1;
            int maxCol = input.get(0).length() - 1;

            for (int row = 0; row < input.size(); row++) {
                final String rowString = input.get(row);
                for (int col = 0; col < rowString.length(); col++) {
                    final Mirror mirror = Mirror.from(rowString.charAt(col));
                    final Coord coord = Coord.of(row, col);
                    caveMap.put(coord, new Tile(coord, mirror));
                }
            }
            return new Cave(caveMap, Coord.of(maxRow, maxCol));
        }

        public long countMaxTiles() {
            final Set<Long> counts = new HashSet<>();
            for (int i = 0; i <= max.row(); i++) {
                counts.add(beam(Coord.of(i, 0), Dir.RIGHT));
                counts.add(beam(Coord.of(i, max.col()), Dir.LEFT));
                counts.add(beam(Coord.of(0, i), Dir.DOWN));
                counts.add(beam(Coord.of(max.row(), i), Dir.UP));
            }

            return counts.stream().reduce(0L, Long::max);
        }

        public long beam(final Coord start, final Dir dir) {
            final Set<Coord> visited = new HashSet<>();

            final Set<TileHistory> tileHistories = new HashSet<>();
            tileHistories.add(new TileHistory(start, dir, dir));

            final Deque<BeamPath> paths = new LinkedList<>();
            paths.add(BeamPath.start(start, dir));

            while (!paths.isEmpty()) {
                final BeamPath actual = paths.removeFirst();
                final Tile tile = caveMap.get(actual.position);
                for (Dir nextDir : tile.beam(actual.dir)) {
                    final Coord nextPosition = actual.nextPosition(nextDir);
                    if (caveMap.containsKey(nextPosition)) {
                        if (tileHistories.contains(new TileHistory(nextPosition, actual.dir, nextDir))) {
                            visited.addAll(actual.path);
                        } else {
                            paths.add(actual.nextPath(nextPosition, nextDir));
                            tileHistories.add(new TileHistory(nextPosition, actual.dir, nextDir));
                        }
                    } else {
                        visited.addAll(actual.path);
                    }
                }
            }

            return visited.size();
        }
    }

    record Tile(Coord coord, Mirror mirror) {

        List<Dir> beam(final Dir dir) {
            return mirror.next(dir);
        }

    }

    enum Mirror {
        EMPTY('.'), MIRROR_L('\\'), MIRROR_R('/'), SPLIT_V('|'), SPLIT_H('-');

        final char symbol;

        Mirror(char symbol) {
            this.symbol = symbol;
        }

        List<Dir> next(final Dir from) {
            return switch (this) {
                case EMPTY -> List.of(from);
                case MIRROR_L -> switch (from) {
                    case UP -> List.of(Dir.LEFT);
                    case DOWN -> List.of(Dir.RIGHT);
                    case LEFT -> List.of(Dir.UP);
                    case RIGHT -> List.of(Dir.DOWN);
                };
                case MIRROR_R -> switch (from) {
                    case UP -> List.of(Dir.RIGHT);
                    case DOWN -> List.of(Dir.LEFT);
                    case LEFT -> List.of(Dir.DOWN);
                    case RIGHT -> List.of(Dir.UP);
                };
                case SPLIT_H -> switch (from) {
                    case UP, DOWN -> List.of(Dir.LEFT, Dir.RIGHT);
                    case RIGHT, LEFT -> List.of(from);
                };
                case SPLIT_V -> switch (from) {
                    case UP, DOWN -> List.of(from);
                    case RIGHT, LEFT -> List.of(Dir.UP, Dir.DOWN);
                };
            };
        }

        static Mirror from(final char symbol) {
            return Arrays.stream(Mirror.values())
                    .filter(m -> m.symbol == symbol)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown mirror symbol: " + symbol));
        }
    }

    record BeamPath(Coord position, Dir dir, List<Coord> path) {
        BeamPath nextPath(final Coord position, final Dir dir) {
            final List<Coord> path = new ArrayList<>(this.path);
            path.add(position);
            return new BeamPath(position, dir, path);
        }

        static BeamPath start(Coord coord, Dir dir) {
            return new BeamPath(coord, dir, new ArrayList<>(List.of(coord)));
        }

        Coord nextPosition(Dir dir) {
            return position.getAdjacent(dir);
        }
    }

    record TileHistory(Coord coord, Dir from, Dir to) {
    }
}
