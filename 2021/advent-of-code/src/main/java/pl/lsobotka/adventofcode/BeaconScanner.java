package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BeaconScanner {

    private final Map<String, Coord> alignScanners;
    private final List<Beacon> uniqueBeacons;

    public BeaconScanner(final List<String> input) {
        alignScanners = new HashMap<>();
        uniqueBeacons = new ArrayList<>();
        final List<Scanner> scanners = processRawInput(input);
        alignScanners(scanners);
    }

    public int getUniqueBeaconsSize() {
        return uniqueBeacons.size();
    }

    private List<Scanner> processRawInput(final List<String> input) {
        final List<Scanner> scanners = new ArrayList<>();
        String name = null;
        final List<Beacon> beacons = new ArrayList<>();

        for (int index = 0; index < input.size(); index++) {
            final String row = input.get(index);

            if (row.contains("scanner")) {
                name = row.replace("-", "").strip();
            } else if (!row.isEmpty()) {
                beacons.add(new Beacon(Coord.parse(row)));
            }
            if (row.isEmpty() || index + 1 == input.size()) {
                scanners.add(new Scanner(name, beacons));
                beacons.clear();
            }
        }
        scanners.forEach(Scanner::rotate);
        return scanners;
    }

    private void alignScanners(final List<Scanner> scanners) {
        final List<Scanner> left = new ArrayList<>(scanners);

        while (!left.isEmpty()) {
            if (uniqueBeacons.isEmpty()) {
                final Scanner scanner = left.get(0);
                uniqueBeacons.addAll(scanner.beacons);
                alignScanners.put(scanner.name, scanner.coord);
                left.remove(scanner);
            } else {
                Scanner toRemove = null;
                for (Scanner scanner : left) {
                    for (Scanner rotation : scanner.rotations) {
                        final Set<Coord> test = rotation.getUniqueBeaconCoords();
                        final Set<Coord> current = getUniqueCoords(uniqueBeacons);
                        test.retainAll(current);
                        if (test.size() >= 66) { // at least 12 same scanners
                            final List<Beacon> testUniqueBeacons = rotation.beacons.stream()
                                    .filter(b -> b.relativeToOtherBeacons.values().stream().noneMatch(test::contains))
                                    .collect(Collectors.toList());

                            final Coord diff = calculateDiff(test, rotation.beacons);
                            testUniqueBeacons.forEach(b -> b.adjustCoord(diff));
                            uniqueBeacons.addAll(testUniqueBeacons);
                            rotation.adjust(diff);
                            alignScanners.put(rotation.name, rotation.coord);

                            toRemove = scanner;
                            break;
                        }
                    }
                    if (Objects.nonNull(toRemove)) {
                        break;
                    }
                }
                left.remove(toRemove);
            }
        }
    }

    public Set<Coord> getUniqueCoords(final List<Beacon> beacons) {
        return beacons.stream()
                .map(b -> b.relativeToOtherBeacons.values())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Coord calculateDiff(final Set<Coord> coordsUnderTest, final List<Beacon> beaconsUnderTest) {
        final Coord refCoord = coordsUnderTest.stream().findFirst().orElseGet(() -> Coord.startPos());
        final Beacon testBeacon = beaconsUnderTest.stream()
                .filter(b -> b.relativeToOtherBeacons.values().stream().anyMatch(c -> c.equals(refCoord)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        final Beacon uniqueBeacon = uniqueBeacons.stream()
                .filter(b -> b.relativeToOtherBeacons.values().stream().anyMatch(c -> c.equals(refCoord)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return uniqueBeacon.getCoord().getDiff(testBeacon.coord);
    }

    private static class Scanner {
        private final String name;
        private Coord coord;
        private final Axis axis;
        private final Degree degree;
        private final ReverseX reverseX;
        private final List<Beacon> beacons;
        private List<Scanner> rotations;

        public Scanner(final String name, final List<Beacon> beacons) {
            this.name = name;
            this.coord = Coord.startPos();
            this.axis = Axis.X;
            this.degree = Degree.ZERO;
            this.reverseX = ReverseX.NO;
            this.beacons = new ArrayList<>();
            this.rotations = new ArrayList<>();
            beacons.forEach(this::addBeacon);
        }

        public Scanner(final String name, final Axis axis, final Degree degree, final ReverseX reverseX) {
            this.name = name;
            this.coord = Coord.startPos();
            this.axis = axis;
            this.degree = degree;
            this.reverseX = reverseX;
            this.beacons = new ArrayList<>();
        }

        public void addBeacon(final Beacon beacon) {
            beacons.forEach(b -> b.determineRelativeTo(beacon));
            beacons.forEach(beacon::determineRelativeTo);
            beacons.add(beacon);
        }

        public void adjust(final Coord adjust) {
            this.coord = this.coord.add(adjust);
        }

        public void rotate() {
            final List<Scanner> rotations = new ArrayList<>();
            for (ReverseX reverse : ReverseX.values()) {
                for (Axis axis : Axis.values()) {
                    for (Degree degree : Degree.values()) {
                        rotations.add(rotate(axis, degree, reverse));
                    }
                }
            }
            this.rotations = rotations;
        }

        public Scanner rotate(final Axis axis, final Degree degree, final ReverseX reverse) {
            final Scanner scanner = new Scanner(this.name, axis, degree, reverse);
            final Function<Coord, Coord> rotateCoordFunction = Coord.getRotateFunction(axis, degree);
            final Function<Coord, Coord> reverseFunction = Coord.getReverseFunction(reverse);

            final List<Beacon> tempBeacons = rotateBeacons(rotateCoordFunction, reverseFunction);
            tempBeacons.forEach(scanner::addBeacon);
            return scanner;
        }

        private List<Beacon> rotateBeacons(final Function<Coord, Coord> rotate, final Function<Coord, Coord> reverse) {
            return this.beacons.stream()
                    .map(Beacon::getCoord)
                    .map(rotate)
                    .map(reverse)
                    .map(Beacon::new)
                    .collect(Collectors.toList());
        }

        public Set<Coord> getUniqueBeaconCoords() {
            return beacons.stream()
                    .map(b -> b.relativeToOtherBeacons.values())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }

    }

    private static class Beacon {
        private Coord coord;

        private final Map<Beacon, Coord> relativeToOtherBeacons;

        public Beacon(final Coord coord) {
            this.coord = coord;
            this.relativeToOtherBeacons = new HashMap<>();
        }

        public void adjustCoord(final Coord adjust) {
            this.coord = this.coord.add(adjust);
        }

        public void determineRelativeTo(final Beacon beacon) {
            relativeToOtherBeacons.put(beacon, this.coord.getAbsoluteDiff(beacon.coord));
        }

        public Coord getCoord() {
            return coord;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Beacon.class.getSimpleName() + "[", "]").add("coord=" + coord).toString();
        }
    }

    protected record Coord(int x, int y, int z) {
        public static Coord parse(final String input) {
            final String[] split = input.split(",");
            return new Coord(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }

        public static Coord startPos() {
            return new Coord(0, 0, 0);
        }

        public Coord add(final Coord coord) {
            return new Coord(this.x + coord.x(), this.y + coord.y(), this.z() + coord.z());
        }

        public Coord getAbsoluteDiff(final Coord other) {
            final int x = Math.abs(this.x - other.x);
            final int y = Math.abs(this.y - other.y);
            final int z = Math.abs(this.z - other.z);

            return new Coord(x, y, z);
        }

        public Coord getDiff(final Coord other) {
            final int x = this.x - other.x;
            final int y = this.y - other.y;
            final int z = this.z - other.z;

            return new Coord(x, y, z);
        }

        public static Function<Coord, Coord> getReverseFunction(final ReverseX reverse) {
            final Function<Coord, Coord> reverseFunction;
            if (reverse.equals(ReverseX.NO)) {
                reverseFunction = c -> c;
            } else {
                reverseFunction = c -> new Coord(-c.x, c.y, c.z);
            }
            return reverseFunction;
        }

        public static Function<Coord, Coord> getRotateFunction(final Axis axis, final Degree degree) {
            final Function<Coord, Coord> rotateCoordFunction;
            if (axis.equals(Axis.X)) {
                if (degree.equals(Degree.ZERO)) {
                    rotateCoordFunction = (c -> new Coord(c.x, c.y, c.z));
                } else if (degree.equals(Degree.NINETY)) {
                    rotateCoordFunction = (c -> new Coord(c.x, -c.z, c.y));
                } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                    rotateCoordFunction = (c -> new Coord(c.x, -c.y, -c.z));
                } else { //Degree.TWO_HUNDRED_SEVENTY
                    rotateCoordFunction = (c -> new Coord(c.x, c.z, -c.y));
                }
            } else if (axis.equals(Axis.Y)) {
                if (degree.equals(Degree.ZERO)) {
                    rotateCoordFunction = (c -> new Coord(c.y, c.x, c.z));
                } else if (degree.equals(Degree.NINETY)) {
                    rotateCoordFunction = (c -> new Coord(c.y, -c.z, c.x));
                } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                    rotateCoordFunction = (c -> new Coord(c.y, -c.x, -c.z));
                } else { //Degree.TWO_HUNDRED_SEVENTY
                    rotateCoordFunction = (c -> new Coord(c.y, c.z, -c.x));
                }
            } else { // Axis Z
                if (degree.equals(Degree.ZERO)) {
                    rotateCoordFunction = (c -> new Coord(c.z, c.y, c.x));
                } else if (degree.equals(Degree.NINETY)) {
                    rotateCoordFunction = (c -> new Coord(c.z, -c.x, c.y));
                } else if (degree.equals(Degree.ONE_HUNDRED_EIGHTY)) {
                    rotateCoordFunction = (c -> new Coord(c.z, -c.y, -c.x));
                } else { //Degree.TWO_HUNDRED_SEVENTY
                    rotateCoordFunction = (c -> new Coord(c.z, c.x, -c.y));
                }
            }
            return rotateCoordFunction;
        }

    }

    private enum Axis {
        X, Y, Z
    }

    private enum Degree {
        ZERO, NINETY, ONE_HUNDRED_EIGHTY, TWO_HUNDRED_SEVENTY
    }

    private enum ReverseX {
        YES, NO
    }

}
