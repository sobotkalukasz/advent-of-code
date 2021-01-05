package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public SunnyAsteroids(int[] instructions, Integer... input) {
        program = instructions;
        this.input = Stream.of(input).collect(Collectors.toCollection(LinkedList::new));
        this.output = new ArrayList<>();
    }

    public List<Integer> execute() {
        int opIndex = -1;
        while (program[++opIndex] != STOP) {
            opIndex = executeOperation(opIndex);
        }
        return output;
    }

    private int executeOperation(int index) {
        return isSimpleOperation(index) ? executeSimple(index) : executeComplex(index);
    }

    private boolean isSimpleOperation(int index) {
        return String.valueOf(program[index]).length() == 1;
    }

    private int executeSimple(int index) {
        int operation = program[index];
        if (operation == INPUT) setValueByIndex(++index, input.removeFirst());
        else if (operation == OUTPUT) output.add(getValueByIndex(++index));
        else {
            int arg1 = getValueByIndex(++index);
            int arg2 = getValueByIndex(++index);
            if (isJumpOperation(operation)) {
                if (operation == JUMP_IF_TRUE && arg1 != 0) index = arg2 - 1;
                if (operation == JUMP_IF_FALSE && arg1 == 0) index = arg2 - 1;
            } else {
                int result = getResult(operation, arg1, arg2);
                setValueByIndex(++index, result);
            }
        }
        return index;
    }

    private int executeComplex(int index) {
        String op = String.valueOf(program[index]);
        int operation = Integer.parseInt(op.substring(op.length() - 2));
        int[] mode = new int[]{0, 0, 0};

        for (int i = op.length() - 3, j = 0; i >= 0; i--, j++) {
            mode[j] = Integer.parseInt(String.valueOf(op.charAt(i)));
        }
        if (operation == OUTPUT) {
            output.add(getValueByIndex(++index, mode[0]));
        } else {
            int arg1 = getValueByIndex(++index, mode[0]);
            int arg2 = getValueByIndex(++index, mode[1]);
            if (isJumpOperation(operation)) {
                if (operation == JUMP_IF_TRUE && arg1 != 0) index = arg2 - 1;
                if (operation == JUMP_IF_FALSE && arg1 == 0) index = arg2 - 1;
            } else {
                int result = getResult(operation, arg1, arg2);
                setValueByIndex(++index, result, mode[2]);
            }
        }
        return index;
    }

    private int getValueByIndex(int index) {
        return getValueByIndex(index, POSITION_MODE);
    }

    private int getValueByIndex(int index, int mode) {
        if (mode == POSITION_MODE)
            return program[program[index]];
        return program[index];
    }

    private void setValueByIndex(int index, int value) {
        setValueByIndex(index, value, POSITION_MODE);
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
