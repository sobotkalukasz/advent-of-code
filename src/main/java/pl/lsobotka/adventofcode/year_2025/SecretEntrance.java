package pl.lsobotka.adventofcode.year_2025;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.lsobotka.adventofcode.utils.Rotate;

/*
 * https://adventofcode.com/2025/day/1
 * */
public class SecretEntrance {

    private static final int SIZE = 100;
    private static final int START = 50;

    final List<Rotation> rotations;

    public SecretEntrance(final List<String> input) {
        rotations = input.stream().map(Rotation::of).toList();
    }

    public int countStopAt() {
        int count = 0;
        int position = START;

        for (Rotation rotation : rotations) {
            position = position + rotation.signedSteps();
            position = Math.floorMod(position, SIZE);

            if (position == 0) {
                count++;
            }
        }
        return count;
    }

    public int countPointingAt() {
        int count = 0;
        int lastPosition = START;
        int actual;

        for (Rotation rotation : rotations) {
            actual = lastPosition + rotation.signedSteps();

            final int passTimes = Math.abs(actual / SIZE);
            count += passTimes;

            if (actual == 0 || actual * lastPosition < 0) {
                count++;
            }

            lastPosition = Math.floorMod(actual, SIZE);
        }
        return count;
    }

    record Rotation(Rotate rotation, int steps) {

        private static final Pattern PATTERN = Pattern.compile("\\b(?<rotation>[LR])(?<value>\\d+)\\b");

        static Rotation of(final String raw) {
            final Matcher matcher = PATTERN.matcher(raw);

            if (matcher.find()) {
                String side = matcher.group("rotation");
                String valueRaw = matcher.group("value");
                final Rotate rotate = Rotate.L.name().equals(side) ? Rotate.L : Rotate.R;
                return new Rotation(rotate, Integer.parseInt(valueRaw));
            }

            throw new IllegalArgumentException("Unable to parse rotation: " + raw);
        }

        int signedSteps() {
            return rotation == Rotate.L ? -steps : steps;
        }

    }

}
