package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.List;

public class NeverOdds {

    final List<Hailstone> hailstones;

    public NeverOdds(final List<String> input) {
        this.hailstones = Hailstone.from(input);
    }

    int intersectXY(final long from, final long to) {
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

    record Hailstone(long x, long y, long z, int velX, int velY, int velZ) {

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
            boolean intersect = false;
            final int denominator = this.velX * other.velY - this.velY * other.velX;

            double px = (double) (other.x - this.x) * other.velY - (other.y - this.y) * other.velX;
            double py = (double) (this.x - other.x) * this.velY - (this.y - other.y) * this.velX;

            final double time = px / denominator;
            double intersectionX = time * this.velX + this.x;
            double intersectionY = time * this.velY + this.y;

            if (intersectionX >= from && intersectionX <= to && intersectionY >= from && intersectionY <= to && (
                    time > 0 && (py / denominator) < 0)) {
                intersect = true;

            }

            return intersect;
        }
    }
}
