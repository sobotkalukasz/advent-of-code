package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * https://adventofcode.com/2019/day/11
 * */
public class SpacePolice {

    final long[] program;

    public SpacePolice(long[] program) {
        this.program = program;
    }

    public int countPaintedPanels() {
        final PaintingRobot robot = PaintingRobot.initOnBlack();
        paint(robot);
        return robot.getCountOfPaintedPanels();
    }

    public void printPaintedPanels() {
        final PaintingRobot robot = PaintingRobot.initOnWhite();
        paint(robot);
        final String print = robot.printBlackPanels();
        System.out.println(print);
    }

    private void paint(final PaintingRobot robot) {
        final IntCode intCode = new IntCode(program, robot.getActualColor());

        List<Long> instructions = intCode.executeUntilExpectedOutputSize(2);
        while (robot.applyInstruction(instructions)) {
            intCode.addInput(robot.getActualColor());
            intCode.executeUntilExpectedOutputSize(2);
        }
    }

    private static class PaintingRobot {
        final Set<Coordinate> whitePanels;
        final Set<Coordinate> paintedPanels;

        Coordinate actualPosition;
        Direction direction;
        Color actualColor;

        private PaintingRobot(Color startingColor) {
            this.actualColor = startingColor;
            this.whitePanels = new HashSet<>();
            this.paintedPanels = new HashSet<>();
            this.actualPosition = new Coordinate(0, 0);
            this.direction = Direction.N;
        }

        public static PaintingRobot initOnBlack() {
            return new PaintingRobot(Color.BLACK);
        }

        public static PaintingRobot initOnWhite() {
            return new PaintingRobot(Color.WHITE);
        }

        public long getActualColor() {
            return this.actualColor.getValue();
        }

        public boolean applyInstruction(final List<Long> instructions) {
            final boolean processed;
            if (instructions.size() == 2) {
                paintedPanels.add(actualPosition);
                final Color newColor = Color.getByValue(instructions.get(0));

                if (newColor.equals(Color.WHITE)) {
                    whitePanels.add(actualPosition);
                } else {
                    whitePanels.remove(actualPosition);
                }

                this.direction = this.direction.applyRotation(instructions.get(1));
                this.actualPosition = actualPosition.applyDirection(this.direction);
                this.actualColor = whitePanels.contains(actualPosition) ? Color.WHITE : Color.BLACK;

                processed = true;
            } else {
                processed = false;
            }
            return processed;
        }

        public int getCountOfPaintedPanels() {
            return paintedPanels.size();
        }

        public String printBlackPanels() {
            final int minRow = whitePanels.stream().map(Coordinate::row).min(Integer::compare).orElse(0);
            final int maxRow = whitePanels.stream().map(Coordinate::row).max(Integer::compare).orElse(0);
            final int minCol = whitePanels.stream().map(Coordinate::column).min(Integer::compare).orElse(0);
            final int maxCol = whitePanels.stream().map(Coordinate::column).max(Integer::compare).orElse(0);

            final StringBuilder printer = new StringBuilder();

            for (int row = maxRow; row >= minRow; row--) {
                for (int col = minCol; col < maxCol; col++) {
                    if (whitePanels.contains(new Coordinate(row, col))) {
                        printer.append(" # ");
                    } else {
                        printer.append("   ");
                    }
                }
                printer.append("\n");
            }
            return printer.toString();
        }

    }

    enum Direction {
        N, E, S, W;

        public Direction applyRotation(final long rotation) {
            final Direction newDirection;
            if (rotation == 0) {
                newDirection = switch (this) {
                    case N -> W;
                    case E -> N;
                    case S -> E;
                    default -> S;
                };
            } else {
                newDirection = switch (this) {
                    case N -> E;
                    case E -> S;
                    case S -> W;
                    default -> N;
                };
            }
            return newDirection;
        }
    }

    enum Color {
        BLACK(0), WHITE(1);

        final long value;

        Color(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }

        public static Color getByValue(final long value) {
            if (value == 0) {
                return BLACK;
            } else {
                return WHITE;
            }
        }
    }

    record Coordinate(int row, int column) {
        public Coordinate applyDirection(final Direction direction) {
            return switch (direction) {
                case N -> new Coordinate(row + 1, column);
                case E -> new Coordinate(row, column + 1);
                case S -> new Coordinate(row - 1, column);
                default -> new Coordinate(row, column - 1);
            };
        }
    }
}
