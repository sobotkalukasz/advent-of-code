package pl.lsobotka.adventofcode.year_2021;

import java.util.Arrays;
import java.util.List;

/*
 * https://adventofcode.com/2021/day/6
 * */
public class Lanternfish {

    private static final int NEWBORN = 8;
    private static final int AFTER_BIRTH = 6;
    private static final int BIRTHDAY = 0;

    final long[] fishes = new long[NEWBORN + 1];

    public Lanternfish(List<Integer> fishAge) {
        Arrays.fill(fishes, 0);
        fishAge.forEach(i -> fishes[i]++);
    }

    public long countFishAfterDays(int days) {
        while (days-- > 0) {
            dayGone();
        }
        return Arrays.stream(fishes).reduce(Long::sum).orElse(0);
    }

    private void dayGone() {
        final long newborns = fishes[BIRTHDAY];
        for (int i = 1; i < fishes.length; i++) {
            fishes[i - 1] = fishes[i];
        }
        fishes[AFTER_BIRTH] += newborns;
        fishes[NEWBORN] = newborns;
    }
}
