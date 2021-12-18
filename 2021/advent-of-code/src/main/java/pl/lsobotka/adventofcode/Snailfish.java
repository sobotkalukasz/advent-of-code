package pl.lsobotka.adventofcode;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class Snailfish {

    protected static Fish add(final List<String> rawInput) {
        final List<Fish> fishes = rawInput.stream().map(Fish::init).collect(Collectors.toList());
        return fishes.stream().reduce((a, b) -> a.add(b)).orElseThrow(IllegalArgumentException::new);
    }

    @Builder
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fish {
        private Integer x;
        private Fish fishX;
        private Integer y;
        private Fish fishY;
        private boolean isExploded;

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
                fish.fishX = new Fish();
                pointer = init(fish.fishX, rawData, pointer);
            } else if (Character.isDigit(first)) {
                fish.x = Integer.parseInt(String.valueOf(first));
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
                fish.fishY = new Fish();
                pointer = init(fish.fishY, rawData, pointer);
            } else if (Character.isDigit(second)) {
                fish.y = Integer.parseInt(String.valueOf(second));
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
            final Fish newFish = new FishBuilder().fishX(this).fishY(other).build();
            newFish.validate();
            return newFish;
        }

        private void resetStatus() {
            this.isExploded = false;
            if (Objects.nonNull(fishX)) {
                fishX.resetStatus();
            }
            if (Objects.nonNull(fishY)) {
                fishY.resetStatus();
            }
        }

        protected void validate() {
            Fish exploded;
            do {
                exploded = validateExploded(0);
                System.out.println(exploded);
                resetStatus();
            } while (Objects.nonNull(exploded));
        }

        private Fish validateExploded(final int nested) {
            Fish exploded = null;
            if (nested < 3) {
                if (Objects.nonNull(fishX)) {
                    exploded = fishX.validateExploded(nested + 1);
                    if (nested == 0 && Objects.nonNull(exploded) && Objects.nonNull(fishY)) {
                        final Fish reversed = fishY.applyExploded(new FishBuilder().x(exploded.y).build());
                        exploded = new FishBuilder().x(exploded.x).y(reversed.x).build();
                    }
                    exploded = applyExploded(exploded);
                }
                if (Objects.isNull(exploded) && Objects.nonNull(fishY)) {
                    exploded = fishY.validateExploded(nested + 1);
/*                    if (nested == 0 && Objects.nonNull(exploded) && Objects.nonNull(fishX)) {
                        final Fish reversed = fishX.applyExploded(new FishBuilder().y(exploded.x).build());
                        exploded = new FishBuilder().x(reversed.y).y(exploded.y).build();
                    }*/
                    exploded = applyExploded(exploded);
                }

            } else if (nested == 3) {
                if (Objects.nonNull(fishX)) {
                    this.isExploded = true;
                    this.x = 0;
                    exploded = new FishBuilder().x(fishX.x).y(fishX.y).build();
                    this.fishX = null;
                    exploded = applyExploded(exploded);
                } else if (Objects.nonNull(fishY)) {
                    this.isExploded = true;
                    this.y = 0;
                    exploded = new FishBuilder().x(fishY.x).y(fishY.y).build();
                    this.fishY = null;
                    exploded = applyExploded(exploded);
                }
            }
            return exploded;
        }

        private Fish applyExplodedX(Fish exploded) {
            if (Objects.nonNull(exploded)) {
                if (Objects.nonNull(exploded.x)) {
                    if (Objects.nonNull(this.x) && !(this.x == 0 && isExploded)) {
                        this.x += exploded.x;
                        exploded = new FishBuilder().y(exploded.y).build();
                    } else if (Objects.nonNull(this.fishX)) {
                        exploded = fishX.applyExploded(exploded);
                    }
                }
            }
            return exploded;
        }

        private Fish applyExplodedY(Fish exploded) {
            if (Objects.nonNull(exploded)) {
                if (Objects.nonNull(exploded.y)) {
                    if (Objects.nonNull(this.y) && !(this.y == 0 && isExploded)) {
                        this.y += exploded.y;
                        exploded = new FishBuilder().x(exploded.x).build();
                    } else if (Objects.nonNull(this.fishY)) {
                        exploded = fishY.applyExploded(exploded);
                    }
                }
            }
            return exploded;
        }

        private Fish applyExploded(Fish exploded) {
            if (Objects.nonNull(exploded)) {
                if (Objects.nonNull(exploded.x)) {
                    if (Objects.nonNull(this.x) && !(this.x == 0 && isExploded)) {
                        this.x += exploded.x;
                        exploded = new FishBuilder().y(exploded.y).build();
                    } else if (Objects.nonNull(this.fishX)) {
                        exploded = fishX.applyExploded(exploded);
                    }
                }
                if (Objects.nonNull(exploded.y)) {
                    if (Objects.nonNull(this.y) && !(this.y == 0 && isExploded)) {
                        this.y += exploded.y;
                        exploded = new FishBuilder().x(exploded.x).build();
                    } else if (Objects.nonNull(this.fishY)) {
                        exploded = fishY.applyExploded(exploded);
                    }
                }
            }
            return exploded;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Fish{");
            if (Objects.nonNull(x)) {
                sb.append("x=").append(x);
            }
            if (Objects.nonNull(fishX)) {
                sb.append("fishX=").append(fishX);
            }
            if (Objects.nonNull(y)) {
                sb.append(", y=").append(y);
            }
            if (Objects.nonNull(fishY)) {
                sb.append(", fishY=").append(fishY);
            }
            sb.append('}');
            return sb.toString();
        }

    }

}
