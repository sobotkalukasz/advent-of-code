package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.beaconscanner.Point;

/*
 * https://adventofcode.com/2021/day/22
 * */
public class ReactorReboot {

    private final List<Instruction> instructions;
    private final Range limit;

    public ReactorReboot(final List<String> instructions, final String limit) {
        this.instructions = instructions.stream().map(Instruction::new).collect(Collectors.toList());
        this.limit = new Range(limit);
    }

    public int applyInstructions() {
        final Set<Point> onPoints = new HashSet<>();
        instructions.forEach(instr -> applyInstruction(instr, onPoints));
        return onPoints.size();
    }

    private void applyInstruction(final Instruction instruction, final Set<Point> onPoints) {
        final Range range = instruction.getRange();
        for (int x = range.getMinX(); x <= range.getMaxX(); x++) {
            if (x >= limit.getMinX() && x <= limit.getMaxX()) {
                for (int y = range.getMinY(); y <= range.getMaxY(); y++) {
                    if (y >= limit.getMinY() && y <= limit.getMaxY()) {
                        for (int z = range.getMinZ(); z <= range.getMaxZ(); z++) {
                            if (z >= limit.getMinZ() && z <= limit.getMaxZ()) {
                                final Point actual = new Point(x, y, z);
                                if (instruction.type.equals(Instruction.Type.ON)) {
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

    private static class Instruction {

        private final Type type;
        private final Range range;

        public Instruction(final String instruction) {
            String temp;
            if (instruction.contains(Type.ON.stringValue)) {
                type = Type.ON;
                temp = instruction.replace(Type.ON.stringValue, "");
            } else {
                type = Type.OFF;
                temp = instruction.replace(Type.OFF.stringValue, "");
            }

            range = new Range(temp);
        }

        public Range getRange() {
            return range;
        }

        private enum Type {
            ON("on"), OFF("off");
            private final String stringValue;

            Type(String stringValue) {
                this.stringValue = stringValue;
            }

        }
    }

    private static class Range {
        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;
        private final int minZ;
        private final int maxZ;

        public Range(final String instruction) {
            final String[] values = instruction.replace(" ", "").split(",");

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

        public int getMinX() {
            return minX;
        }

        public int getMaxX() {
            return maxX;
        }

        public int getMinY() {
            return minY;
        }

        public int getMaxY() {
            return maxY;
        }

        public int getMinZ() {
            return minZ;
        }

        public int getMaxZ() {
            return maxZ;
        }
    }

}
