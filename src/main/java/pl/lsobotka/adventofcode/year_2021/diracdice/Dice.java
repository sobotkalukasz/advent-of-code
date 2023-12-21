package pl.lsobotka.adventofcode.year_2021.diracdice;

import java.util.stream.IntStream;

public class Dice {

    private int pointer;
    private int rollCount;

    public Dice() {
        pointer = 0;
        rollCount = 0;
    }

    public int getRollCount() {
        return rollCount;
    }

    public int roll() {
        return IntStream.rangeClosed(1, 3).map(turn -> singleRoll()).sum();
    }

    public int singleRoll() {
        pointer++;
        if (pointer == 101) {
            pointer = 1;
        }
        rollCount++;
        return pointer;
    }
}
