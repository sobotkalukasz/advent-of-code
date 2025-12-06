package pl.lsobotka.adventofcode.year_2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrashCompactor {

    private static final Pattern DIGIT_PATTERN = Pattern.compile("-?\\d+");

    private final List<String> input;

    public TrashCompactor(final List<String> input) {
        this.input = input;
    }

    long grandTotal() {
        final List<Equation> equations = Equation.parseForGrandTotal(input);
        return equations.stream().mapToLong(Equation::evaluate).sum();
    }

    long correctTotal() {
        final List<Equation> equations = Equation.parseForCorrectTotal(input);
        return equations.stream().mapToLong(Equation::evaluate).sum();
    }

    record Equation(List<Long> numbers, Agg agg) {

        long evaluate() {
            return numbers.stream().mapToLong(Long::longValue).reduce(agg.identity, agg.op);
        }

        static List<Equation> parseForGrandTotal(final List<String> input) {
            final List<List<Long>> numbers = new ArrayList<>();
            final List<Character> operators = new ArrayList<>();

            for (String line : input) {
                final List<Long> row = extractNumbers(line);
                if (row.isEmpty()) {
                    operators.addAll(extractOperators(line));
                } else {
                    numbers.add(row);
                }
            }

            return from(transpose(numbers), operators);
        }

        private static List<Long> extractNumbers(final String line) {
            Matcher matcher = DIGIT_PATTERN.matcher(line);
            List<Long> row = new ArrayList<>();
            while (matcher.find()) {
                row.add(Long.parseLong(matcher.group()));
            }
            return row;
        }

        private static List<Character> extractOperators(final String line) {
            return line.chars().filter(c -> !Character.isWhitespace(c)).mapToObj(c -> (char) c).toList();
        }

        private static List<List<Long>> transpose(final List<List<Long>> numbers) {
            final List<List<Long>> ordered = new ArrayList<>();
            for (int idx = 0; idx < numbers.getFirst().size(); idx++) {
                final List<Long> actual = new ArrayList<>();
                for (List<Long> number : numbers) {
                    actual.add(number.get(idx));
                }
                ordered.add(actual);
            }
            return ordered;
        }

        static List<Equation> parseForCorrectTotal(final List<String> input) {
            final List<List<Long>> ordered = new ArrayList<>();
            final List<Character> operators = new ArrayList<>();

            final List<char[]> numbersToParse = input.stream()
                    .filter(s -> DIGIT_PATTERN.matcher(s).find())
                    .map(String::toCharArray)
                    .toList();

            final int length = numbersToParse.stream().map(c -> c.length).max(Comparator.naturalOrder()).orElse(0);
            int end;
            for (int idx = length - 1; idx >= 0; idx = end - 1) {
                end = determineEnd(idx, numbersToParse);
                final List<Long> numbers = new ArrayList<>();
                for (int i = idx; i > end; i--) {
                    numbers.add(columnAsLong(numbersToParse, i));
                }
                ordered.add(numbers);
            }

            input.stream()
                    .filter(s -> !DIGIT_PATTERN.matcher(s).find())
                    .forEach(line -> line.chars()
                            .filter(c -> !Character.isWhitespace(c))
                            .forEach(c -> operators.addFirst((char) c)));

            return from(ordered, operators);
        }

        private static int determineEnd(final int start, final List<char[]> numbersToParse) {
            int end = start;
            for (; end >= 0; end--) {
                int finalEnd = end;
                final boolean whiteSpace = numbersToParse.stream()
                        .map(l -> getByIndex(l, finalEnd))
                        .allMatch(Character::isWhitespace);
                if (whiteSpace) {
                    break;
                }
            }
            return end;
        }

        private static Long columnAsLong(final List<char[]> rows, final int col) {
            StringBuilder sb = new StringBuilder(rows.size());
            for (char[] r : rows) {
                sb.append(getByIndex(r, col));
            }
            return Long.parseLong(sb.toString().trim());
        }

        private static char getByIndex(final char[] chars, final int idx) {
            if (chars.length <= idx) {
                return ' ';
            } else {
                return chars[idx];
            }
        }

        private static List<Equation> from(List<List<Long>> numbers, List<Character> operators) {
            final List<Equation> equations = new ArrayList<>();
            for (int idx = 0; idx < numbers.size(); idx++) {
                final Character c = operators.get(idx);

                if (c == '+') {
                    equations.add(new Equation(numbers.get(idx), Agg.SUM));
                } else if (c == '*') {
                    equations.add(new Equation(numbers.get(idx), Agg.MULTIPLY));
                } else {
                    throw new IllegalArgumentException("Unknown operator: " + c);
                }
            }
            return equations;
        }
    }

    enum Agg {
        SUM('+', 0L, Long::sum), //
        MULTIPLY('*', 1L, (a, b) -> a * b);

        final char symbol;
        final long identity;
        final LongBinaryOperator op;

        Agg(char symbol, long identity, LongBinaryOperator op) {
            this.symbol = symbol;
            this.identity = identity;
            this.op = op;
        }
    }
}
