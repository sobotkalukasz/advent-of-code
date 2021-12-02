package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.function.BiConsumer;

public class Dive {

    public int determinePositionAndMultiplyCoords(final List<String> instructions, final boolean withAim) {
        final Position position = new Position(withAim);
        instructions.stream().map(Step::new).forEach(step -> step.updatePosition(position));
        return position.getDepth() * position.getHorizontal();
    }

    private static class Position {
        private int horizontal;
        private int depth;
        private int aim;
        private final boolean withAim;

        public Position(boolean withAim) {
            this.withAim = withAim;
        }

        public void increaseHorizontal(final int value) {
            this.horizontal += value;
            if (withAim) {
                this.depth += aim * value;
            }
        }

        public void increaseDepth(final int value) {
            if (withAim) {
                this.aim += value;
            } else {
                this.depth += value;
            }
        }

        public void decreaseDepth(final int value) {
            if (withAim) {
                this.aim -= value;
            } else {
                this.depth -= value;
            }
        }

        public int getHorizontal() {
            return horizontal;
        }

        public int getDepth() {
            return depth;
        }
    }

    private static class Step {
        private final Type type;
        private final int value;

        public Step(final String instruction) {
            final String[] split = instruction.split("\\s+");
            this.type = Type.from(split[0]);
            this.value = Integer.parseInt(split[1]);
        }

        public void updatePosition(final Position actualPosition) {
            type.getOperation().accept(actualPosition, value);
        }

    }

    private enum Type {
        FORWARD(Position::increaseHorizontal), DOWN(Position::increaseDepth), UP(Position::decreaseDepth);

        private final BiConsumer<Position, Integer> operation;

        Type(BiConsumer<Position, Integer> operation) {
            this.operation = operation;
        }

        public static Type from(final String type) {
            return Type.valueOf(type.toUpperCase().trim());
        }

        public BiConsumer<Position, Integer> getOperation() {
            return operation;
        }
    }

}
