package pl.lsobotka.adventofcode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class Snailfish {

    protected static Fish add(final List<String> rawInput) {
        final List<Fish> fishes = rawInput.stream().map(Fish::init).collect(Collectors.toList());
        return fishes.stream().reduce(Fish::add).orElseThrow(IllegalArgumentException::new);
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
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
            final Fish newFish = new FishBuilder().leftNode(this).rightNode(other).build();
            newFish.validate();
            return newFish;
        }

        protected void validate() {
            Explosion exploded;
            do {
                exploded = validateExplosion(0);

                SplitResult splitResult;
                do {
                    splitResult = validateSplit(0);
                } while (splitResult.isSplit() && !splitResult.shouldExplode);

            } while (Objects.nonNull(exploded));
        }

        private Explosion validateExplosion(final int nestedLevel) {
            Explosion exploded = null;
            if (nestedLevel >= 3 && !isMoreNestedLevel()) {
            //if (nestedLevel >= 3) {
                exploded = explode();
            } else {
                if (isLeftNode()) {
                    exploded = leftNode.validateExplosion(nestedLevel + 1);
                    if (Objects.nonNull(exploded)) {
                        if (exploded.isRight()) {
                            if (isRight()) {
                                right += exploded.value;
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
                                left += exploded.value;
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

        private boolean isMoreNestedLevel(){
            boolean moreLevel = false;
            if(isLeftNode()){
               moreLevel = leftNode.isLeftNode() || leftNode.isRightNode();
            } else if(!moreLevel && isRightNode()){
                moreLevel = rightNode.isLeftNode() || rightNode.isRightNode();
            }
            return moreLevel;
        }

        private Explosion explode() {
            Explosion explosion = null;
            if (isLeftNode()) {
                this.left = 0;
                explosion = Explosion.left(leftNode.left);
                if (isRight()) {
                    right += leftNode.right;
                } else {
                    if(rightNode.isLeft()){
                        rightNode.left += leftNode.right;
                    }
                }
                this.leftNode = null;
            } else if (isRightNode()) {
                this.right = 0;
                explosion = Explosion.right(rightNode.right);
                if (isLeft()) {
                    left += rightNode.left;
                } else {
                    leftNode.right += rightNode.left;
                }
                this.rightNode = null;
            }
            return explosion;
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
                    final Fish leftFish = applySplit(this.left);
                    this.leftNode = leftFish;
                    this.left = null;
                    splitResult = nestedLevel >= 3 ? SplitResult.shouldExplode() : SplitResult.split();
                }
            } else {
                splitResult = this.leftNode.validateSplit(nestedLevel + 1);
            }

            if (!splitResult.isShouldExplode() && this.isRight()) {
                if (shouldSplit(this.right)) {
                    final Fish rightFish = applySplit(this.right);
                    this.rightNode = rightFish;
                    this.right = null;
                    splitResult = nestedLevel >= 3 ? SplitResult.shouldExplode() : SplitResult.split();
                }
            } else if (!splitResult.isShouldExplode()) {
                splitResult = this.rightNode.validateSplit(nestedLevel + 1);
            }

            return splitResult;
        }

        private boolean shouldSplit(final int value) {
            return value >= 10;
        }

        private Fish applySplit(final int value) {
            final BigDecimal toSplit = BigDecimal.valueOf(value);
            final BigDecimal divisor = BigDecimal.valueOf(2);
            return new FishBuilder().left(toSplit.divide(divisor, RoundingMode.DOWN).intValue())
                    .right(toSplit.divide(divisor, RoundingMode.UP).intValue())
                    .build();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Fish{");
            if (Objects.nonNull(left)) {
                sb.append("left=").append(left);
            }
            if (Objects.nonNull(leftNode)) {
                sb.append("leftNode=").append(leftNode);
            }
            if (Objects.nonNull(right)) {
                sb.append(", right=").append(right);
            }
            if (Objects.nonNull(rightNode)) {
                sb.append(", rightNode=").append(rightNode);
            }
            sb.append('}');
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
    }

    private static class Explosion {

        Integer value;
        Type type;

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
            return type.equals(Type.RIGHT)&& shouldApply();
        }

        enum Type {
            LEFT, RIGHT
        }
    }

    private static class SplitResult {
        boolean split;
        boolean shouldExplode;

        private SplitResult(boolean split, boolean shouldExplode) {
            this.split = split;
            this.shouldExplode = shouldExplode;
        }

        public static SplitResult split() {
            return new SplitResult(true, false);
        }

        public static SplitResult shouldExplode() {
            return new SplitResult(true, true);
        }

        public static SplitResult none() {
            return new SplitResult(false, false);
        }

        public boolean isSplit() {
            return split;
        }

        public boolean isShouldExplode() {
            return shouldExplode;
        }
    }

}
