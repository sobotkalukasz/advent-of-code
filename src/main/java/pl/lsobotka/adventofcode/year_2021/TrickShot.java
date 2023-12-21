package pl.lsobotka.adventofcode.year_2021;

import java.util.ArrayList;
import java.util.List;

/*
 * https://adventofcode.com/2021/day/17
 * */
public class TrickShot {

    private final Target target;
    private final List<Prob> hits;

    public TrickShot(final String target) {
        this.target = new Target(target);
        this.hits = findAllHits();
    }

    public int getTheHighestPosition() {
        return hits.stream()
                .map(prob -> prob.trajectory.stream().map(Coordinate::row).max(Integer::compareTo).orElse(0))
                .max(Integer::compareTo)
                .orElse(0);
    }

    public int getNumberOfHits() {
        return hits.size();
    }

    private List<Prob> findAllHits() {
        final List<Prob> hits = new ArrayList<>();

        for (int x = 0; x <= target.lastColumn; x++) {
            for (int y = 0; y <= Math.abs(target.bottomRow) + 1; y++) {
                final Prob prob = testVelocity(x, y);
                if (prob.isHit()) {
                    hits.add(prob);
                }
            }
            for (int y = -1; y >= target.bottomRow - 1; y--) {
                final Prob prob = testVelocity(x, y);
                if (prob.isHit()) {
                    hits.add(prob);
                }
            }
        }
        return hits;
    }

    private Prob testVelocity(final int x, final int y) {
        final Prob prob = new Prob(new Velocity(x, y));
        while (prob.isFly()) {
            prob.applyStep();
        }
        return prob;
    }

    private class Prob {
        private final List<Coordinate> trajectory;
        private Coordinate position;
        private final Velocity velocity;
        private ProbStatus status;

        public Prob(Velocity velocity) {
            this.velocity = velocity;
            this.position = new Coordinate(0, 0);
            this.trajectory = new ArrayList<>();
            this.trajectory.add(position);
            this.status = ProbStatus.FLY;
        }

        public void applyStep() {
            final Coordinate coord = velocity.applyStep(position);
            trajectory.add(coord);
            position = coord;
            status = target.determineStatus(coord);
        }

        public boolean isFly() {
            return status.equals(ProbStatus.FLY);
        }

        public boolean isHit() {
            return status.equals(ProbStatus.HIT);
        }
    }

    private static class Target {

        private final int topRow;
        private final int bottomRow;
        private final int firstColumn;
        private final int lastColumn;

        public Target(final String input) {
            final String[] split = input.split(", ");

            final String[] column = split[0].trim().replace("x=", "").split("\\.\\.");
            firstColumn = Integer.parseInt(column[0]);
            lastColumn = Integer.parseInt(column[1]);

            final String[] row = split[1].trim().replace("y=", "").split("\\.\\.");
            bottomRow = Integer.parseInt(row[0]);
            topRow = Integer.parseInt(row[1]);
        }

        public ProbStatus determineStatus(final Coordinate coord) {
            final int row = coord.row();
            final int column = coord.column();
            if (row < bottomRow || column > lastColumn) {
                return ProbStatus.MISSED;
            } else if ((row <= topRow && row >= bottomRow) && (column >= firstColumn && column <= lastColumn)) {
                return ProbStatus.HIT;
            } else {
                return ProbStatus.FLY;
            }
        }
    }

    private static class Velocity {
        private int x;
        private int y;

        public Velocity(int startX, int startY) {
            this.x = startX;
            this.y = startY;
        }

        public Coordinate applyStep(final Coordinate coord) {
            final Coordinate newCoordinate = new Coordinate(coord.row() + y, coord.column() + x);
            if (x > 0) {
                x--;
            } else if (x < 0) {
                x++;
            }
            y--;

            return newCoordinate;
        }
    }

    private enum ProbStatus {
        MISSED, HIT, FLY
    }
}

