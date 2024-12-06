package pl.lsobotka.adventofcode.utils;

public enum Dir {

    UP, DOWN, LEFT, RIGHT;

    public Dir rotate(Rotate rotate) {
        return switch (this) {
            case UP -> rotate == Rotate.L ? LEFT : RIGHT;
            case DOWN -> rotate == Rotate.L ? RIGHT : LEFT;
            case LEFT -> rotate == Rotate.L ? DOWN : UP;
            case RIGHT -> rotate == Rotate.L ? UP : DOWN;
        };
    }
}
