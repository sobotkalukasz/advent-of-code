package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/7
 * */
public class AmplificationCircuit {

    long[] program;

    AmplificationCircuit(long[] program) {
        this.program = program;
    }

    public long calculateMaxSignal(long[] inputRange) {
        List<Deque<Long>> permutations = getPermutations(inputRange);
        return permutations.stream().map(this::calculateSignalForRange).max(Long::compareTo).orElse(0L);
    }

    private long calculateSignalForRange(Deque<Long> input) {
        long output = 0;
        SunnyAsteroids intCode;

        while (!input.isEmpty()) {
            intCode = new SunnyAsteroids(program, input.removeFirst(), output);
            output = intCode.execute().stream().max(Long::compareTo).orElse(0L);
        }
        return output;
    }

    public long calculateMaxSignalLoop(long[] inputRange) {
        List<Deque<Long>> permutations = getPermutations(inputRange);
        return permutations.stream().map(this::calculateSignalForRangeLoop).max(Long::compareTo).orElse(0L);
    }

    private long calculateSignalForRangeLoop(Deque<Long> input) {
        int intCodeSize = input.size();
        long output = 0;
        IntCode[] intCodes = new IntCode[intCodeSize];

        while (!isAnyHalted(intCodes)) {
            for (int i = 0; i < intCodeSize; i++) {
                if (Objects.isNull(intCodes[i]))
                    intCodes[i] = new IntCode(getCopy(program), input.removeFirst(), output);
                else
                    intCodes[i].addInput(output);
                List<Long> execute = intCodes[i].executeUntilOutput();
                output = Math.max(output, execute.stream().max(Long::compareTo).orElse(0L));
            }
        }
        return output;
    }

    private boolean isAnyHalted(IntCode[] intCodes) {
        return Stream.of(intCodes).anyMatch(p -> Objects.nonNull(p) && p.isHalted());
    }

    private List<Deque<Long>> getPermutations(long[] elements) {
        long[] tempArray = getCopy(elements);
        List<Deque<Long>> permutations = new ArrayList<>();

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

    private void swap(long[] input, int a, int b) {
        long tmp = input[a];
        input[a] = input[b];
        input[b] = tmp;
    }

    private Deque<Long> toLinkedList(long[] elements) {
        return Arrays.stream(elements).boxed().collect(Collectors.toCollection(LinkedList::new));
    }

    private long[] getCopy(long[] arr) {
        long[] tempArray = new long[arr.length];
        System.arraycopy(arr, 0, tempArray, 0, arr.length);
        return tempArray;
    }

}
