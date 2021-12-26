package pl.lsobotka.adventofcode.beaconscanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BeaconScanner {

    private final Map<String, Point> alignScanners;
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

    public int getBiggestDistance() {
        final Map<String, Map<String, Integer>> distances = new HashMap<>();
        for (Map.Entry<String, Point> entry : alignScanners.entrySet()) {
            final Map<String, Integer> between = new HashMap<>();
            final String name = entry.getKey();
            distances.put(name, between);
            for (Map.Entry<String, Point> other : alignScanners.entrySet()) {
                if (other.getKey().equals(name)) {
                    continue;
                }
                final int distance = entry.getValue().getDiff(other.getValue()).getDistance();
                between.put(other.getKey(), distance);
            }
        }
        return distances.values()
                .stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .max(Integer::compareTo)
                .orElse(0);
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
                beacons.add(new Beacon(Point.parse(row)));
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
                uniqueBeacons.addAll(scanner.getBeacons());
                alignScanners.put(scanner.getName(), scanner.getPoint());
                left.remove(scanner);
            } else {
                Scanner toRemove = null;
                for (Scanner scanner : left) {
                    for (List<Beacon> rotation : scanner.getRotations()) {
                        final Set<Point> test = getUniqueCoords(rotation);
                        final Set<Point> current = getUniqueCoords(uniqueBeacons);
                        test.retainAll(current);
                        if (test.size() >= 66) { // at least 12 same scanners
                            final List<Beacon> testUniqueBeacons = rotation.stream()
                                    .filter(b -> b.getRelativeToOtherBeacons()
                                            .values()
                                            .stream()
                                            .noneMatch(test::contains))
                                    .collect(Collectors.toList());

                            final Point diff = calculateDiff(test, rotation);
                            testUniqueBeacons.forEach(b -> b.adjustCoord(diff));
                            uniqueBeacons.addAll(testUniqueBeacons);
                            scanner.adjust(diff);
                            alignScanners.put(scanner.getName(), scanner.getPoint());

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

    public Set<Point> getUniqueCoords(final List<Beacon> beacons) {
        return beacons.stream()
                .map(b -> b.getRelativeToOtherBeacons().values())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Point calculateDiff(final Set<Point> coordsUnderTest, final List<Beacon> beaconsUnderTest) {
        final Point refPoint = coordsUnderTest.stream().findFirst().orElseGet(Point::startPos);
        final Beacon testBeacon = beaconsUnderTest.stream()
                .filter(b -> b.getRelativeToOtherBeacons().values().stream().anyMatch(c -> c.equals(refPoint)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        final Beacon uniqueBeacon = uniqueBeacons.stream()
                .filter(b -> b.getRelativeToOtherBeacons().values().stream().anyMatch(c -> c.equals(refPoint)))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return uniqueBeacon.getPoint().getDiff(testBeacon.getPoint());
    }

}
