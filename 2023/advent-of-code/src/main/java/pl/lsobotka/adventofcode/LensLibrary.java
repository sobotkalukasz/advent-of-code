package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2023/day/15
 * */
public class LensLibrary {

    private final List<Lens> lenses;

    public LensLibrary(final String input) {
        this.lenses = Lens.lenses(input);

    }

    long sumOfLensHash() {
        return lenses.stream().map(Lens::hash).reduce(0, Integer::sum);
    }

    long calculateFocussingPower() {
        final Map<Integer, Box> boxes = new HashMap<>();

        lenses.forEach(lens -> {
            switch (lens.operation) {
            case FOCUS -> {
                final Box box = boxes.computeIfAbsent(lens.labelHash(), v -> Box.from(lens.labelHash()));
                box.add(lens);
            }
            case REMOVE -> boxes.values().forEach(b -> b.removeIfContains(lens.label));
            }
        });

        return boxes.values().stream().map(Box::calculateFocusingPower).reduce(0L, Long::sum);
    }

    record Box(int id, Map<String, Integer> lensIndex, List<Integer> length) {

        static Box from(int id) {
            return new Box(id, new HashMap<>(), new ArrayList<>());
        }

        void add(final Lens lens) {
            if (lensIndex.containsKey(lens.label)) {
                final int index = lensIndex.get(lens.label);
                length.remove(index);
                length.add(index, lens.focusLength);
            } else {
                length.add(lens.focusLength);
                lensIndex.put(lens.label, length.size() - 1);
            }
        }

        void removeIfContains(final String label) {
            if (lensIndex.containsKey(label)) {
                final int index = lensIndex.remove(label);
                length.remove(index);
                lensIndex.keySet().forEach(key -> lensIndex.computeIfPresent(key, (k, v) -> v >= index ? --v : v));
            }
        }

        long calculateFocusingPower() {
            int power = 0;
            for (int index = 0; index < length.size(); index++) {
                power += (this.id + 1) * (index + 1) * length.get(index);
            }
            return power;
        }

    }

    record Lens(String rawValue, String label, Operation operation, int focusLength) {

        private final static Pattern lensesPattern = Pattern.compile("([^,]+)");
        private final static Pattern singleLensPattern = Pattern.compile("(\\w+)([=-])(\\d)*");

        static List<Lens> lenses(String input) {
            return lensesPattern.matcher(input).results().map(MatchResult::group).map(Lens::singleLens).toList();
        }

        static Lens singleLens(final String raw) {
            final Matcher lensMatcher = singleLensPattern.matcher(raw);
            if (lensMatcher.matches()) {
                final String label = lensMatcher.group(1);
                final Operation operation = Operation.from(lensMatcher.group(2).charAt(0));
                final Integer focusLength = Optional.ofNullable(lensMatcher.group(3)).map(Integer::parseInt).orElse(0);
                return new Lens(raw, label, operation, focusLength);
            } else {
                throw new IllegalArgumentException("Unknown lens: " + raw);
            }
        }

        int hash() {
            return hash(this.rawValue);
        }

        int labelHash() {
            return hash(label);
        }

        private int hash(final String toHash) {
            int hash = 0;
            for (int index = 0; index < toHash.length(); index++) {
                hash += toHash.charAt(index);
                hash = (hash * 17) % 256;
            }
            return hash;
        }
    }

    enum Operation {
        REMOVE('-'), FOCUS('=');

        private final char sign;

        Operation(char sign) {
            this.sign = sign;
        }

        static Operation from(final char sign) {
            return Arrays.stream(Operation.values())
                    .filter(o -> o.sign == sign)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown sign: " + sign));
        }
    }
}
