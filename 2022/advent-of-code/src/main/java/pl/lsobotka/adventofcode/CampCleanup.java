package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2022/day/4
 * */
public class CampCleanup {

    private final List<String> input;

    public CampCleanup(List<String> input) {
        this.input = input;
    }

    long countContainOther() {
        return input.stream().map(CleaningPair::of).filter(CleaningPair::containsOther).count();
    }

    long countOverlapOther() {
        return input.stream().map(CleaningPair::of).filter(CleaningPair::overlapsOther).count();
    }

    record CleaningPair(CleaningElf firstElf, CleaningElf secondElf) {

        static CleaningPair of(final String raw) {
            final String[] pairs = raw.split(",");
            return new CleaningPair(CleaningElf.of(pairs[0]), CleaningElf.of(pairs[1]));
        }

        boolean containsOther() {
            return firstElf.contains(secondElf) || secondElf.contains(firstElf);
        }

        boolean overlapsOther() {
            return firstElf.overlaps(secondElf) || secondElf.overlaps(firstElf);
        }
    }

    record CleaningElf(int from, int to) {
        static CleaningElf of(final String raw) {
            final String[] range = raw.split("-");
            return new CleaningElf(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        }

        boolean contains(final CleaningElf other) {
            final List<Integer> collect = IntStream.range(this.from, this.to + 1).boxed().toList();
            return IntStream.range(other.from, other.to + 1).allMatch(collect::contains);
        }

        boolean overlaps(final CleaningElf other) {
            final List<Integer> collect = IntStream.range(this.from, this.to + 1).boxed().toList();
            return IntStream.range(other.from, other.to + 1).anyMatch(collect::contains);
        }
    }
}
