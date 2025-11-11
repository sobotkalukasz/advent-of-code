package pl.lsobotka.adventofcode.utils;

import java.util.Arrays;

public enum Dir {

    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

    final char code;

    Dir(char code) {
        this.code = code;
    }

    public static boolean isDir(final long code) {
        return code == UP.code || code == DOWN.code || code == LEFT.code || code == RIGHT.code;
    }

    public static Dir of(final long c) {
        return Arrays.stream(Dir.values())
                .filter(w -> w.code == c)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown Dir symbol: " + c));
    }

    public Dir rotate(Rotate rotate) {
        return switch (this) {
            case UP -> rotate == Rotate.L ? LEFT : RIGHT;
            case DOWN -> rotate == Rotate.L ? RIGHT : LEFT;
            case LEFT -> rotate == Rotate.L ? DOWN : UP;
            case RIGHT -> rotate == Rotate.L ? UP : DOWN;
        };
    }
}
