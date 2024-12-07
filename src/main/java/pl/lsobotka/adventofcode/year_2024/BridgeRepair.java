package pl.lsobotka.adventofcode.year_2024;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2024/day/7
 * */
public class BridgeRepair {

    private final List<Equation> equations;
    private final Map<Integer, List<List<Operator>>> permutationCache;

    public BridgeRepair(final List<String> input) {
        equations = input.stream().map(Equation::parseEquation).toList();
        permutationCache = new ConcurrentHashMap<>();
    }

    BigInteger findSumOfCorrectEquations() {
        return calculate(Operator.ADD, Operator.MULTIPLY);
    }

    BigInteger findSumOfCorrectEquationsWithConcatenation() {
        return calculate(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT);
    }

    private BigInteger calculate(final Operator... operators) {
        permutationCache.clear();
        return equations.stream()
                .parallel()
                .filter(e -> e.canBePossible(operators, permutationCache))
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

    boolean canBePossible(final Operator[] operators, final Map<Integer, List<List<Operator>>> permutationCache) {
        final List<List<Operator>> permutations = permutationCache.computeIfAbsent(params.size() - 1,
                v -> Operator.generatePermutations(operators, params.size() - 1));
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
                case MULTIPLY -> sum.multiply(params.get(i));
                case CONCAT -> new BigInteger(sum.toString() + params.get(i).toString());
            };
        }
        return sum.compareTo(value) == 0;
    }

}

enum Operator {
    ADD, MULTIPLY, CONCAT;

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
