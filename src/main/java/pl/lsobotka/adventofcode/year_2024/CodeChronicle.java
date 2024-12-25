package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/25
 * */
public class CodeChronicle {

    final List<List<Integer>> keys = new ArrayList<>();
    final List<List<Integer>> locks = new ArrayList<>();
    int height;

    CodeChronicle(final List<String> input) {

        int offset = 0;
        this.height = 0;
        int width = 0;
        boolean shouldCheck = true;
        boolean key = false;

        final Set<Coord> temp = new HashSet<>();

        for (int row = 0; row < input.size(); row++) {
            final String rowString = input.get(row);
            if (width == 0) {
                width = rowString.length();
            }
            if (rowString.isEmpty()) {
                shouldCheck = true;
                offset = row + 1;
                if (height == 0) {
                    height = row;
                }
                if (key) {
                    keys.add(determineKey(temp, width, height));
                } else {
                    locks.add(determineLock(temp, width));
                }
                temp.clear();
                continue;
            }
            if (shouldCheck) {
                key = rowString.contains(".");
                shouldCheck = false;
            }

            for (int col = 0; col < rowString.length(); col++) {
                if (rowString.charAt(col) == '#') {
                    temp.add(new Coord(row - offset, col));
                }
            }
        }
        if (!temp.isEmpty()) {
            if (key) {
                keys.add(determineKey(temp, width, height));
            } else {
                locks.add(determineLock(temp, width));
            }
        }
    }

    private List<Integer> determineLock(final Set<Coord> coords, int width) {
        final List<Integer> pins = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            final int index = i;
            final int max = coords.stream().filter(c -> c.col() == index).mapToInt(Coord::row).max().orElse(0);
            pins.add(max);
        }
        return pins;
    }

    private List<Integer> determineKey(final Set<Coord> coords, int width, int height) {
        final List<Integer> pins = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            final int index = i;
            final int min = coords.stream().filter(c -> c.col() == index).mapToInt(Coord::row).min().orElse(0);
            pins.add(height - min - 1);
        }
        return pins;
    }

    int countUniquePairs() {
        int count = 0;
        for (List<Integer> lock : locks) {
            for (List<Integer> key : keys) {
                if (canOpen(lock, key, height)) {
                    count++;
                }

            }
        }
        return count;
    }

    private boolean canOpen(final List<Integer> lock, final List<Integer> key, int height) {
        for (int i = 0; i < lock.size(); i++) {
            if (lock.get(i) + key.get(i) > height - 2) {
                return false;
            }
        }
        return true;
    }
}
