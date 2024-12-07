package pl.lsobotka.adventofcode.year_2024;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2024/day/7
 * */
public class BridgeRepair {

    private final List<Equation> equations;

    public BridgeRepair(final List<String> input) {
        equations = input.stream().map(Equation::parseEquation).toList();
    }

    BigInteger findSumOfCorrectEquations() {
        final Operator[] operators = { Operator.ADD, Operator.MULTIPLY };
        return equations.stream()
                .filter(e -> e.canBePossible(operators))
                .map(Equation::value)
                .reduce(BigInteger::add)
                .orElse(BigInteger.ZERO);
    }
}

record Equation(BigInteger value, List<BigInteger> params) {
    static Pattern ROW_PATTERN = Pattern.compile("(\\d+):\\s*((?:\\d+\\s*)+)");

    static Equation parseEquation(String line) {
        final Matcher matcher = ROW_PATTERN.matcher(line);

        if (matcher.find()) {
            final BigInteger value = new BigInteger(matcher.group(1));
            final List<BigInteger> params = Stream.of(matcher.group(2).trim().split("\\s"))
                    .map(BigInteger::new)
                    .toList();
            return new Equation(value, params);

        } else {
            throw new RuntimeException("Unable to parse equation: " + line);
        }
    }

    boolean canBePossible(final Operator[] operators) {
        final List<List<Operator>> permutations = Operator.generatePermutations(operators, params.size() - 1);
        for (List<Operator> permutation : permutations) {
            if (validate(permutation)) {
                return true;
            }
        }
        return false;
    }

    private boolean validate(final List<Operator> operators) {
        BigInteger sum = params.getFirst();
        for (int i = 1; i < params.size(); i++) {
            final Operator operator = operators.get(i - 1);
            sum = switch (operator) {
                case ADD -> sum.add(params.get(i));
                case SUBTRACT -> sum.subtract(params.get(i));
                case MULTIPLY -> sum.multiply(params.get(i));
                case DIVIDE -> sum.divide(params.get(i));
            };
        }
        return sum.compareTo(value) == 0;
    }

}

enum Operator {
    ADD, SUBTRACT, MULTIPLY, DIVIDE;

    static List<List<Operator>> generatePermutations(final Operator[] operators, final int size) {
        final List<List<Operator>> result = new ArrayList<>();
        generate(new ArrayList<>(), operators, size, result);
        return result;

    }

    private static void generate(final List<Operator> current, final Operator[] operators, int size,
            final List<List<Operator>> result) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (Operator op : operators) {
            current.add(op);
            generate(current, operators, size, result);
            current.removeLast();
        }
    }
}
