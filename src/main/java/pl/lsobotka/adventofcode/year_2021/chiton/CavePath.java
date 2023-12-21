package pl.lsobotka.adventofcode.year_2021.chiton;

import java.util.Comparator;

import pl.lsobotka.adventofcode.year_2021.Coordinate;

record CavePath(Coordinate actual, int currentValue) implements Comparable<CavePath> {

    @Override
    public int compareTo(CavePath o) {
        return Comparator.comparingInt(CavePath::currentValue).compare(this, o);
    }
}
