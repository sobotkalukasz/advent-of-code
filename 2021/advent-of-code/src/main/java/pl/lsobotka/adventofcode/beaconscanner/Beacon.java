package pl.lsobotka.adventofcode.beaconscanner;

import java.util.HashMap;
import java.util.Map;

class Beacon {
    private Point point;

    private final Map<Beacon, Point> relativeToOtherBeacons;

    public Beacon(final Point point) {
        this.point = point;
        this.relativeToOtherBeacons = new HashMap<>();
    }

    public void adjustCoord(final Point adjust) {
        this.point = this.point.add(adjust);
    }

    public void determineRelativeTo(final Beacon beacon) {
        relativeToOtherBeacons.put(beacon, this.point.getDiff(beacon.point));
    }

    public Map<Beacon, Point> getRelativeToOtherBeacons() {
        return relativeToOtherBeacons;
    }

    public Point getPoint() {
        return point;
    }
}
