package pl.lsobotka.adventofcode.chiton;

import java.util.Comparator;

import pl.lsobotka.adventofcode.Coordinate;

record CavePath(Coordinate actual, int currentValue) implements Comparable<CavePath> {

    @Override
    public int compareTo(CavePath o) {
        return Comparator.comparingInt(CavePath::currentValue).compare(this, o);
    }
}
