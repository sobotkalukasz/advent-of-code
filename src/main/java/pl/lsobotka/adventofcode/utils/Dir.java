package pl.lsobotka.adventofcode.utils;

import java.util.Arrays;
import java.util.Set;

public enum Dir {

    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

    private static final Set<Character> symbols = Set.of('^', 'v', '<', '>');
    final char symbol;

    Dir(char symbol) {
        this.symbol = symbol;
    }

    public static boolean isDir(char c) {
        return symbols.contains(c);
    }

    public static Dir of(final char c) {
        return Arrays.stream(Dir.values())
                .filter(w -> w.symbol == c)
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
