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
        int operation = program[index];
        if (operation == INPUT) program[program[++index]] = input.removeFirst();
        else if (operation == OUTPUT) output.add(program[program[++index]]);
        else if (isSimplyCode(operation)) {
            int arg1 = program[program[++index]];
            int arg2 = program[program[++index]];
            if (isJumpOperation(operation)) {
                if (operation == JUMP_IF_TRUE && arg1 != 0) index = arg2 - 1;
                if (operation == JUMP_IF_FALSE && arg1 == 0) index = arg2 - 1;
             } else{
                int result = 0;
                if (operation == ADD) result = arg1 + arg2;
                if (operation == MULTIPLY) result = arg1 * arg2;
                if (operation == LESS) result = arg1 < arg2 ? 1 : 0;
                if (operation == EQUALS) result = arg1 == arg2 ? 1 : 0;
                program[program[++index]] = result;
            }
        } else index = executeComplexOperation(index);
        return index;
    }

    private boolean isSimplyCode(int code) {
        return code == ADD || code == MULTIPLY || code == JUMP_IF_TRUE || code == JUMP_IF_FALSE || code == LESS || code == EQUALS;
    }

    private int executeComplexOperation(int index) {
        String op = String.valueOf(program[index]);
        int operation = Integer.parseInt(op.substring(op.length() - 2));
        int[] mode = new int[]{0, 0, 0};

        for (int i = op.length() - 3, j = 0; i >= 0; i--, j++) {
            mode[j] = Integer.parseInt(String.valueOf(op.charAt(i)));
        }
        if (operation == OUTPUT) {
            output.add(mode[0] == POSITION_MODE ? program[program[++index]] : program[++index]);
        } else {
            int arg1 = mode[0] == POSITION_MODE ? program[program[++index]] : program[++index];
            int arg2 = mode[1] == POSITION_MODE ? program[program[++index]] : program[++index];
            if (isJumpOperation(operation)) {
                if (operation == JUMP_IF_TRUE && arg1 != 0) index = arg2 - 1;
                if (operation == JUMP_IF_FALSE && arg1 == 0) index = arg2 - 1;
            } else {
                int result = 0;
                if (operation == ADD) result = arg1 + arg2;
                if (operation == MULTIPLY) result = arg1 * arg2;
                if (operation == LESS) result = arg1 < arg2 ? 1 : 0;
                if (operation == EQUALS) result = arg1 == arg2 ? 1 : 0;
                if (mode[2] == POSITION_MODE) program[program[++index]] = result;
                else program[++index] = result;
            }
        }
        return index;
    }

    private boolean isJumpOperation(int code) {
        return code == JUMP_IF_FALSE || code == JUMP_IF_TRUE;
    }
}
