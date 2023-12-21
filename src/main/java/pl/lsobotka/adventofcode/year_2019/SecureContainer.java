package pl.lsobotka.adventofcode.year_2019;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2019/day/4
 * */
public class SecureContainer {

    private final Predicate<String> lengthPredicate = val -> val.length() == 6;

    private final Predicate<String> adjacentPredicate = pass -> {
        char previous = pass.charAt(0);
        for (int i = 1; i < pass.length(); i++) {
            if (pass.charAt(i) == previous) return true;
            previous = pass.charAt(i);
        }
        return false;
    };

    private final Predicate<String> neverDecreasePredicate = pass -> {
        List<Integer> ints = pass.chars().boxed().collect(Collectors.toList());
        int max = 0;
        for (Integer anInt : ints) {
            if (anInt < max) return false;
            if (anInt > max) max = anInt;
        }
        return true;
    };

    private final Predicate<String> extendedAdjacentPredicate = pass -> {
        Map<Integer, Long> collect = pass.chars().boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect.containsValue(2L);
    };

    public long countPasswords(int start, int end) {
        return IntStream.rangeClosed(start, end).mapToObj(String::valueOf)
                .filter(lengthPredicate.and(adjacentPredicate).and(neverDecreasePredicate)).count();
    }

    public long countPasswordsExtendedRule(int start, int end) {
        return IntStream.rangeClosed(start, end).mapToObj(String::valueOf)
                .filter(lengthPredicate.and(adjacentPredicate).and(neverDecreasePredicate).and(extendedAdjacentPredicate)).count();
    }

}
