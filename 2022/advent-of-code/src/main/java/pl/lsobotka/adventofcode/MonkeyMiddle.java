package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonkeyMiddle {

    final TreeMap<Integer, Monkey> monkeys;

    MonkeyMiddle(final List<String> input) {
        this.monkeys = MonkeyParser.ofMonkeys(input)
                .stream()
                .collect(Collectors.toMap(Monkey::getId, Function.identity(), (a, b) -> a, TreeMap::new));
    }

    long getMonkeyBusiness(int rounds) {
        final Function<Long, Long> stressRelief = getStressRelief(rounds);
        final int commonModulo = getCommonModulo(monkeys);

        while (rounds-- > 0) {
            monkeys.forEach((key, monkey) -> {
                final List<Result> results = monkey.performTurn(stressRelief, commonModulo);
                results.forEach(result -> monkeys.get(result.monkeyId).addItem(result.item));
            });
        }

        return monkeys.values()
                .stream()
                .map(m -> m.inspectedItems.get())
                .sorted(Collections.reverseOrder())
                .limit(2)
                .reduce((a, b) -> a * b)
                .orElse(0L);
    }

    private int getCommonModulo(final TreeMap<Integer, Monkey> monkeys) {
        return monkeys.values().stream().mapToInt(Monkey::getDivisor).reduce((a, b) -> a * b).orElse(1);
    }

    private Function<Long, Long> getStressRelief(int rounds) {
        final Function<Long, Long> stressRelief;
        if (rounds == 20) {
            stressRelief = val -> val / 3;
        } else {
            stressRelief = val -> val;
        }
        return stressRelief;
    }

    private static class Monkey {
        private final int id;
        private final List<Long> currentItems;
        private final Function<Long, Long> operation;
        private final int divisor;
        private final Function<Long, Integer> targetMonkey;
        private final AtomicLong inspectedItems;

        private Monkey(final int id, List<Long> items, final Function<Long, Long> operation, int divisor,
                final Function<Long, Integer> targetMonkey) {
            this.id = id;
            this.currentItems = new LinkedList<>(items);
            this.operation = operation;
            this.divisor = divisor;
            this.targetMonkey = targetMonkey;
            this.inspectedItems = new AtomicLong();
        }

        public int getId() {
            return id;
        }

        public int getDivisor() {
            return divisor;
        }

        List<Result> performTurn(final Function<Long, Long> stressRelief, final int modulo) {
            final List<Result> results = currentItems.stream().map(item -> {
                final long worryLevel = operation.apply(item % modulo);
                final long afterRelief = stressRelief.apply(worryLevel);
                final int goesTo = targetMonkey.apply(afterRelief);
                this.inspectedItems.incrementAndGet();
                return new Result(goesTo, afterRelief);
            }).collect(Collectors.toList());
            currentItems.clear();
            return results;
        }

        void addItem(final long item) {
            currentItems.add(item);
        }
    }

    private static class MonkeyParser {
        static List<Monkey> ofMonkeys(final List<String> input) {
            final List<Monkey> monkeys = new ArrayList<>();
            final Iterator<String> iter = input.iterator();

            int start = 0;
            int end = 0;

            while (iter.hasNext()) {
                final String row = iter.next();
                if (row.isEmpty()) {
                    monkeys.add(MonkeyParser.of(input.subList(start, end)));
                    start = end + 1;
                } else if (!iter.hasNext()) {
                    monkeys.add(MonkeyParser.of(input.subList(start, end + 1)));
                }
                end++;
            }
            return monkeys;
        }

        static Monkey of(final List<String> rawMonkey) {
            final int id = Integer.parseInt(rawMonkey.get(0).split(" ")[1].replace(":", ""));

            final String[] splitItems = rawMonkey.get(1)
                    .replaceAll("Starting items:", " ")
                    .replaceAll(" ", "")
                    .split(",");
            final List<Long> items = Stream.of(splitItems).map(Long::valueOf).collect(Collectors.toList());

            final Function<Long, Long> operation = parseOperation(rawMonkey);

            final int divisor = Integer.parseInt(
                    rawMonkey.get(3).replaceAll("Test: divisible by", "").replaceAll(" ", ""));

            final Function<Long, Integer> targetMonkeyProvider = parseTargetMonkeyProvider(rawMonkey, divisor);

            return new Monkey(id, items, operation, divisor, targetMonkeyProvider);
        }

        private static Function<Long, Long> parseOperation(List<String> rawMonkey) {
            final Function<Long, Long> operation;
            final String[] splitOperation = rawMonkey.get(2).replaceAll("Operation: new =", "").strip().split(" ");
            final boolean isAdd = splitOperation[1].equals("+");
            final boolean isDigit = Character.isDigit(splitOperation[2].charAt(0));
            if (isAdd) {
                if (isDigit) {
                    final long by = Long.parseLong(splitOperation[2]);
                    operation = val -> val + by;
                } else {
                    operation = val -> val + val;
                }
            } else {
                if (isDigit) {
                    final long by = Long.parseLong(splitOperation[2]);
                    operation = val -> val * by;
                } else {
                    operation = val -> val * val;
                }
            }
            return operation;
        }

        private static Function<Long, Integer> parseTargetMonkeyProvider(final List<String> rawMonkey,
                final int divisor) {
            final String[] trueSplit = rawMonkey.get(4).split(" ");
            final int trueMonkey = Integer.parseInt(trueSplit[trueSplit.length - 1]);

            final String[] falseSplit = rawMonkey.get(5).split(" ");
            final int falseMonkey = Integer.parseInt(falseSplit[trueSplit.length - 1]);

            return val -> val % divisor == 0 ? trueMonkey : falseMonkey;
        }
    }

    private record Result(int monkeyId, long item) {
    }

}
