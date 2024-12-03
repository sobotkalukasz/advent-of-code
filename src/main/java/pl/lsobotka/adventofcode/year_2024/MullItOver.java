package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2024/day/3
 * */
public class MullItOver {

    List<Instruction> instructions;

    public MullItOver(final List<String> input) {
        instructions = new ArrayList<>();
        for (String line : input) {
            instructions.addAll(Instruction.from(line));
        }
    }

    long resultOfInstructions() {
        return instructions.stream()
                .filter(Multiply.class::isInstance)
                .map(Multiply.class::cast)
                .map(Multiply::outcome)
                .reduce(Integer::sum)
                .orElse(0);
    }

    long resultOfDoDontInstructions() {
        boolean shoudDo = true;
        long value = 0;

        for (Instruction instruction : instructions) {
            if (instruction instanceof Condition c) {
                shoudDo = c.shouldDo();
            } else if (shoudDo && instruction instanceof Multiply m) {
                value += m.outcome();
            }
        }
        return value;
    }

}

interface Instruction {
    Pattern PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");

    static List<Instruction> from(String line) {
        final Matcher matcher = PATTERN.matcher(line);
        final List<Instruction> instructions = new ArrayList<>();
        while (matcher.find()) {
            if (Multiply.PATTERN.matcher(matcher.group(0)).find()) {
                final int left = Integer.parseInt(matcher.group(1));
                final int right = Integer.parseInt(matcher.group(2));
                instructions.add(new Multiply(left, right));
            } else if (Condition.DO_PATTERN.matcher(matcher.group(0)).find()) {
                instructions.add(Condition.parse(matcher.group(0)));
            } else if (Condition.DONT_PATTERN.matcher(matcher.group(0)).find()) {
                instructions.add(Condition.parse(matcher.group(0)));
            }
        }

        return instructions;
    }
}

record Condition(boolean shouldDo) implements Instruction {
    static final Pattern DO_PATTERN = Pattern.compile("do\\(\\)");
    static final Pattern DONT_PATTERN = Pattern.compile("don't\\(\\)");

    static Condition parse(String line) {
        final Matcher matcher = DO_PATTERN.matcher(line);
        return new Condition(matcher.matches());
    }
}

record Multiply(int a, int b) implements Instruction {

    static final Pattern PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

    public int outcome() {
        return a * b;
    }
}
