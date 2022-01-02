package pl.lsobotka.adventofcode.beaconscanner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

class Scanner {
    private final String name;
    private Point point;
    private final List<Beacon> beacons;
    private final List<List<Beacon>> rotations;

    public Scanner(final String name, final List<Beacon> beacons) {
        this.name = name;
        this.point = Point.startPos();
        this.beacons = new ArrayList<>();
        this.rotations = new ArrayList<>();
        beacons.forEach(this::addBeacon);
    }

    public void addBeacon(final Beacon beacon) {
        this.addBeacon(beacons, beacon);
    }

    public void addBeacon(final List<Beacon> beacons, final Beacon beacon) {
        beacons.forEach(b -> b.determineRelativeTo(beacon));
        beacons.forEach(beacon::determineRelativeTo);
        beacons.add(beacon);
    }

    public void adjust(final Point adjust) {
        this.point = this.point.add(adjust);
    }

    public void rotate() {
        for (Point.Reverse reverse : Point.Reverse.values()) {
            for (Point.Axis axis : Point.Axis.values()) {
                for (Point.Degree degree : Point.Degree.values()) {
                    rotations.add(rotate(axis, degree, reverse));
                }
            }
        }
    }

    public List<Beacon> rotate(final Point.Axis axis, final Point.Degree degree, final Point.Reverse reverse) {
        final List<Beacon> rotateBeacons = new ArrayList<>();

        final Function<Point, Point> rotateCoordFunction = Point.getRotateFunction(axis, degree);
        final Function<Point, Point> reverseCoordFunction = Point.getReverseFunction(reverse);

        rotateBeacons(rotateCoordFunction, reverseCoordFunction).forEach(b -> addBeacon(rotateBeacons, b));
        return rotateBeacons;
    }

    private List<Beacon> rotateBeacons(final Function<Point, Point> rotate, final Function<Point, Point> reverse) {
        return this.beacons.stream()
                .map(Beacon::getPoint)
                .map(rotate)
                .map(reverse)
                .map(Beacon::new)
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public Point getPoint() {
        return point;
    }

    public List<Beacon> getBeacons() {
        return beacons;
    }

    public List<List<Beacon>> getRotations() {
        return rotations;
    }
}
