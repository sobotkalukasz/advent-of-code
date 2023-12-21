package pl.lsobotka.adventofcode.year_2020;

/*
 * https://adventofcode.com/2020/day/18
 * */
public class OperationOrder {

    public long calculate(String input) {
        return solveIt(input.replaceAll(" ", ""), 0);
    }

    public long calculateWeird(String input) {
        input = input.replaceAll(" ", "");
        input = removeParentheses(input);
        input = calc(input, '+');
        input = calc(input, '*');

        return Long.parseLong(input);
    }

    private long solveIt(String input, long current) {

        int length = input.length();
        StringBuilder first = current != 0 ? new StringBuilder(String.valueOf(current)) : new StringBuilder();
        StringBuilder second = new StringBuilder();
        char op = 0;

        int openIndex = 0;
        int openCount = 0;
        int closeCount = 0;

        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);

            if (openCount == 0 && (Character.isDigit(c) || c == '.')) {
                if (current == 0 && op == 0) first.append(c);
                else second.append(c);
                continue;
            }

            if (openCount == 0 && isOperator(c)) {
                if (op == 0) {
                    op = c;
                    continue;
                } else {
                    long calculate = calculate(op, Long.parseLong(first.toString()), Long.parseLong(second.toString()));
                    current = solveIt(input.substring(i), calculate);
                    op = 0;
                    break;
                }
            }

            if (isOpen(c)) {
                if (openCount == 0) openIndex = i;
                openCount++;
                continue;
            }
            if (isClose(c)) {
                closeCount++;
                if (openCount == closeCount) {
                    String paren = input.substring(openIndex + 1, i);
                    long solveIt = solveIt(paren, 0);
                    String firstPart = input.substring(0, openIndex);
                    String secondPart = input.substring(i + 1);

                    current = solveIt(firstPart + solveIt + secondPart, current);
                    op = 0;
                    break;
                }
            }
        }
        if (op != 0 && !second.isEmpty())
            current = calculate(op, (current == 0 && !first.isEmpty()) ? Long.parseLong(first.toString()) : current, Long.parseLong(second.toString()));
        return current;
    }

    private String removeParentheses(String input) {
        if (!input.contains("(")) {
            return input;
        }
        int length = input.length();

        int openIndex = 0;
        int openCount = 0;
        int closeCount = 0;

        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);

            if (isOpen(c)) {
                if (openCount == 0) openIndex = i;
                openCount++;
                continue;
            }
            if (isClose(c)) {
                closeCount++;
                if (openCount == closeCount) {
                    String paren = input.substring(openIndex + 1, i);
                    if (paren.contains("(")) {
                        paren = removeParentheses(paren);
                    }
                    String calc = calc(calc(paren, '+'), '*');
                    String firstPart = input.substring(0, openIndex);
                    String secondPart = input.substring(i + 1);
                    input = removeParentheses(removeParentheses(firstPart + calc + secondPart));
                    break;

                }
            }
        }
        return input;
    }

    private String calc(String input, char op) {

        int length = input.length();
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        int from = -1;
        int to = -1;

        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == op && input.charAt(i + 1) != '(') {
                for (int j = i - 1; j >= 0; j--) {
                    if (Character.isDigit(input.charAt(j))) {
                        first.append(input.charAt(j));
                        from = j;
                    } else {
                        break;
                    }
                }
                for (int j = i + 1; j < length; j++) {
                    if (Character.isDigit(input.charAt(j))) {
                        second.append(input.charAt(j));
                        to = j;
                    } else {
                        break;
                    }
                }
                String calculate = String.valueOf(calculate(op, Long.parseLong(first.reverse().toString()), Long.parseLong(second.toString())));

                String s1 = input.substring(0, from);
                String s2 = input.substring(to + 1);
                String calculated = s1 + calculate + s2;
                return calc(calculated, op);
            }
        }
        return input;
    }

    private long calculate(char op, long a, long b) {
        if (op == '+') return a + b;
        if (op == '-') return a - b;
        if (op == '*') return a * b;
        if (op == '/') return a / b;
        return 0;
    }

    private boolean isOperator(char test) {
        if (test == '+') return true;
        if (test == '-') return true;
        if (test == '*') return true;
        if (test == '/') return true;
        return false;
    }

    private boolean isOpen(char test) {
        return test == '(';
    }

    private boolean isClose(char test) {
        return test == ')';
    }
}
