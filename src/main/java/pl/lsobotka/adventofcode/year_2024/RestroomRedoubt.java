package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.lsobotka.adventofcode.utils.Coord;

public class RestroomRedoubt {

    private static final Pattern PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
    private final List<Robot> robots;

    public RestroomRedoubt(final List<String> lines) {

        this.robots = new ArrayList<>();

        for (String line : lines) {
            Matcher matcher = PATTERN.matcher(line);
            if (matcher.matches()) {
                int posCol = Integer.parseInt(matcher.group(1));
                int posRow = Integer.parseInt(matcher.group(2));
                int velCol = Integer.parseInt(matcher.group(3));
                int velRow = Integer.parseInt(matcher.group(4));

                Coord pos = new Coord(posRow, posCol);
                Coord vel = new Coord(velRow, velCol);

                robots.add(new Robot(pos, vel));
            }
        }
    }

    long determineSafetyFactor(int tall, int wide, int seconds) {
        final List<Coord> moved = robots.stream().map(r -> r.positionAfter(tall, wide, seconds)).toList();
        final Coord middle = Coord.of(tall / 2, wide / 2);

        long[] quadrants = new long[4];
        for (Coord move : moved) {
            if (move.row() < middle.row()) {
                if (move.col() < middle.col()) {
                    quadrants[0]++;
                } else if (move.col() > middle.col()) {
                    quadrants[1]++;
                }
            } else if (move.row() > middle.row()) {
                if (move.col() < middle.col()) {
                    quadrants[2]++;
                } else if (move.col() > middle.col()) {
                    quadrants[3]++;
                }
            }
        }

        return quadrants[0] * quadrants[1] * quadrants[2] * quadrants[3];
    }

    record Robot(Coord pos, Coord vel) {

        Coord positionAfter(int tall, int wide, int seconds) {
            int moveVelCol = vel.col() * seconds;
            int moveVelRow = vel.row() * seconds;

            int afterCol = moveVelCol + pos.col();
            int afterRow = moveVelRow + pos.row();

            int col = Math.floorMod(afterCol, wide);
            int row = Math.floorMod(afterRow, tall);

            return Coord.of(row, col);
        }
    }
}
