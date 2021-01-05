package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/5
 * */
public class SunnyAsteroids {

    private static final int STOP = 99;
    private static final int ADD = 1;
    private static final int MULTIPLY = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int JUMP_IF_TRUE = 5;
    private static final int JUMP_IF_FALSE = 6;
    private static final int LESS = 7;
    private static final int EQUALS = 8;

    private static final int POSITION_MODE = 0;

    Deque<Integer> input;
    List<Integer> output;
    int[] program;
    AtomicInteger index;


    public SunnyAsteroids(int[] instructions, Integer... input) {
        program = instructions;
        this.input = Stream.of(input).collect(Collectors.toCollection(LinkedList::new));
        this.output = new ArrayList<>();
        index = new AtomicInteger(-1);
    }

    public List<Integer> execute() {
        while (program[index.incrementAndGet()] != STOP) {
            executeOperation();
        }
        return output;
    }

    private void executeOperation() {
        int opCode = getCurrentOpCode();
        int[] mode = getParamMode();
        if (opCode == INPUT) setValueByIndex(index.incrementAndGet(), input.removeFirst(), mode[0]);
        else if (opCode == OUTPUT) output.add(getValueByIndex(index.incrementAndGet(), mode[0]));
        else {
            int arg1 = getValueByIndex(index.incrementAndGet(), mode[0]);
            int arg2 = getValueByIndex(index.incrementAndGet(), mode[1]);
            if (isJumpOperation(opCode)) {
                if (opCode == JUMP_IF_TRUE && arg1 != 0) index.set(arg2 - 1);
                if (opCode == JUMP_IF_FALSE && arg1 == 0) index.set(arg2 - 1);
            } else {
                int result = getResult(opCode, arg1, arg2);
                setValueByIndex(index.incrementAndGet(), result, mode[2]);
            }
        }
    }

    private int getCurrentOpCode() {
        if (isSimpleOperation(index.get()))
            return program[index.get()];
        String op = String.valueOf(program[index.get()]);
        return Integer.parseInt(op.substring(op.length() - 2));
    }

    private int[] getParamMode() {
        int[] mode = new int[]{POSITION_MODE, POSITION_MODE, POSITION_MODE};
        if (!isSimpleOperation(index.get())) {
            String op = String.valueOf(program[index.get()]);
            for (int i = op.length() - 3, j = 0; i >= 0; i--, j++) {
                mode[j] = Integer.parseInt(String.valueOf(op.charAt(i)));
            }
        }
        return mode;
    }

    private boolean isSimpleOperation(int index) {
        return String.valueOf(program[index]).length() == 1;
    }

    private int getValueByIndex(int index, int mode) {
        if (mode == POSITION_MODE)
            return program[program[index]];
        return program[index];
    }

    private void setValueByIndex(int index, int value, int mode) {
        if (mode == POSITION_MODE)
            program[program[index]] = value;
        else program[index] = value;
    }

    private boolean isJumpOperation(int code) {
        return code == JUMP_IF_FALSE || code == JUMP_IF_TRUE;
    }

    private int getResult(int operation, int arg1, int arg2) {
        int result = 0;
        if (operation == ADD) result = arg1 + arg2;
        if (operation == MULTIPLY) result = arg1 * arg2;
        if (operation == LESS) result = arg1 < arg2 ? 1 : 0;
        if (operation == EQUALS) result = arg1 == arg2 ? 1 : 0;
        return result;
    }
}
