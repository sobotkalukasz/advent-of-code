package pl.lsobotka.adventofcode.diracdice;

public class Player {
    private int actualSpace;
    private int score;

    public Player(int actualSpace) {
        this.actualSpace = actualSpace;
    }


    public int applyTurn(final Dice dice) {
        final int roll = dice.roll();
        return applyTurn(roll);
    }

    public int applyTurn(final int value) {
        this.actualSpace = (actualSpace + value) % 10;
        this.score += actualSpace == 0 ? 10 : actualSpace;
        return this.score;
    }

    public int getScore() {
        return score;
    }

}
