package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;

public class ChristmasTreeFarm {

    final List<Present> presents;
    final List<Grid> grids;

    ChristmasTreeFarm(final List<String> input) {
        this.presents = new ArrayList<>();
        this.grids = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            final String row = input.get(i);
            if (row.isEmpty()) {
                continue;
            }

            if (row.contains("x")) {
                grids.add(Grid.from(row));
            } else if (row.contains(":")) {
                for (int j = 0; j < input.size(); j++) {
                    if (input.get(i + j).isBlank()) {
                        presents.add(Present.from(input.subList(i, i + j)));
                        i += j;
                        break;
                    }
                }
            }
        }
    }

    long countRegions() {
        return grids.stream()
                .filter(g -> g.fits(presents))
                .filter(this::testIfCanFit)
                .count();
    }

    boolean testIfCanFit(final Grid grid) {
        int width = grid.width();
        int height = grid.height();
        int cells = width * height;

        int[] areaById = new int[grid.qty().length];
        for (Present p : presents) {
            Set<Coord> base = p.coords().values().iterator().next();
            areaById[p.id()] = base.size();
        }

        int totalAreaNeeded = 0;
        for (int id = 0; id < grid.qty().length; id++) {
            totalAreaNeeded += grid.qty()[id] * areaById[id];
        }

        List<BitSet>[] placementsById = new List[grid.qty().length];
        for (int i = 0; i < placementsById.length; i++) {
            placementsById[i] = new ArrayList<>();
        }

        for (Present p : presents) {
            int id = p.id();
            for (Set<Coord> shape : p.coords().values()) {
                precomputePlacementsForShape(shape, width, height, placementsById[id]);
            }
        }

        int[] idOrder = new int[grid.qty().length];
        for (int i = 0; i < idOrder.length; i++) {
            idOrder[i] = i;
        }

        BitSet used = new BitSet(cells);
        int[] remaining = grid.qty().clone();
        int remainingArea = totalAreaNeeded;

        return backtrack(remaining, areaById, idOrder, placementsById, used, cells, remainingArea);
    }

    private static boolean backtrack(final int[] remaining, final int[] areaById, final int[] idOrder,
            final List<BitSet>[] placementsById, final BitSet used, final int gridCells, final int remainingArea) {

        if (remainingArea == 0) {
            return true;
        }

        int freeCells = gridCells - used.cardinality();
        if (remainingArea > freeCells) {
            return false;
        }

        int nextId = nextIdWithRemaining(remaining, idOrder);
        if (nextId == -1) {
            return false;
        }

        List<BitSet> placements = placementsById[nextId];
        if (placements.isEmpty()) {
            return false;
        }

        for (BitSet placement : placements) {
            BitSet tmp = (BitSet) placement.clone();
            tmp.and(used);
            if (!tmp.isEmpty()) {
                continue;
            }

            used.or(placement);
            remaining[nextId]--;
            int newRemainingArea = remainingArea - areaById[nextId];

            if (backtrack(remaining, areaById, idOrder, placementsById, used, gridCells, newRemainingArea)) {
                return true;
            }

            used.andNot(placement);
            remaining[nextId]++;
        }

        return false;
    }

    private static int nextIdWithRemaining(final int[] remaining, final int[] idOrder) {
        for (int id : idOrder) {
            if (remaining[id] > 0) {
                return id;
            }
        }
        return -1;
    }

    private static void precomputePlacementsForShape(final Set<Coord> shape, final int width, final int height,
            final List<BitSet> outPlacements) {
        int maxRow = 0;
        int maxCol = 0;
        for (Coord c : shape) {
            if (c.row() > maxRow) {
                maxRow = c.row();
            }
            if (c.col() > maxCol) {
                maxCol = c.col();
            }
        }
        int shapeHeight = maxRow + 1;
        int shapeWidth = maxCol + 1;

        int maxStartRow = height - shapeHeight;
        int maxStartCol = width - shapeWidth;

        for (int baseRow = 0; baseRow <= maxStartRow; baseRow++) {
            for (int baseCol = 0; baseCol <= maxStartCol; baseCol++) {
                BitSet bs = new BitSet(width * height);
                for (Coord c : shape) {
                    int r = baseRow + c.row();
                    int col = baseCol + c.col();
                    int idx = r * width + col;
                    bs.set(idx);
                }
                outPlacements.add(bs);
            }
        }
    }

    record Grid(int width, int height, int[] qty) {

        boolean fits(final List<Present> presents) {
            return size() >= presentSize(presents);
        }

        int size() {
            return width * height;
        }

        int presentSize(final List<Present> presents) {
            int size = 0;
            for (int i = 0; i < qty.length; i++) {
                size += qty[i] * presents.get(i).size();
            }
            return size;
        }

        static Grid from(final String input) {
            final String[] split = input.split(":");
            final String[] size = split[0].split("x");
            final int[] qty = Arrays.stream(split[1].strip().split(" ")).mapToInt(Integer::parseInt).toArray();
            return new Grid(Integer.parseInt(size[0]), Integer.parseInt(size[1]), qty);
        }

    }

    record Present(int id, Map<Dir, Set<Coord>> coords) {

        int size() {
            return coords.get(Dir.UP).size();
        }

        static Present from(final List<String> input) {
            final int id = Integer.parseInt(input.getFirst().replace(":", "").strip());
            final Map<Dir, Set<Coord>> coordsMap = new EnumMap<>(Dir.class);

            final Set<Coord> coords = new HashSet<>();
            for (int i = 1; i < input.size(); i++) {
                final String row = input.get(i);
                for (int j = 0; j < row.length(); j++) {
                    if (row.charAt(j) == '#') {
                        coords.add(Coord.of(i, j));
                    }
                }
            }
            coordsMap.put(Dir.UP, coords);
            coordsMap.put(Dir.RIGHT, rotate90(coords));
            coordsMap.put(Dir.DOWN, rotate180(coords));
            coordsMap.put(Dir.LEFT, rotate270(coords));
            return new Present(id, coordsMap);
        }

        private static int maxRow(Set<Coord> coords) {
            return coords.stream().mapToInt(Coord::row).max().orElse(0);
        }

        private static int maxCol(Set<Coord> coords) {
            return coords.stream().mapToInt(Coord::col).max().orElse(0);
        }

        private static Set<Coord> rotate90(Set<Coord> coords) {
            int maxRow = maxRow(coords);

            return coords.stream().map(p -> new Coord(p.col(), maxRow - p.row())).collect(Collectors.toSet());
        }

        public static Set<Coord> rotate180(Set<Coord> coords) {
            int maxRow = maxRow(coords);
            int maxCol = maxCol(coords);

            return coords.stream().map(p -> new Coord(maxRow - p.row(), maxCol - p.col())).collect(Collectors.toSet());
        }

        public static Set<Coord> rotate270(Set<Coord> coords) {
            int maxCol = maxCol(coords);

            return coords.stream().map(p -> new Coord(maxCol - p.col(), p.row())).collect(Collectors.toSet());
        }

    }
}
