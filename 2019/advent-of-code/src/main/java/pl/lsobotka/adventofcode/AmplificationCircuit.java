package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/7
 * */
public class AmplificationCircuit {

    int[] program;

    AmplificationCircuit(int[] program) {
        this.program = program;
    }

    public int calculateMaxSignal(int[] inputRange) {
        List<Deque<Integer>> permutations = getPermutations(inputRange);
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

    public int calculateMaxSignalLoop(int[] inputRange) {
        List<Deque<Integer>> permutations = getPermutations(inputRange);
        return permutations.stream().map(this::calculateSignalForRangeLoop).max(Integer::compareTo).orElse(0);
    }

    private int calculateSignalForRangeLoop(Deque<Integer> input) {
        int intCodeSize = input.size();
        int output = 0;
        IntCode[] intCodes = new IntCode[intCodeSize];

        while (!isAnyHalted(intCodes)) {
            for (int i = 0; i < intCodeSize; i++) {
                if (Objects.isNull(intCodes[i]))
                    intCodes[i] = new IntCode(getCopy(program), input.removeFirst(), output);
                else
                    intCodes[i].addInput(output);
                List<Integer> execute = intCodes[i].executeUntilOutput();
                output = Math.max(output, execute.stream().max(Integer::compareTo).orElse(0));
            }
        }
        return output;
    }

    private boolean isAnyHalted(IntCode[] intCodes) {
        return Stream.of(intCodes).anyMatch(p -> Objects.nonNull(p) && p.isHalted());
    }

    private List<Deque<Integer>> getPermutations(int[] elements) {
        int[] tempArray = getCopy(elements);
        List<Deque<Integer>> permutations = new ArrayList<>();

        int[] indexes = new int[tempArray.length];
        Arrays.fill(indexes, 0);
        permutations.add(toLinkedList(tempArray));

        int i = 0;
        while (i < tempArray.length) {
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

    private int[] getCopy(int[] arr) {
        int[] tempArray = new int[arr.length];
        System.arraycopy(arr, 0, tempArray, 0, arr.length);
        return tempArray;
    }

}
