package pl.lsobotka.adventofcode.year_2025;

import java.util.List;

/*
 * https://adventofcode.com/2025/day/3
 * */
public class Lobby {

    private final List<BatteryBank> banks;

    public Lobby(final List<String> input) {
        this.banks = BatteryBank.from(input);
    }

    public long sumOutput(final int digits) {
        return banks.stream().parallel().map(b -> b.largest(digits)).reduce(Long::sum).orElse(0L);
    }

    record BatteryBank(int[] batteries) {

        long largest(final int digits) {
            long result = 0;
            int start = 0;
            int length = batteries.length;

            for (int pos = 0; pos < digits; pos++) {
                int end = length - (digits - pos);
                int bestDigit = -1;
                int bestIdx = -1;

                for (int i = start; i <= end; i++) {
                    int actualDigit = batteries[i];
                    if (actualDigit > bestDigit) {
                        bestDigit = actualDigit;
                        bestIdx = i;
                        if (bestDigit == 9) {
                            break;
                        }
                    }
                }

                result = result * 10 + bestDigit;
                start = bestIdx + 1;
            }

            return result;
        }

        private static List<BatteryBank> from(final List<String> input) {
            return input.stream().map(BatteryBank::from).toList();
        }

        private static BatteryBank from(final String input) {
            return new BatteryBank(input.chars().map(Character::getNumericValue).toArray());
        }
    }
}
