package pl.lsobotka.adventofcode.year_2021.transparentorigami;

public class FoldInstruction {

    private final static String FOLD_DELIMITER = "=";
    private final static String COLUMN_VALUE = "x";

    private final Type type;
    private final int value;

    public FoldInstruction(final String row) {
        final String[] split = row.split(FOLD_DELIMITER);
        type = split[0].contains(COLUMN_VALUE) ? Type.COLUMN : Type.ROW;
        value = Integer.parseInt(split[1]);
    }

    public Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static boolean isFoldInstruction(final String row) {
        return row.contains(FOLD_DELIMITER);
    }

    enum Type {
        ROW, COLUMN
    }
}
