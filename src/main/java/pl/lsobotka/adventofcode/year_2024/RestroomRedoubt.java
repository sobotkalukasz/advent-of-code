package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    long displayTreeAfter(final int tall, final int wide, final int maxTime) {
        for (int i = 0; i < maxTime; i++) {
            final int seconds = i + 1;
            final Set<Coord> moved = robots.stream()
                    .map(r -> r.positionAfter(tall, wide, seconds))
                    .collect(Collectors.toSet());

            for (Coord coord : moved) {
                if (isPyramid(moved, coord.row(), coord.col())) {
                    printTree(tall, wide, moved);
                    return seconds;
                }
            }
        }
        return 0;
    }

    private boolean isPyramid(final Set<Coord> coordSet, final int row, final int col) {
        if (!coordSet.contains(new Coord(row + 1, col - 1)) || !coordSet.contains(new Coord(row + 1, col))
                || !coordSet.contains(new Coord(row + 1, col + 1))) {
            return false;
        }

        return coordSet.contains(new Coord(row + 2, col - 2)) && coordSet.contains(new Coord(row + 2, col - 1))
                && coordSet.contains(new Coord(row + 2, col)) && coordSet.contains(new Coord(row + 2, col + 1))
                && coordSet.contains(new Coord(row + 2, col + 2));
    }

    private void printTree(int tall, int wide, Set<Coord> moved) {
        final StringBuilder treeBuilder = new StringBuilder();
        for (int row = 0; row < tall; row++) {
            for (int col = 0; col < wide; col++) {
                final Coord coord = Coord.of(row, col);
                if (moved.contains(coord)) {
                    treeBuilder.append("X");
                } else {
                    treeBuilder.append(" ");
                }
            }
            treeBuilder.append("\n");
        }
        System.out.println(treeBuilder);
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
