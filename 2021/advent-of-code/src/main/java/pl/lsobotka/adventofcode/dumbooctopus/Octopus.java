package pl.lsobotka.adventofcode.dumbooctopus;

import java.util.ArrayList;
import java.util.List;

import pl.lsobotka.adventofcode.Coordinate;

class Octopus {

    private final Coordinate coordinate;
    private int value;
    private boolean flash;

    public Octopus(Coordinate coordinate, int value) {
        this.coordinate = coordinate;
        this.value = value;
    }

    public void increaseValue() {
        if (!flash) {
            value++;
        }
    }

    public List<Coordinate> flashAndGetAffected(final int maxRow, final int maxColumn) {
        final List<Coordinate> adjacent = new ArrayList<>();
        if (!flash && value > 9) {
            value = 0;
            flash = true;
            adjacent.addAll(coordinate.getAdjacentWithDiagonal(maxRow, maxColumn));
        }
        return adjacent;
    }

    public boolean isFlash(){
        return flash;
    }

    public void resetFlash() {
        flash = false;
    }

}
