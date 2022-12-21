package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/*
 * https://adventofcode.com/2022/day/21
 * */
public class MonkeyMath {

    final Map<String, Monkey> monkeys;

    public MonkeyMath(final List<String> input) {
        this.monkeys = initMonkeys(input);
    }

    long yellNumber() {
        return monkeys.get("root").yellNumber(monkeys::get);
    }

    long guessYellNumber() {
        final AtomicLong number = new AtomicLong(0);

        final Monkey root = monkeys.get("root");
        final Monkey human = new Monkey("humn", monkeyProvider -> number.get());

        final Function<String, Monkey> provider = name -> name.equals(human.name) ? human : monkeys.get(name);

        if (root.number instanceof Complex c) {
            final Info info = Info.from(number, provider, monkeys.get(c.firstMonkey), monkeys.get(c.secondMonkey));

            number.set(determineCloseNumber(number, info));
            while (info.constValue() != info.next()) {
                number.incrementAndGet();
            }
        }

        return number.longValue();
    }

    private long determineCloseNumber(final AtomicLong number, final Info info) {
        long offset = 100_000_000_000L;
        number.set(0);

        while (offset != 100) {
            boolean searchingOptimalRange = true;
            while (searchingOptimalRange) {
                number.addAndGet(offset);
                final long newNumber = info.next();
                if (info.increase) {
                    searchingOptimalRange = newNumber < info.constValue;
                } else {
                    searchingOptimalRange = newNumber > info.constValue;
                }
            }

            number.set(number.get() - offset);
            offset /= 10;
        }

        return number.get();
    }

    private record Info(Function<String, Monkey> provider, Monkey monkey, boolean increase, long constValue) {
        static Info from(final AtomicLong tmp, final Function<String, Monkey> provider, final Monkey firstMonkey,
                final Monkey secondMonkey) {

            tmp.set(1);
            final long firstA = firstMonkey.yellNumber(provider);
            final long second = secondMonkey.yellNumber(provider);

            tmp.set(100);
            final long firstB = firstMonkey.yellNumber(provider);

            final boolean isFirst = firstA != firstB;
            final Monkey monkey = isFirst ? firstMonkey : secondMonkey;
            final boolean increase = isFirst ? firstB < second : second < firstB;
            final long constVal = isFirst ? second : firstB;

            return new Info(provider, monkey, increase, constVal);
        }

        long next() {
            return monkey.yellNumber(provider);
        }
    }

    private record Monkey(String name, Yell number) {
        long yellNumber(final Function<String, Monkey> monkeyProvider) {
            return number.yellNumber(monkeyProvider);
        }
    }

    private interface Yell {
        long yellNumber(final Function<String, Monkey> monkeyProvider);
    }

    private record Simple(long number) implements Yell {

        @Override
        public long yellNumber(final Function<String, Monkey> monkeyProvider) {
            return number;
        }
    }

    private record Complex(String firstMonkey, String secondMonkey, char operation) implements Yell {

        @Override
        public long yellNumber(final Function<String, Monkey> monkeyProvider) {
            final long first = monkeyProvider.apply(firstMonkey).yellNumber(monkeyProvider);
            final long second = monkeyProvider.apply(secondMonkey).yellNumber(monkeyProvider);

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
