package pl.lsobotka.adventofcode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/15
 * */
public class BeaconExclusionZone {

    final List<Sensor> sensors;

    BeaconExclusionZone(final List<String> input) {
        this.sensors = input.stream().map(Sensor::from).collect(Collectors.toList());
    }

    long countEmptyFor(final long rowToCheck) {
        final Map<Coord, Type> caveMap = new HashMap<>();

        sensors.forEach(s -> {
            caveMap.put(s.sensor, Type.SENSOR);
            caveMap.put(s.beacon, Type.BEACON);
        });

        sensors.stream()
                .filter(s -> s.isInRange(rowToCheck))
                .map(s -> s.sensor.getInRangeForRow(s.distance, rowToCheck))
                .flatMap(Collection::stream)
                .forEach(c -> caveMap.putIfAbsent(c, Type.EMPTY));

        return caveMap.entrySet()
                .stream()
                .filter(e -> e.getKey().row == rowToCheck && e.getValue().equals(Type.EMPTY))
                .count();
    }

    long findTuningFrequency(final long max) {

        long frequency = 0;
        for (long r = 0; r <= max; r++) {
            for (long c = 0; c <= max; c++) {
                final Coord current = Coord.of(r, c);
                final Sensor sensor = getSensorWithBiggestDistance(current);

                if (sensor == null) {
                    frequency = c * 4_000_000 + r;
                    break;
                }

                c = sensor.sensor.getMaxInRangeForRow(sensor.distance, r).col;
            }
        }

        return frequency;
    }

    private Sensor getSensorWithBiggestDistance(final Coord coord) {
        Sensor sensor = null;
        for (Sensor s : sensors) {
            if (s.sensor.distanceTo(coord) <= s.distance) {
                if (sensor == null || sensor.distance < s.distance) {
                    sensor = s;
                }
            }
        }
        return sensor;
    }

    private record Sensor(Coord sensor, Coord beacon, long distance) {

        static Sensor from(final String data) {
            String edit = data.replaceAll("Sensor at", "");
            edit = edit.replaceAll("closest beacon is at", "");
            edit = edit.replaceAll(" ", "");

            final String[] split = edit.split(":");
            final Coord sensor = Coord.of(split[0]);
            final Coord beacon = Coord.of(split[1]);

            return new Sensor(sensor, beacon, sensor.distanceTo(beacon));
        }

        private boolean isInRange(long row) {
            final long minus = sensor.row - distance;
            final long plus = sensor.row + distance;

            return Math.min(minus, plus) <= row && row <= Math.max(minus, plus);
        }
    }

    private record Coord(long row, long col) {

        static Coord of(final long row, final long col) {
            return new Coord(row, col);
        }

        static Coord of(final String raw) {
            final String[] split = raw.split(",");
            final int col = Integer.parseInt(split[0].replaceAll("x=", ""));
            final int row = Integer.parseInt(split[1].replaceAll("y=", ""));

            return Coord.of(row, col);
        }

        Set<Coord> getInRangeForRow(final long distance, final long forRow) {
            final Set<Coord> inRange = new HashSet<>();
            final long diff = distance - Math.abs(forRow - this.row);
            for (long c = 0; c <= diff; c++) {
                inRange.add(Coord.of(forRow, this.col - c));
                inRange.add(Coord.of(forRow, this.col + c));
            }

            return inRange;
        }

        Coord getMaxInRangeForRow(final long distance, final long forRow) {
            final long diff = distance - Math.abs(forRow - this.row);
            return Coord.of(forRow, this.col + diff);
        }

        long distanceTo(final Coord other) {
            return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
        }
    }

    enum Type {
        BEACON, SENSOR, EMPTY
    }
}
