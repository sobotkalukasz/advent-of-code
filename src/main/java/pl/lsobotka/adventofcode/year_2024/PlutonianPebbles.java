package pl.lsobotka.adventofcode.year_2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlutonianPebbles {

    long blink(final List<Integer> values, final int times) {

        Map<Long, Long> stonesCount = values.stream()
                .mapToLong(Integer::longValue)
                .boxed()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int i = 0; i < times; i++) {
            stonesCount = singleBlink(stonesCount);
        }

        return stonesCount.values().stream().mapToLong(Long::longValue).sum();
    }

    private Map<Long, Long> singleBlink(final Map<Long, Long> stonesBefore) {
        Map<Long, Long> stones = new HashMap<>();

        for (var entry : stonesBefore.entrySet()) {
            long stone = entry.getKey();
            long count = entry.getValue();

            if (stone == 0) {
                stones.merge(1L, count, Long::sum);
            } else {
                int length = (int) Math.log10(stone) + 1;
                if (length % 2 == 0) {
                    int halfLength = length / 2;
                    long divisor = (long) Math.pow(10, halfLength);
                    stones.merge(stone / divisor, count, Long::sum);
                    stones.merge(stone % divisor, count, Long::sum);
                } else {
                    stones.merge(stone * 2024, count, Long::sum);
                }
            }
        }

        return stones;
    }

}
