package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.beaconscanner.Point;

/*
 * https://adventofcode.com/2021/day/22
 * */
public class ReactorReboot {

    private final List<Range> instructions;
    private final Range limit;

    public ReactorReboot(final List<String> instructions, final String limit) {
        this.instructions = instructions.stream().map(Range::new).collect(Collectors.toList());
        this.limit = Optional.ofNullable(limit).map(Range::new).orElseGet(Range::max);
    }

    public int applyInstructions() {
        final Set<Point> onPoints = new HashSet<>();
        instructions.forEach(instr -> applyInstruction(instr, onPoints));
        return onPoints.size();
    }

    private void applyInstruction(final Range range, final Set<Point> onPoints) {
        for (int x = range.minX; x <= range.maxX; x++) {
            if (x >= limit.minX && x <= limit.maxX) {
                for (int y = range.minY; y <= range.maxY; y++) {
                    if (y >= limit.minY && y <= limit.maxY) {
                        for (int z = range.minZ; z <= range.maxZ; z++) {
                            if (z >= limit.minZ && z <= limit.maxZ) {
                                final Point actual = new Point(x, y, z);
                                if (range.type.equals(Type.ON)) {
                                    onPoints.add(actual);
                                } else {
                                    onPoints.remove(actual);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public long applyBigInstructions() {
        final List<Range> ranges = new ArrayList<>();
        for (final Range newRange : instructions) {
            if (!ranges.isEmpty()) {
                final List<Range> toRemove = new ArrayList<>();
                final Set<Range> splitRanges = new HashSet<>();
                for (Range currentRange : ranges) {
                    if (currentRange.isOverlapping(newRange) && currentRange.isOn()) {
                        final Set<Range> split = currentRange.split(newRange);
                        toRemove.add(currentRange);
                        splitRanges.addAll(split);
                    }
                }
                ranges.removeAll(toRemove);
                ranges.addAll(splitRanges);
            }
            ranges.add(newRange);
        }

        return ranges.stream().filter(Range::isOn).map(Range::getSize).reduce(Long::sum).orElse(0L);
    }

    static class Range {
        private final Type type;
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;
        private int minZ;
        private int maxZ;

        public Range(final String instruction) {
            final String temp;
            if (instruction.contains(Type.ON.stringValue)) {
                type = Type.ON;
                temp = instruction.replace(Type.ON.stringValue, "");
            } else {
                type = Type.OFF;
                temp = instruction.replace(Type.OFF.stringValue, "");
            }

            final String[] values = temp.replace(" ", "").split(",");

            final String[] xValues = values[0].replace("x=", "").split("\\.\\.");
            minX = Integer.parseInt(xValues[0]);
            maxX = Integer.parseInt(xValues[1]);

            final String[] yValues = values[1].replace("y=", "").split("\\.\\.");
            minY = Integer.parseInt(yValues[0]);
            maxY = Integer.parseInt(yValues[1]);

            final String[] zValues = values[2].replace("z=", "").split("\\.\\.");
            minZ = Integer.parseInt(zValues[0]);
            maxZ = Integer.parseInt(zValues[1]);
        }

        public Range(final Type type, final int minX, final int maxX, final int minY, final int maxY, final int minZ,
                final int maxZ) {
            this.type = type;
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
            this.minZ = minZ;
            this.maxZ = maxZ;
        }

        public static Range max() {
            return new Range(Type.OFF, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        public boolean isOn() {
            return this.type.equals(Type.ON);
        }

        public Range copy() {
            return new Range(this.type, this.minX, this.maxX, this.minY, this.maxY, this.minZ, this.maxZ);
        }

        public long getSize() {
            return (long) (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        }

        public boolean isValid() {
            return this.minX <= this.maxX && this.minY <= this.maxY && this.minZ <= this.maxZ;
        }

        public boolean isOverlapping(final Range o) {
            final boolean left = isOverlappingLeft(o);
            final boolean right = isOverlappingRight(o);
            final boolean xInside = isXInside(o);

            final boolean bottom = isOverlappingBottom(o);
            final boolean top = isOverlappingTop(o);
            final boolean yInside = isYInside(o);

            final boolean front = isOverlappingFront(o);
            final boolean back = isOverlappingBack(o);
            final boolean zInside = isZInside(o);

            return ((left || right) || xInside) && ((bottom || top) || yInside) && ((front || back) || zInside);

        }

        private boolean isOverlappingLeft(final Range o) {
            return this.minX <= o.minX && o.minX <= this.maxX;
        }

        private boolean isOverlappingRight(final Range o) {
            return this.minX <= o.maxX && o.maxX <= this.maxX;
        }

        private boolean isOverlappingTop(final Range o) {
            return this.minY <= o.maxY && o.maxY <= this.maxY;
        }

        private boolean isOverlappingBottom(final Range o) {
            return this.minY <= o.minY && o.minY <= this.maxY;
        }

        private boolean isOverlappingFront(final Range o) {
            return this.minZ <= o.minZ && o.minZ <= this.maxZ;
        }

        private boolean isOverlappingBack(final Range o) {
            return this.minZ <= o.maxZ && o.maxZ <= this.maxZ;
        }

        public boolean isXInside(final Range o) {
            return o.minX <= this.minX && this.maxX <= o.maxX;
        }

        public boolean isYInside(final Range o) {
            return o.minY <= this.minY && this.maxY <= o.maxY;
        }

        public boolean isZInside(final Range o) {
            return o.minZ <= this.minZ && this.maxZ <= o.maxZ;
        }

        public Set<Range> split(final Range o) {
            final Set<Range> splitResult = new HashSet<>();
            final Range copy = this.copy();

            if (copy.isOverlappingLeft(o)) {
                final Range range = new Range(copy.type, copy.minX, o.minX - 1, copy.minY, copy.maxY, copy.minZ,
                        copy.maxZ);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.minX = o.minX;
                }
            }

            if (copy.isOverlappingRight(o)) {
                final Range range = new Range(copy.type, o.maxX + 1, copy.maxX, copy.minY, copy.maxY, copy.minZ,
                        copy.maxZ);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.maxX = o.maxX;
                }
            }

            if (copy.isOverlappingTop(o)) {
                final Range range = new Range(copy.type, copy.minX, copy.maxX, o.maxY + 1, copy.maxY, copy.minZ,
                        copy.maxZ);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.maxY = o.maxY;
                }
            }

            if (copy.isOverlappingBottom(o)) {
                final Range range = new Range(copy.type, copy.minX, copy.maxX, copy.minY, o.minY - 1, copy.minZ,
                        copy.maxZ);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.minY = o.minY;
                }
            }

            if (copy.isOverlappingBack(o)) {
                final Range range = new Range(copy.type, copy.minX, copy.maxX, copy.minY, copy.maxY, o.maxZ + 1,
                        copy.maxZ);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.maxZ = o.maxZ;
                }
            }

            if (copy.isOverlappingFront(o)) {
                final Range range = new Range(copy.type, copy.minX, copy.maxX, copy.minY, copy.maxY, copy.minZ,
                        o.minZ - 1);
                if (range.isValid()) {
                    splitResult.add(range);
                    copy.minZ = o.minZ;
                }
            }

            return splitResult;
        }
    }

    private enum Type {
        ON("on"), OFF("off");
        private final String stringValue;

        Type(String stringValue) {
            this.stringValue = stringValue;
        }
    }

}
