package pl.lsobotka.adventofcode.year_2021;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
 * https://adventofcode.com/2021/day/18
 * */
public class Snailfish {

    protected static Fish add(final List<String> rawInput) {
        final List<Fish> fishes = rawInput.stream().map(Fish::init).toList();
        return fishes.stream().reduce(Fish::add).orElseThrow(IllegalArgumentException::new);
    }

    protected static long findBiggestMagnitude(final List<String> rawInput) {
        final Map<Fish, Long> magnitudeMap = new HashMap<>();

        for (int i = 0; i < rawInput.size() - 1; i++) {
            for (int j = i + 1; j < rawInput.size(); j++) {
                final Fish add = Snailfish.add(Arrays.asList(rawInput.get(i), rawInput.get(j)));
                magnitudeMap.put(add, add.getMagnitude());

                final Fish addReversed = Snailfish.add(Arrays.asList(rawInput.get(j), rawInput.get(i)));
                magnitudeMap.put(addReversed, addReversed.getMagnitude());
            }
        }

        return magnitudeMap.values().stream().max(Long::compareTo).orElse(0L);
    }

    public static class Fish {

        private Integer left;
        private Fish leftNode;
        private Integer right;
        private Fish rightNode;

        public boolean isLeft() {
            return Objects.nonNull(left);
        }

        public boolean isLeftNode() {
            return Objects.nonNull(leftNode);
        }

        public boolean isRight() {
            return Objects.nonNull(right);
        }

        public boolean isRightNode() {
            return Objects.nonNull(rightNode);
        }

        public static Fish init(final String rawData) {
            final Fish fish = new Fish();
            final int finishPointer = init(fish, rawData, 1);

            if (finishPointer != rawData.length()) {
                throw new InputMismatchException(
                        String.format("Expected close pointer at [%d] but is at [%d]", rawData.length(),
                                finishPointer));
            }
            return fish;
        }

        private static int init(final Fish fish, final String rawData, int pointer) {
            final char first = rawData.charAt(pointer);
            pointer++;
            if (first == '[') {
                fish.leftNode = new Fish();
                pointer = init(fish.leftNode, rawData, pointer);
            } else if (Character.isDigit(first)) {
                fish.left = Integer.parseInt(String.valueOf(first));
            } else {
                throw new IllegalArgumentException(String.valueOf(first));
            }

            final char coma = rawData.charAt(pointer);
            pointer++;
            if (coma != ',') {
                throw new IllegalArgumentException(String.format("Expected [%s] but found [%s]", ",", coma));
            }

            final char second = rawData.charAt(pointer);
            pointer++;
            if (second == '[') {
                fish.rightNode = new Fish();
                pointer = init(fish.rightNode, rawData, pointer);
            } else if (Character.isDigit(second)) {
                fish.right = Integer.parseInt(String.valueOf(second));
            } else {
                throw new IllegalArgumentException(String.valueOf(second));
            }

            final char close = rawData.charAt(pointer);
            pointer++;
            if (close != ']') {
                throw new IllegalArgumentException(String.format("Expected [%s] but found [%s]", "]", close));
            }
            return pointer;
        }

        public Fish add(final Fish other) {
            final Fish newFish = new Builder().leftNode(this).rightNode(other).build();
            newFish.validate();
            return newFish;
        }

        public long getMagnitude() {
            final long leftMagnitude = isLeft() ? this.left : this.leftNode.getMagnitude();
            final long rightMagnitude = isRight() ? this.right : this.rightNode.getMagnitude();
            return leftMagnitude * 3 + rightMagnitude * 2;
        }

        protected void validate() {
            Explosion exploded;
            SplitResult splitResult;
            do {
                do {
                    exploded = validateExplosion(0);
                } while (Objects.nonNull(exploded));

                do {
                    splitResult = validateSplit(0);
                } while (splitResult.isSplit() && !splitResult.isExplode());

            } while (splitResult.isSplit());
        }

        private Explosion validateExplosion(final int nestedLevel) {
            Explosion exploded = null;
            if (nestedLevel >= 3 && canExplode()) {
                exploded = explode();
            } else {
                if (isLeftNode()) {
                    exploded = leftNode.validateExplosion(nestedLevel + 1);
                    if (Objects.nonNull(exploded)) {
                        if (exploded.isRight()) {
                            if (isRight()) {
                                right += exploded.getValue();
                                exploded.erase();
                            } else if (isRightNode()) {
                                exploded = rightNode.applyRightExplosion(exploded);
                            }
                        }
                    }
                }
                if (Objects.isNull(exploded) && isRightNode()) {
                    exploded = rightNode.validateExplosion(nestedLevel + 1);
                    if (Objects.nonNull(exploded)) {
                        if (exploded.isLeft()) {
                            if (isLeft()) {
                                left += exploded.getValue();
                                exploded.erase();
                            } else if (isLeftNode()) {
                                exploded = leftNode.applyLeftExplosion(exploded);
                            }
                        }
                    }
                }
            }
            return exploded;
        }

        private boolean canExplode() {
            boolean isMoreLevels = false;
            if (isLeftNode()) {
                isMoreLevels = leftNode.isLeftNode() || leftNode.isRightNode();
            }
            if (!isMoreLevels && isRightNode()) {
                isMoreLevels = rightNode.isLeftNode() || rightNode.isRightNode();
            }
            return !isMoreLevels;
        }

        private boolean canExplode(final Fish node) {
            boolean isMoreLevels = node.isLeftNode() || node.isRightNode();
            return !isMoreLevels;
        }

        private Explosion explode() {
            Explosion explosion = null;
            if (isLeftNode()) {
                if (canExplode(leftNode)) {
                    this.left = 0;
                    explosion = Explosion.left(leftNode.left);
                    if (isRight()) {
                        right += leftNode.right;
                    } else {
                        applyLeftExplosion(rightNode, leftNode.right);
                    }
                    this.leftNode = null;
                } else {
                    explosion = leftNode.explode();
                }

            }
            if (isRightNode() && Objects.isNull(explosion)) {
                if (canExplode(rightNode)) {
                    this.right = 0;
                    explosion = Explosion.right(rightNode.right);
                    if (isLeft()) {
                        left += rightNode.left;
                    } else {
                        applyRightExplosion(leftNode, rightNode.left);
                    }
                    this.rightNode = null;
                } else {
                    explosion = rightNode.explode();
                }
            }
            return explosion;
        }

        private void applyLeftExplosion(final Fish node, final int value) {
            if (isLeft()) {
                node.left += value;
            } else {
                applyLeftExplosion(node.leftNode, value);
            }
        }

        private void applyRightExplosion(final Fish node, final int value) {
            if (isRight()) {
                node.right += value;
            } else {
                applyRightExplosion(node.rightNode, value);
            }
        }

        private Explosion applyRightExplosion(Explosion exploded) {
            if (Objects.nonNull(exploded) && exploded.shouldApply()) {
                if (isLeft()) {
                    this.left += exploded.value;
                    exploded.erase();
                } else if (isLeftNode()) {
                    exploded = leftNode.applyRightExplosion(exploded);
                }
            }
            return exploded;
        }

        private Explosion applyLeftExplosion(Explosion exploded) {
            if (Objects.nonNull(exploded) && exploded.shouldApply()) {
                if (isRight()) {
                    this.right += exploded.value;
                    exploded.erase();
                } else if (isRightNode()) {
                    exploded = rightNode.applyLeftExplosion(exploded);
                }
            }
            return exploded;
        }

        private SplitResult validateSplit(final int nestedLevel) {

            SplitResult splitResult = SplitResult.none();
            if (this.isLeft()) {
                if (shouldSplit(this.left)) {
                    this.leftNode = applySplit(this.left);
                    this.left = null;
                    splitResult = nestedLevel >= 3 ? SplitResult.forExplode() : SplitResult.forSplit();
                }
            }

            if (!splitResult.explode && this.isLeftNode()) {
                final SplitResult result = this.leftNode.validateSplit(nestedLevel + 1);
                if (result.isSplit()) {
                    splitResult = result;
                }
            }

            if (!splitResult.isExplode() && this.isRight()) {
                if (shouldSplit(this.right)) {
                    this.rightNode = applySplit(this.right);
                    this.right = null;
                    splitResult = nestedLevel >= 3 ? SplitResult.forExplode() : SplitResult.forSplit();
                }
            }

            if (!splitResult.isExplode() && this.isRightNode()) {
                final SplitResult result = this.rightNode.validateSplit(nestedLevel + 1);
                if (result.isSplit()) {
                    splitResult = result;
                }
            }

            return splitResult;
        }

        private boolean shouldSplit(final int value) {
            return value >= 10;
        }

        private Fish applySplit(final int value) {
            final BigDecimal toSplit = BigDecimal.valueOf(value);
            final BigDecimal divisor = BigDecimal.valueOf(2);
            return new Builder().left(toSplit.divide(divisor, RoundingMode.DOWN).intValue())
                    .right(toSplit.divide(divisor, RoundingMode.UP).intValue())
                    .build();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("[");
            if (Objects.nonNull(left)) {
                sb.append(left);
            }
            if (Objects.nonNull(leftNode)) {
                sb.append(leftNode);
            }
            if (Objects.nonNull(right)) {
                sb.append(",").append(right);
            }
            if (Objects.nonNull(rightNode)) {
                sb.append(",").append(rightNode);
            }
            sb.append(']');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Fish fish = (Fish) o;
            return Objects.equals(left, fish.left) && Objects.equals(leftNode, fish.leftNode) && Objects.equals(right,
                    fish.right) && Objects.equals(rightNode, fish.rightNode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, leftNode, right, rightNode);
        }

        static class Builder {
            private Integer left;
            private Fish leftNode;
            private Integer right;
            private Fish rightNode;

            public static Builder builder() {
                return new Builder();
            }

            public Builder left(final int left) {
                this.left = left;
                return this;
            }

            public Builder leftNode(final Fish leftNode) {
                this.leftNode = leftNode;
                return this;
            }

            public Builder right(final int right) {
                this.right = right;
                return this;
            }

            public Builder rightNode(final Fish rightNode) {
                this.rightNode = rightNode;
                return this;
            }

            public Fish build() {
                final Fish fish = new Fish();
                fish.left = left;
                fish.leftNode = leftNode;
                fish.right = right;
                fish.rightNode = rightNode;
                return fish;
            }
        }

    }

    private static class Explosion {

        private Integer value;
        private final Type type;

        private Explosion(Integer value, Type type) {
            this.value = value;
            this.type = type;
        }

        public static Explosion left(final int value) {
            return new Explosion(value, Type.LEFT);
        }

        public static Explosion right(final int value) {
            return new Explosion(value, Type.RIGHT);
        }

        public Integer getValue() {
            return value;
        }

        public void erase() {
            value = null;
        }

        public boolean shouldApply() {
            return Objects.nonNull(value);
        }

        public boolean isLeft() {
            return type.equals(Type.LEFT) && shouldApply();
        }

        public boolean isRight() {
            return type.equals(Type.RIGHT) && shouldApply();
        }

        enum Type {
            LEFT, RIGHT
        }
    }

    private record SplitResult(boolean split, boolean explode) {
        public static SplitResult forSplit() {
            return new SplitResult(true, false);
        }

        public static SplitResult forExplode() {
            return new SplitResult(true, true);
        }

        public static SplitResult none() {
            return new SplitResult(false, false);
        }

        public boolean isSplit() {
            return split;
        }

        public boolean isExplode() {
            return explode;
        }
    }

}
