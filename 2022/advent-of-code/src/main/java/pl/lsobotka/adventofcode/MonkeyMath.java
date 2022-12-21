package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2022/day/21
 * */
public class MonkeyMath {

    final Map<String, Monkey> monkeys;

    public MonkeyMath(final List<String> input) {
        this.monkeys = initMonkeys(input);
    }

    long yellNumber() {
        final String root = "root";
        return monkeys.get(root).yellNumber(monkeys);
    }

    private record Monkey(String name, Yell number) {
        long yellNumber(final Map<String, Monkey> monkeys) {
            return number.yellNumber(monkeys);
        }
    }

    private interface Yell {
        long yellNumber(final Map<String, Monkey> monkeys);
    }

    private record Simple(long number) implements Yell {

        @Override
        public long yellNumber(final Map<String, Monkey> monkeys) {
            return number;
        }
    }

    private record Complex(String firstMonkey, String secondMonkey, char operation) implements Yell {

        @Override
        public long yellNumber(final Map<String, Monkey> monkeys) {
            final long first = monkeys.get(firstMonkey).yellNumber(monkeys);
            final long second = monkeys.get(secondMonkey).yellNumber(monkeys);

            return switch (operation) {
                case '+' -> first + second;
                case '-' -> first - second;
                case '/' -> first / second;
                case '*' -> first * second;
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            };
        }
    }

    private Map<String, Monkey> initMonkeys(final List<String> input) {
        final Map<String, Monkey> monkeys = new HashMap<>();

        for (String row : input) {
            final String[] splitMonkey = row.split(":");

            final String monkeyName = splitMonkey[0].trim();
            final Yell yell;

            final boolean isDigit = splitMonkey[1].replaceAll(" ", "").chars().allMatch(Character::isDigit);
            if (isDigit) {
                yell = new Simple(Long.parseLong(splitMonkey[1].replaceAll(" ", "")));
            } else {
                final String[] complexSplit = splitMonkey[1].strip().split(" ");
                final String firstMonkey = complexSplit[0];
                final String secondMonkey = complexSplit[2];
                final char operation = complexSplit[1].charAt(0);
                yell = new Complex(firstMonkey, secondMonkey, operation);
            }

            monkeys.put(monkeyName, new Monkey(monkeyName, yell));

        }
        return monkeys;
    }
}
