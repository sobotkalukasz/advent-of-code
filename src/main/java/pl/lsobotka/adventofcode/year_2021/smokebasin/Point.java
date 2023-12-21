package pl.lsobotka.adventofcode.year_2021.smokebasin;

import pl.lsobotka.adventofcode.year_2021.Coordinate;

class Point {

    private final Coordinate coordinate;
    private final int value;
    private boolean isLowest;
    private boolean isBasin;

    public Point(final Coordinate coordinate, final int value) {
        this.coordinate = coordinate;
        this.value = value;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getValue() {
        return value;
    }

    public boolean isLowest() {
        return isLowest;
    }

    public void setLowest(boolean lowest) {
        isLowest = lowest;
    }

    public boolean isBasin() {
        return isBasin;
    }

    public void setBasin(boolean basin) {
        isBasin = basin;
    }
}
