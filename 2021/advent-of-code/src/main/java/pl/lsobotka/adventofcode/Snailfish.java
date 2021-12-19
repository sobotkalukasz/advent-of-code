package pl.lsobotka.adventofcode;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class Snailfish {

    protected static Fish add(final List<String> rawInput) {
        final List<Fish> fishes = rawInput.stream().map(Fish::init).collect(Collectors.toList());
        return fishes.stream().reduce(Fish::add).orElseThrow(IllegalArgumentException::new);
    }

    @Builder
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fish {
        private Integer left;
        private Fish fishLeft;
        private Integer right;
        private Fish fishRight;

        public boolean isLeft() {
            return Objects.nonNull(left);
        }

        public boolean isFishLeft() {
            return Objects.nonNull(fishLeft);
        }

        public boolean isRight() {
            return Objects.nonNull(right);
        }

        public boolean isFishRight() {
            return Objects.nonNull(fishRight);
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
                fish.fishLeft = new Fish();
                pointer = init(fish.fishLeft, rawData, pointer);
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
                fish.fishRight = new Fish();
                pointer = init(fish.fishRight, rawData, pointer);
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
            final Fish newFish = new FishBuilder().fishLeft(this).fishRight(other).build();
            newFish.validate();
            return newFish;
        }

        protected void validate() {
            Explosion exploded;
            do {
                exploded = validateExplosion(0);
                System.out.println(exploded);
            } while (Objects.nonNull(exploded));
        }

        private Explosion validateExplosion(final int nested) {
            Explosion exploded = null;
            if (nested == 3) {
                exploded = explode();
            } else {
                if (isFishLeft()) {
                    exploded = fishLeft.validateExplosion(nested + 1);
                    if (Objects.nonNull(exploded)) {
                        if (exploded.isRight()) {
                            if (isRight()) {
                                right += exploded.value;
                                exploded.erase();
                            } else if (isFishRight()) {
                                exploded = fishRight.applyRightExplosion(exploded);
                            }
                        }
                    }
                }
                if (Objects.isNull(exploded) && isFishRight()) {
                    exploded = fishRight.validateExplosion(nested + 1);
                    if (Objects.nonNull(exploded)) {
                        if (exploded.isLeft()) {
                            if (isLeft()) {
                                left += exploded.value;
                                exploded.erase();
                            } else if (isFishLeft()) {
                                exploded = fishLeft.applyLeftExplosion(exploded);
                            }
                        }
                    }
                }
            }
            return exploded;
        }

        private Explosion explode() {
            Explosion explosion = null;
            if (isFishLeft()) {
                this.left = 0;
                explosion = Explosion.left(fishLeft.left);
                if (isRight()) {
                    right += fishLeft.right;
                } else {
                    fishRight.left += fishLeft.right;
                }
                this.fishLeft = null;
            } else if (isFishRight()) {
                this.right = 0;
                explosion = Explosion.right(fishRight.right);
                if (isLeft()) {
                    left += fishRight.left;
                } else {
                    fishLeft.right += fishRight.left;
                }
                this.fishRight = null;
            }
            return explosion;
        }

        private Explosion applyRightExplosion(Explosion exploded) {
            if (Objects.nonNull(exploded) && exploded.shouldApply()) {
                if (isLeft()) {
                    this.left += exploded.value;
                    exploded.erase();
                } else if (isFishLeft()) {
                    exploded = fishLeft.applyRightExplosion(exploded);
                }
            }
            return exploded;
        }

        private Explosion applyLeftExplosion(Explosion exploded) {
            if (Objects.nonNull(exploded) && exploded.shouldApply()) {
                if (isRight()) {
                    this.right += exploded.value;
                    exploded.erase();
                } else if (isFishRight()) {
                    exploded = fishRight.applyLeftExplosion(exploded);
                }
            }
            return exploded;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Fish{");
            if (Objects.nonNull(left)) {
                sb.append("x=").append(left);
            }
            if (Objects.nonNull(fishLeft)) {
                sb.append("fishX=").append(fishLeft);
            }
            if (Objects.nonNull(right)) {
                sb.append(", y=").append(right);
            }
            if (Objects.nonNull(fishRight)) {
                sb.append(", fishY=").append(fishRight);
            }
            sb.append('}');
            return sb.toString();
        }

    }

    @ToString
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
            return type.equals(Type.LEFT);
        }

        public boolean isRight() {
            return type.equals(Type.RIGHT);
        }

        enum Type {
            LEFT, RIGHT
        }
    }

}
