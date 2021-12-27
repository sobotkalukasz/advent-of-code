package pl.lsobotka.adventofcode.beaconscanner;

import java.util.function.Function;

public record Point(int x, int y, int z) {
    public static Point parse(final String input) {
        final String[] split = input.split(",");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static Point startPos() {
        return new Point(0, 0, 0);
    }

    public Point add(final Point point) {
        return new Point(this.x + point.x(), this.y + point.y(), this.z() + point.z());
    }

    public Point getDiff(final Point other) {
        final int x = this.x - other.x;
        final int y = this.y - other.y;
        final int z = this.z - other.z;

        return new Point(x, y, z);
    }

    public int getDistance() {
        return Math.abs(this.x) + Math.abs(this.y) + Math.abs(this.z);
    }

    public static Function<Point, Point> getRotateFunction(final Axis axis, final Degree degree) {
        final Function<Point, Point> rotateCoordFunction;
        if (axis.equals(Axis.X)) {
            if (degree.equals(Degree.ZERO)) {
                rotateCoordFunction = (c -> new Point(c.x, c.y, c.z));
            } else if (degree.equals(Degree.NINETY)) {
                rotateCoordFunction = (c -> new Point(c.x, -c.z, c.y));
            } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                rotateCoordFunction = (c -> new Point(c.x, -c.y, -c.z));
            } else { //Degree.TWO_HUNDRED_SEVENTY
                rotateCoordFunction = (c -> new Point(c.x, c.z, -c.y));
            }
        } else if (axis.equals(Axis.Y)) {
            if (degree.equals(Degree.ZERO)) {
                rotateCoordFunction = (c -> new Point(c.y, c.x, -c.z));
            } else if (degree.equals(Degree.NINETY)) {
                rotateCoordFunction = (c -> new Point(c.y, c.z, c.x));
            } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                rotateCoordFunction = (c -> new Point(c.y, -c.x, c.z));
            } else { //Degree.TWO_HUNDRED_SEVENTY
                rotateCoordFunction = (c -> new Point(c.y, -c.z, -c.x));
            }
        } else {
            if (degree.equals(Degree.ZERO)) {
                rotateCoordFunction = (c -> new Point(c.z, c.y, -c.x));
            } else if (degree.equals(Degree.NINETY)) {
                rotateCoordFunction = (c -> new Point(c.z, c.x, c.y));
            } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                rotateCoordFunction = (c -> new Point(c.z, -c.y, c.x));
            } else { //Degree.TWO_HUNDRED_SEVENTY
                rotateCoordFunction = (c -> new Point(c.z, -c.x, -c.y));
            }
        }
        return rotateCoordFunction;
    }

    public static Function<Point, Point> getReverseFunction(final Reverse reverse) {
        if (reverse.equals(Reverse.YES)) {
            return c -> new Point(-c.x, -c.y, c.z);
        }
        return c -> c;
    }

    enum Axis {
        X, Y, Z
    }

    enum Degree {
        ZERO, NINETY, ONE_HUNDRED_EIGHTY, TWO_HUNDRED_SEVENTY
    }

    enum Reverse {
        YES, NO
    }
}
