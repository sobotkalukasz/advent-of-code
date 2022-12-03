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
        final List<Rucksack> rucksacks = input.stream().map(Rucksack::of).toList();
        return rucksacks.stream().mapToInt(Rucksack::getCommonPriority).sum();
    }

    long sumPriorityOfCommonItemsInGroupOfRucksacks() {
        final List<GroupOfRucksacks> groupOfRucksacks = GroupOfRucksacks.of(input);
        return groupOfRucksacks.stream().mapToInt(GroupOfRucksacks::getCommonPriority).sum();
    }

    static class Rucksack {
        private final List<String> compartment;
        private final char commonForCompartments;

        private Rucksack(final List<String> compartment, final char common) {
            this.compartment = compartment;
            this.commonForCompartments = common;
        }

        static Rucksack of(final String content) {
            final int mid = content.length() / 2;
            final List<String> compartments = List.of(content.substring(0, mid), content.substring(mid));

            final List<List<String>> compartmentsByType = compartments.stream()
                    .map(c -> Arrays.asList(c.split("")))
                    .toList();
            final char common = findCommon(compartmentsByType.get(0), compartmentsByType.get(1));
            final List<String> compartment = compartmentsByType.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return new Rucksack(compartment, common);
        }

        private static char findCommon(List<String> firstCompartment, List<String> secondCompartment) {
            for (String s : firstCompartment) {
                if (secondCompartment.contains(s)) {
                    return s.charAt(0);
                }
            }
            return 0;
        }

        int getCommonPriority() {
            final int offset = Character.isUpperCase(commonForCompartments) ? 38 : 96;
            return commonForCompartments - offset;
        }

        List<String> getCompartment() {
            return compartment;
        }

        boolean containsItem(final String item) {
            return compartment.stream().anyMatch(s -> s.equals(item));
        }

    }

    static class GroupOfRucksacks {
        final List<Rucksack> rucksacks;
        private final char common;

        GroupOfRucksacks(final List<String> input) {
            this.rucksacks = input.stream().map(Rucksack::of).collect(Collectors.toList());
            this.common = findCommon(rucksacks);
        }

        private char findCommon(List<Rucksack> rucksacks) {
            final List<String> compartment = rucksacks.get(0).getCompartment();
            final String common = compartment.stream()
                    .filter(c -> rucksacks.stream().allMatch(r -> r.containsItem(c)))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Don't find common item for rucksacks"));

            return common.charAt(0);
        }

        int getCommonPriority() {
            final int offset = Character.isUpperCase(common) ? 38 : 96;
            return common - offset;
        }

        static List<GroupOfRucksacks> of(final List<String> rawRucksacks) {
            final List<GroupOfRucksacks> rucksacks = new ArrayList<>();
            for (int i = 0; i + 3 <= rawRucksacks.size(); i += 3) {
                rucksacks.add(new GroupOfRucksacks(rawRucksacks.subList(i, i + 3)));
            }
            return rucksacks;
        }

    }
}
