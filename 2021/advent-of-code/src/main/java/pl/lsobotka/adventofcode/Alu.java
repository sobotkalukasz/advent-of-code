package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import lombok.Getter;

/*
 * https://adventofcode.com/2021/day/24
 * */
public class Alu {

    private final static String INP = "INP";
    private final List<List<String>> rawInstructions;
    private final List<ReverseInstruction> reverseInstructions;

    public Alu(final List<String> rawInstructions) {
        this.rawInstructions = new ArrayList<>();

        List<String> temp = new ArrayList<>();
        final Iterator<String> iter = rawInstructions.iterator();

        while (iter.hasNext()) {
            final String next = iter.next();
            if ((next.toUpperCase().startsWith(INP) && !temp.isEmpty()) || !iter.hasNext()) {
                this.rawInstructions.add(new ArrayList<>(temp));
                temp = new ArrayList<>();
            }
            temp.add(next);
        }
        reverseInstructions = initReverseInstructions(this.rawInstructions);
    }

    private List<ReverseInstruction> initReverseInstructions(List<List<String>> instructions) {
        List<ReverseInstruction> reverseInstructions = new ArrayList<>();
        for (final List<String> part : instructions) {
            boolean isDivisor = false;
            int xOffset = 0;
            int yOffset = 0;
            for (final String singleInstruction : part) {
                if (singleInstruction.contains("add x ")) {
                    final String value = singleInstruction.replace("add x ", "");
                    if (value.matches("-?\\d+")) {
                        xOffset = Integer.parseInt(value);
                    }
                }
                if (singleInstruction.contains("add y ")) {
                    final String value = singleInstruction.replace("add y ", "");
                    if (value.matches("-?\\d+") && Integer.parseInt(value) != 25) {
                        yOffset = Integer.parseInt(value);
                    }
                }
                if (singleInstruction.contains("div z ")) {
                    final String value = singleInstruction.replace("div z ", "");
                    if (value.matches("-?\\d+")) {
                        isDivisor = Integer.parseInt(value) == 26;
                    }
                }
            }
            reverseInstructions.add(new ReverseInstruction(isDivisor, xOffset, yOffset));
        }
        return reverseInstructions;

    }

    public long findBiggestNumber() {
        final List<Integer> found = searchNumber(new LinkedList<>(reverseInstructions), SearchStrategy.BIGGEST);
        final int output = validateValue(found);
        return output == 0 ? Long.parseLong(found.stream().map(String::valueOf).collect(Collectors.joining())) : 0;
    }

    public long findLowestNumber() {
        final List<Integer> found = searchNumber(new LinkedList<>(reverseInstructions), SearchStrategy.LOWEST);
        final int output = validateValue(found);
        return output == 0 ? Long.parseLong(found.stream().map(String::valueOf).collect(Collectors.joining())) : 0;
    }

    private List<Integer> searchNumber(final LinkedList<ReverseInstruction> reverse,
            final SearchStrategy searchStrategy) {
        if (reverse.isEmpty() || reverse.peekFirst().isDivisor) {
            return Collections.emptyList();
        }

        final ReverseInstruction leftSide = reverse.pop();
        final List<Integer> middleNumbers = searchNumber(reverse, searchStrategy);
        final ReverseInstruction rightSide = reverse.pop();

        final int leftOutput = searchStrategy.searchStrategy.apply(leftSide.yOffset, rightSide.xOffset);
        final int rightOutput = leftOutput + leftSide.yOffset + rightSide.xOffset;

        final List<Integer> endNumbers = searchNumber(reverse, searchStrategy);

        final List<Integer> foundNumbers = new ArrayList<>();
        foundNumbers.add(leftOutput);
        foundNumbers.addAll(middleNumbers);
        foundNumbers.add(rightOutput);
        foundNumbers.addAll(endNumbers);
        return foundNumbers;

    }

    private int validateValue(final List<Integer> value) {
        final Result result = new Result();
        for (int i = 0; i < rawInstructions.size(); i++) {
            processInstructions(rawInstructions.get(i), value.get(i), result);
        }
        return result.getZ();
    }

    private void processInstructions(final List<String> instructions, final int value, final Result result) {
        instructions.forEach(instruction -> {
            instruction = instruction.toUpperCase();
            if (instruction.startsWith("INP")) {
                final String insert = instruction.replace(INP, "").replace(" ", "");
                result.storeValue(insert, value);
            } else {
                final Instruction type = Instruction.getInstruction(instruction);
                final String[] values = instruction.replace(type.name(), "").stripLeading().split(" ");
                final int a = result.readValue(values[0]);
                final int b = result.readValue(values[1]);
                final int newValue = type.operation.apply(a, b);
                result.storeValue(values[0], newValue);
            }
        });
    }

    @Getter
    static class Result {
        int w;
        int x;
        int y;
        int z;

        public int readValue(final String read) {
            int value;

            if (read.matches("-?\\d+")) {
                value = Integer.parseInt(read);
            } else if (read.equals("W")) {
                value = this.w;
            } else if (read.equals("X")) {
                value = this.x;
            } else if (read.equals("Y")) {
                value = this.y;
            } else if (read.equals("Z")) {
                value = this.z;
            } else {
                throw new IllegalArgumentException(String.format("Unknown read instruction: %s", read));
            }

            return value;
        }

        private void storeValue(final String insert, final int value) {
            switch (insert) {
            case "W" -> this.w = value;
            case "X" -> this.x = value;
            case "Y" -> this.y = value;
            case "Z" -> this.z = value;
            default -> throw new IllegalArgumentException(String.format("Unknown insert instruction: %s", insert));
            }
        }
    }

    enum Instruction {
        ADD(Integer::sum), MUL((a, b) -> a * b), DIV((a, b) -> a / b), MOD((a, b) -> a % b), EQL(
                (a, b) -> a.equals(b) ? 1 : 0);

        final BiFunction<Integer, Integer, Integer> operation;

        Instruction(BiFunction<Integer, Integer, Integer> operation) {
            this.operation = operation;
        }

        public static Instruction getInstruction(final String rawInstruction) {
            return Arrays.stream(Instruction.values())
                    .filter(i -> rawInstruction.startsWith(i.name()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            String.format("Unknown instruction type: %s", rawInstruction)));
        }
    }

    record ReverseInstruction(boolean isDivisor, int xOffset, int yOffset) {

    }

    enum SearchStrategy {
        BIGGEST((a, b) -> Math.min(9, 9 - a - b)), LOWEST((a, b) -> Math.max(1, 1 - a - b));

        SearchStrategy(BiFunction<Integer, Integer, Integer> searchStrategy) {
            this.searchStrategy = searchStrategy;
        }

        BiFunction<Integer, Integer, Integer> searchStrategy;
    }

}
