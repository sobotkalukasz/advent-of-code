package pl.lsobotka.adventofcode.year_2022;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/*
 * https://adventofcode.com/2022/day/8
 * */
public class TreetopTreeHouse {

    private final Map<Coord, Tree> forest;

    TreetopTreeHouse(final List<String> input) {
        forest = initForest(input);
    }

    private Map<Coord, Tree> initForest(List<String> input) {
        final Map<Coord, Tree> forest;
        forest = new HashMap<>();

        for (int row = 0; row < input.size(); row++) {
            final String inputRow = input.get(row);
            for (int col = 0; col < input.size(); col++) {
                final int size = Integer.parseInt(String.valueOf(inputRow.charAt(col)));
                final Coord coord = Coord.of(row, col);
                forest.put(coord, new Tree(coord, size));
            }
        }
        return forest;
    }

    long countVisibleTrees() {
        return forest.values().stream().filter(this::isVisible).count();
    }

    private boolean isVisible(final Tree tree) {
        final var iter = Coord.providers.iterator();

        boolean visible = false;
        while (!visible && iter.hasNext()) {
            visible = checkVisibility(tree, iter.next());
        }
        return visible;
    }

    private boolean checkVisibility(final Tree tree, Function<Coord, Coord> provider) {
        boolean visible = true;
        Coord next = provider.apply(tree.coord);
        while (visible && forest.containsKey(next)) {
            visible = forest.get(next).size() < tree.size();
            next = provider.apply(next);
        }
        return visible;
    }

    int highestScenicScore() {
        return forest.values().stream().mapToInt(this::scenicScoreBy).max().orElse(0);
    }

    private int scenicScoreBy(final Tree tree) {
        return Coord.providers.stream()
                .mapToInt(p -> getVisibleTreeByProvider(tree, p))
                .reduce((a, b) -> a * b)
                .orElse(0);
    }

    private int getVisibleTreeByProvider(final Tree tree, Function<Coord, Coord> provider) {
        int visibleTrees = 0;
        boolean visible = true;
        Coord next = provider.apply(tree.coord);

        while (visible && forest.containsKey(next)) {
            visibleTrees++;
            visible = forest.get(next).size() < tree.size();
            next = provider.apply(next);
        }

        return visibleTrees;
    }

    record Tree(Coord coord, int size) {
    }

    record Coord(int row, int column) {
        static final List<Function<Coord, Coord>> providers = List.of(Coord::prevRow, Coord::nextRow, Coord::prevCol,
                Coord::nextCol);

        static Coord of(int row, int column) {
            return new Coord(row, column);
        }

        private Coord nextRow() {
            return Coord.of(row + 1, column);
        }

        private Coord prevRow() {
            return Coord.of(row - 1, column);
        }

        private Coord nextCol() {
            return Coord.of(row, column + 1);
        }

        private Coord prevCol() {
            return Coord.of(row, column - 1);
        }
    }
}
