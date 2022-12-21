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

    private static final String ROOT = "root";
    private static final String HUMAN = "humn";

    final Map<String, Monkey> monkeys;

    public MonkeyMath(final List<String> input) {
        this.monkeys = initMonkeys(input);
    }

    long yellNumber() {
        return monkeys.get(ROOT).yellNumber(monkeys::get);
    }

    long guessYellNumber() {
        final AtomicLong number = new AtomicLong(0);
        monkeys.put(HUMAN, new Monkey(HUMAN, monkeyProvider -> number.get()));

        return NumberSeeker.from(number, monkeys.get(ROOT).yell, monkeys::get).seek();
    }

    private record NumberSeeker(Monkey monkey, Function<String, Monkey> provider, boolean increase, long constValue,
                                AtomicLong number) {
        static NumberSeeker from(final AtomicLong number, final Yell yell, final Function<String, Monkey> provider) {

            if (yell instanceof Complex y) {
                number.set(1);
                final long firstA = provider.apply(y.firstMonkey).yellNumber(provider);
                final long second = provider.apply(y.secondMonkey).yellNumber(provider);

                number.set(100);
                final long firstB = provider.apply(y.firstMonkey).yellNumber(provider);

                final boolean isFirst = firstA != firstB;
                final String monkey = isFirst ? y.firstMonkey : y.secondMonkey;
                final boolean increase = isFirst ? firstB < second : second < firstB;
                final long constVal = isFirst ? second : firstB;

                return new NumberSeeker(provider.apply(monkey), provider, increase, constVal, number);
            } else {
                throw new IllegalArgumentException("Not supported Yell type");
            }
        }

        long seek() {
            long range = 100_000_000_000_0L;
            number.set(0);

            while (range != 1) {
                range /= 10;
                boolean searchingOptimalRange = true;
                while (searchingOptimalRange) {
                    number.addAndGet(range);
                    final long next = next();
                    if (increase) {
                        searchingOptimalRange = next < constValue;
                    } else {
                        searchingOptimalRange = next > constValue;
                    }
                }
                number.set(number.get() - range);
            }
            return number.incrementAndGet();
        }

        private long next() {
            return monkey.yellNumber(provider);
        }
    }

    private record Monkey(String name, Yell yell) {
        long yellNumber(final Function<String, Monkey> monkeyProvider) {
            return yell.yellNumber(monkeyProvider);
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

    private record Complex(String firstMonkey, String secondMonkey, char operator) implements Yell {

        @Override
        public long yellNumber(final Function<String, Monkey> monkeyProvider) {
            final long first = monkeyProvider.apply(firstMonkey).yellNumber(monkeyProvider);
            final long second = monkeyProvider.apply(secondMonkey).yellNumber(monkeyProvider);

            return switch (operator) {
                case '+' -> first + second;
                case '-' -> first - second;
                case '/' -> first / second;
                case '*' -> first * second;
                default -> throw new IllegalStateException("Unexpected value: " + operator);
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
