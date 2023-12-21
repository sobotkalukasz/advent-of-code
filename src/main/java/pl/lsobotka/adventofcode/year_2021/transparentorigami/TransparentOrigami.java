package pl.lsobotka.adventofcode.year_2021.transparentorigami;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.year_2021.Coordinate;

public class TransparentOrigami {
    private static final String COORDINATE_DELIMITER = ",";

    private final Set<Coordinate> dots;
    private final List<FoldInstruction> instructions;

    public TransparentOrigami(final List<String> inputRows) {

        dots = new HashSet<>();
        instructions = new ArrayList<>();
        for (String inputRow : inputRows) {
            if (inputRow.contains(COORDINATE_DELIMITER)) {
                final String[] split = inputRow.split(COORDINATE_DELIMITER);
                final int row = Integer.parseInt(split[1]);
                final int column = Integer.parseInt(split[0]);
                dots.add(new Coordinate(row, column));
            } else if (FoldInstruction.isFoldInstruction(inputRow)) {
                instructions.add(new FoldInstruction(inputRow));
            }
        }
    }

    public int foldAndCountDots(final int qtyOfFolds) {
        int index = 0;
        do {
            fold(instructions.get(index));
        } while (index++ >= qtyOfFolds || dots.size() < index);

        return dots.size();
    }

    public void foldAndPrintCode() {
        instructions.forEach(this::fold);
        printCode();
    }

    private void fold(final FoldInstruction instruction) {
        final int foldValue = instruction.getValue();
        final Set<Coordinate> dotsToRemove;
        final Set<Coordinate> dotsToFold;
        final Set<Coordinate> newDots;

        if (instruction.getType().equals(FoldInstruction.Type.ROW)) {
            dotsToRemove = dots.stream().filter(c -> c.row() == foldValue).collect(Collectors.toSet());
            dotsToFold = dots.stream().filter(c -> c.row() > foldValue).collect(Collectors.toSet());
            newDots = dotsToFold.stream().map(coordinate -> {
                final int row = coordinate.row();
                final int delta = row - foldValue;
                return new Coordinate(row - 2 * delta, coordinate.column());
            }).collect(Collectors.toSet());
        } else {
            dotsToRemove = dots.stream().filter(c -> c.column() == foldValue).collect(Collectors.toSet());
            dotsToFold = dots.stream().filter(c -> c.column() > foldValue).collect(Collectors.toSet());
            newDots = dotsToFold.stream().map(coordinate -> {
                final int column = coordinate.column();
                final int delta = column - foldValue;
                return new Coordinate(coordinate.row(), column - 2 * delta);
            }).collect(Collectors.toSet());
        }
        dots.removeAll(dotsToRemove);
        dots.removeAll(dotsToFold);
        dots.addAll(newDots);
    }

    private void printCode() {
        final int maxRow = dots.stream().map(Coordinate::row).max(Integer::compareTo).orElse(0);
        final int maxColumn = dots.stream().map(Coordinate::column).max(Integer::compareTo).orElse(0);

        final StringBuilder codeBuilder = new StringBuilder();

        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxColumn; col++) {
                codeBuilder.append(getSymbolToPrint(row, col));
            }
            codeBuilder.append("\n");
        }

        System.out.println(codeBuilder);
    }

    private String getSymbolToPrint(final int row, final int column) {
        final boolean isDot = dots.stream().anyMatch(c -> c.row() == row && c.column() == column);
        return isDot ? " # " : "   ";
    }
}
