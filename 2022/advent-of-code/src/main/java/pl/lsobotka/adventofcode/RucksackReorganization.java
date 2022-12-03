package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/3
 * */
public class RucksackReorganization {

    private final List<String> input;

    RucksackReorganization(List<String> input) {
        this.input = input;
    }

    long sumPriorityOfCommonItems() {
        final List<Rucksack> rucksacks = input.stream().map(Rucksack::of).collect(Collectors.toList());
        return rucksacks.stream().mapToInt(Rucksack::getCommonPriority).sum();
    }
}

class Rucksack {
    private final List<String> compartments;
    private final char common;

    private Rucksack(final List<String> compartments, final char common) {
        this.compartments = compartments;
        this.common = common;
    }

    static Rucksack of(final String content) {
        final int mid = content.length() / 2;
        final List<String> compartments = List.of(content.substring(0, mid), content.substring(mid));

        final List<List<String>> compartmentsByType = compartments.stream()
                .map(c -> Arrays.asList(c.split("")))
                .collect(Collectors.toList());
        final char common = findCommon(compartmentsByType.get(0), compartmentsByType.get(1));

        return new Rucksack(compartments, common);
    }

    private static char findCommon(List<String> firstCompartment, List<String> secondCompartment) {
        for (String s : firstCompartment) {
            if (secondCompartment.contains(s)) {
                return s.charAt(0);
            }
        }
        return 0;
    }

    public char getCommon() {
        return common;
    }

    public int getCommonPriority() {
        final int offset = Character.isUpperCase(common) ? 38 : 96;
        return common - offset;
    }
}
