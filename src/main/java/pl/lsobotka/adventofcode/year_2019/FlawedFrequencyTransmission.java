package pl.lsobotka.adventofcode.year_2019;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2019/day/16
 * */
public class FlawedFrequencyTransmission {

    private final int[] basePattern;
    private final int[] startingInput;
    private final int[][] pattern;

    public FlawedFrequencyTransmission(final int[] basePattern, final String startingInput) {
        this.basePattern = basePattern;
        this.startingInput = Arrays.stream(startingInput.split("")).mapToInt(Integer::parseInt).toArray();
        this.pattern = initPattern();
    }

    public int[][] initPattern() {
        final int patternLength = this.startingInput.length;
        final int[][] pattern = new int[patternLength][];

        for (int index = 0; index < patternLength; index++) {
            final int repeat = index + 1;
            StringBuilder temp = new StringBuilder();

            while (temp.length() < (patternLength * 2 + patternLength)) {
                for (int patternValue : basePattern) {
                    final String value = String.valueOf(patternValue).concat(",");
                    temp.append(value.repeat(repeat));
                }
            }
            final int fromIndex = 1;
            final int[] patternByIndex = Arrays.stream(temp.toString().split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList())
                    .subList(fromIndex, fromIndex + patternLength)
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray();

            pattern[index] = patternByIndex;
        }

        return pattern;
    }

    public String getOutputAfter(final int phases) {

        int[] output = Arrays.copyOf(startingInput, startingInput.length);
        for (int phase = 0; phase < phases; phase++) {
            output = applyPhase(output);
        }

        return Arrays.stream(output).mapToObj(String::valueOf).limit(8).collect(Collectors.joining());
    }

    public String getRealOutput() {
        final int offset = Integer.parseInt(
                Arrays.stream(startingInput).limit(7).mapToObj(String::valueOf).collect(Collectors.joining()));
        final String input = Arrays.stream(startingInput)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining())
                .repeat(10000)
                .substring(offset);

        final int[] inputArray = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();

        for (int phase = 0; phase < 100; phase++) {
            for (int i = inputArray.length - 2; i >= 0; i--) {
                inputArray[i] = (inputArray[i + 1] + inputArray[i]) % 10;
            }
        }

        return Arrays.stream(inputArray).limit(8).mapToObj(String::valueOf).collect(Collectors.joining());
    }

    private int[] applyPhase(final int[] input) {
        final int[] result = new int[input.length];

        for (int i = 0; i < input.length; i++) {
            int temp = 0;
            for (int j = 0; j < input.length; j++) {
                temp += input[j] * pattern[i][j];
            }
            temp = Math.abs(temp);
            result[i] = (temp > 9) ? temp % 10 : temp;
        }
        return result;
    }
}
