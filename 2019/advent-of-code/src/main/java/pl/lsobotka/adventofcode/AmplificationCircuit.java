package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2019/day/7
 * */
public class AmplificationCircuit {

    int[] program;
    int[] inputRange;

    AmplificationCircuit(int[] program, int rangeClosed) {
        this.program = program;
        this.inputRange = IntStream.rangeClosed(0, rangeClosed).toArray();
    }

    public int calculateMaxSignal() {
        List<Deque<Integer>> permutations = getPermutations(inputRange, inputRange.length);
        return permutations.stream().map(this::calculateSignalForRange).max(Integer::compareTo).orElse(0);
    }

    private int calculateSignalForRange(Deque<Integer> input) {
        int output = 0;
        SunnyAsteroids intCode;

        while (!input.isEmpty()) {
            intCode = new SunnyAsteroids(program, input.removeFirst(), output);
            output = intCode.execute().stream().max(Integer::compareTo).orElse(0);
        }
        return output;
    }

    private List<Deque<Integer>> getPermutations(int[] elements, int size) {
        int[] tempArray = new int[size];
        System.arraycopy(elements, 0, tempArray, 0, size);
        List<Deque<Integer>> permutations = new ArrayList<>();

        int[] indexes = new int[size];
        Arrays.fill(indexes, 0);
        permutations.add(toLinkedList(tempArray));

        int i = 0;
        while (i < size) {
            if (indexes[i] < i) {
                swap(tempArray, i % 2 == 0 ? 0 : indexes[i], i);
                permutations.add(toLinkedList(tempArray));
                indexes[i]++;
                i = 0;
            } else {
                indexes[i] = 0;
                i++;
            }
        }

        return permutations;
    }

    private void swap(int[] input, int a, int b) {
        int tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private Deque<Integer> toLinkedList(int[] elements) {
        return Arrays.stream(elements).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

}
