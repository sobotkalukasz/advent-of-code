package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2022/day/20
 * */
public class GrovePositioningSystem {

    final Map<Integer, Integer> orderKeyToValue;

    GrovePositioningSystem(final List<Integer> input) {
        this.orderKeyToValue = init(input);
    }

    long sumOfCoordinates() {
        return mix(1, 1);
    }

    long sumOfCoordinatesWithDecryptionKey() {
        return mix(811589153, 10);
    }

    private long mix(final long key, int times) {
        final int size = orderKeyToValue.size();
        final List<Integer> keyOrder = getInitialKeyOrder(size);

        while (times-- > 0) {
            for (int i = 0; i < size; i++) {
                final long value = orderKeyToValue.get(i) * key;

                final int oldIndex = keyOrder.indexOf(i);
                keyOrder.remove(oldIndex);

                final int newIndex = (int) determineNewIndex(size, oldIndex, value);
                keyOrder.add(newIndex, i);
            }
        }

        final int startIndex = keyOrder.indexOf(getZeroId());
        return IntStream.rangeClosed(1, 3)
                .map(i -> getIdIndexOfElement(1000 * i, startIndex, size))
                .mapToLong(i -> orderKeyToValue.get(keyOrder.get(i)) * key)
                .sum();
    }

    private long determineNewIndex(long size, long oldIndex, long offset) {
        return Math.floorMod(oldIndex + offset, size - 1);
    }

    private Integer getZeroId() {
        return orderKeyToValue.entrySet()
                .stream()
                .filter(e -> e.getValue() == 0)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private int getIdIndexOfElement(int offset, int startIndex, int size) {
        return (offset + startIndex) % size;
    }

    private Map<Integer, Integer> init(final List<Integer> input) {
        final Map<Integer, Integer> numbers = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            numbers.put(i, input.get(i));
        }
        return numbers;
    }

    private List<Integer> getInitialKeyOrder(int size) {
        final List<Integer> keyOrder = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            keyOrder.add(i);
        }
        return keyOrder;
    }

}
