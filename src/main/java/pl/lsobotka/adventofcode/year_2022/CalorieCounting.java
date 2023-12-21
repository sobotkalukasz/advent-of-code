package pl.lsobotka.adventofcode.year_2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/1
 * */
public class CalorieCounting {

    private final List<Elf> elves;

    public CalorieCounting(List<String> input) {
        elves = initElves(input.iterator());
    }

    public long getMostCalories() {
        return elves.stream().map(Elf::countCalories).reduce(0L, Long::max);
    }

    public long getTopCalories(final int top) {
        return elves.stream()
                .map(Elf::countCalories)
                .sorted(Collections.reverseOrder())
                .mapToLong(Long::longValue)
                .limit(top)
                .sum();
    }

    private List<Elf> initElves(final Iterator<String> iter) {
        final List<Elf> elves = new ArrayList<>();
        final List<String> tmp = new ArrayList<>();
        while (iter.hasNext()) {
            final String row = iter.next();

            if (!row.isEmpty()) {
                tmp.add(row);
            }

            if ((row.isEmpty() && !tmp.isEmpty()) || !iter.hasNext()) {
                elves.add(Elf.of(tmp));
                tmp.clear();
            }
        }
        return elves;
    }

    record Elf(List<Long> calories) {

        static Elf of(final List<String> calories) {
            return new Elf(calories.stream().map(Long::valueOf).collect(Collectors.toList()));
        }

        long countCalories() {
            return calories.stream().reduce(0L, Long::sum);
        }

    }
}
