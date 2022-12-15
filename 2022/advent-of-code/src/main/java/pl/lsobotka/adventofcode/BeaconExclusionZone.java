package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/14
 * */
public class BeaconExclusionZone {

    final List<Sensor> sensors;

    BeaconExclusionZone(final List<String> input) {
        this.sensors = input.stream().map(Sensor::from).collect(Collectors.toList());
    }

    long countEmptyFor(final int rowToCheck) {
        final Map<Coord, Type> caveMap = new HashMap<>();

        sensors.forEach(s -> {
            caveMap.put(s.sensor, Type.SENSOR);
            caveMap.put(s.beacon, Type.BEACON);
        });

        sensors.forEach(s -> {
            final int minus = s.sensor.row - s.distance;
            final int plus = s.sensor.row + s.distance;

            final boolean inRange = Math.min(minus, plus) <= rowToCheck && rowToCheck <= Math.max(minus, plus);
            if (inRange) {
                final Set<Coord> allInRange = s.sensor.getAllInRange(s.distance, rowToCheck);
                allInRange.forEach(c -> caveMap.putIfAbsent(c, Type.EMPTY));
            }

        });

        return caveMap.entrySet()
                .stream()
                .filter(e -> e.getKey().row == rowToCheck)
                .map(Map.Entry::getValue)
                .filter(Type.EMPTY::equals)
                .count();
    }

    private record Sensor(Coord sensor, Coord beacon, int distance) {

        static Sensor from(final String data) {
            String edit = data.replaceAll("Sensor at", "");
            edit = edit.replaceAll("closest beacon is at", "");
            edit = edit.replaceAll(" ", "");

            final String[] split = edit.split(":");
            final Coord sensor = Coord.of(split[0]);
            final Coord beacon = Coord.of(split[1]);

            return new Sensor(sensor, beacon, sensor.distanceTo(beacon));
        }
    }

    private record Coord(int row, int col) {

        static Coord of(final int row, final int col) {
            return new Coord(row, col);
        }

        static Coord of(final String raw) {
            final String[] split = raw.split(",");
            final int col = Integer.parseInt(split[0].replaceAll("x=", ""));
            final int row = Integer.parseInt(split[1].replaceAll("y=", ""));

            return Coord.of(row, col);
        }

        Set<Coord> getAllInRange(final int distance, final int forRow) {
            final Set<Coord> inRange = new HashSet<>();
            final int diff = distance - Math.abs(forRow - this.row);
            for (int c = 0; c <= diff; c++) {
                inRange.add(Coord.of(forRow, this.col - c));
                inRange.add(Coord.of(forRow, this.col + c));
            }

            return inRange;
        }

        private int distanceTo(final Coord other) {
            return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
        }
    }

    enum Type {
        BEACON, SENSOR, EMPTY
    }
}
