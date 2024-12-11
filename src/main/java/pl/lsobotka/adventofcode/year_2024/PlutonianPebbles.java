package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;

public class PlutonianPebbles {

    long blink(final List<Integer> values, final int blinkTimes) {
        List<Long> stones = values.stream().mapToLong(Integer::longValue).boxed().toList();
        int times = blinkTimes;

        while (times-- > 0) {
            stones = singleBlink(stones);
        }

        return stones.size();
    }

    private List<Long> singleBlink(final List<Long> stones) {
        final List<Long> values = new ArrayList<>();
        for (Long stone : stones) {
            if (stone == 0) {
                values.add(1L);
            } else {
                final String stoneString = Long.toString(stone);
                if (stoneString.length() % 2 == 0) {
                    final int middle = stoneString.length() / 2;
                    values.add(Long.parseLong(stoneString.substring(0, middle)));
                    values.add(Long.parseLong(stoneString.substring(middle)));
                } else {
                    values.add(stone * 2024);
                }
            }
        }

        return values;
    }

}
