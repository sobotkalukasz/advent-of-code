package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.List;

/*
 * https://adventofcode.com/2023/day/24
 * */
public class NeverOdds {

    final List<Hailstone> hailstones;

    public NeverOdds(final List<String> input) {
        this.hailstones = Hailstone.from(input);
    }

    int intersectXYatRange(final long from, final long to) {
        int intersectCount = 0;

        for (int first = 0; first < hailstones.size() - 1; first++) {
            for (int second = first + 1; second < hailstones.size(); second++) {
                if (hailstones.get(first).canIntersectXYinRange(hailstones.get(second), from, to)) {
                    intersectCount++;
                }
            }
        }

        return intersectCount;
    }

    long intersectXYZ() {
        final Hailstone potential = determineHailston();
        return potential == null ? 0 : potential.x() + potential.y() + potential.z();
    }

    private Hailstone determineHailston() {
        final Hailstone hf = hailstones.get(0);
        final Hailstone hl = hailstones.get(hailstones.size() - 1);

        final int range = hailstones.stream()
                .map(h -> Math.max(Math.max(Math.abs(h.vx), Math.abs(h.vy)), Math.abs(h.vz())))
                .reduce(0, Integer::max);

        for (int vx = -range; vx <= range; vx++) {
            for (int vy = -range; vy <= range; vy++) {
                for (int vz = -range; vz <= range; vz++) {

                    final int avx = hf.vx - vx;
                    final int avy = hf.vy - vy;
                    final int bvx = hl.vx - vx;
                    final int bvy = hl.vy - vy;

                    final int divisor = (avx * bvy) - (avy * bvx);

                    if (divisor != 0) {
                        long time = (bvy * (hl.x - hf.x) - bvx * (hl.y - hf.y)) / divisor;

                        long x = hf.x + hf.vx * time - vx * time;
                        long y = hf.y + hf.vy * time - vy * time;
                        long z = hf.z + hf.vz * time - vz * time;

                        final Hailstone potential = new Hailstone(x, y, z, vx, vy, vz);

                        boolean found = true;
                        for (int index = 1; index < hailstones.size() - 1; index++) {
                            if (!potential.canIntersect(hailstones.get(index))) {
                                found = false;
                                break;
                            }
                        }

                        if (found) {
                            return potential;
                        }
                    }
                }
            }
        }
        return null;
    }

    record Hailstone(long x, long y, long z, int vx, int vy, int vz) {

        static List<Hailstone> from(final List<String> input) {
            final List<Hailstone> hails = new ArrayList<>();
            for (String row : input) {
                final String[] parts = row.replace(" ", "").replace("@", ",").split(",");
                hails.add(new Hailstone(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]),
                        Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5])));
            }
            return hails;
        }

        //Line–line intersection
        boolean canIntersectXYinRange(final Hailstone other, final long from, final long to) {
            final double time = willIntersectAtTime(other);

            double x = time * this.vx + this.x;
            double y = time * this.vy + this.y;

            return time > 0 && x >= from && x <= to && y >= from && y <= to;
        }

        private double willIntersectAtTime(final Hailstone other) {
            final int denominator = this.vx * other.vy - this.vy * other.vx;

            double px = (double) (other.x - this.x) * other.vy - (other.y - this.y) * other.vx;
            double py = (double) (this.x - other.x) * this.vy - (this.y - other.y) * this.vx;

            final double time = px / denominator;

            return (time > 0 && (py / denominator) < 0) ? time : 0;
        }

        boolean canIntersect(final Hailstone other) {
            if (other.vx != vx) {
                long nextTime = (x - other.x) / (other.vx - vx);
                return (x + nextTime * vx == other.x + nextTime * other.vx) // X match
                        && (y + nextTime * vy == other.y + nextTime * other.vy) // Y match
                        && (z + nextTime * vz == other.z + nextTime * other.vz); // Z match
            }
            return false;
        }
    }
}
