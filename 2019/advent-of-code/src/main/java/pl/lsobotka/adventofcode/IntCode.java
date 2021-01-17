package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntCode {

    private static final int STOP = 99;
    private static final int ADD = 1;
    private static final int MULTIPLY = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int JUMP_IF_TRUE = 5;
    private static final int JUMP_IF_FALSE = 6;
    private static final int LESS = 7;
    private static final int EQUALS = 8;
    private static final int RELATIVE_BASE = 9;

    Deque<Long> input;
    List<Long> output;
    long[] program;
    AtomicInteger index;


    public IntCode(long[] instructions, Long... input) {
        program = instructions;
        this.input = Stream.of(input).collect(Collectors.toCollection(LinkedList::new));
        this.output = new ArrayList<>();
        index = new AtomicInteger(-1);
    }

    public void addInput(long... input) {
        for (long i : input) {
            this.input.addLast(i);
        }
    }

    public boolean isHalted() {
        return program[index.get()] == STOP || program[index.get() + 1] == STOP;
    }

    public List<Long> execute() {
        while (program[index.incrementAndGet()] != STOP) {
            executeOperation();
        }
        return output;
    }

    public List<Long> executeUntilOutput() {
        output.clear();
        while (output.isEmpty() && program[index.incrementAndGet()] != STOP) {
            executeOperation();
        }
        return output;
    }

    private void executeOperation() {
        int opCode = getCurrentOpCode();
        Mode[] mode = getParamMode();
        if (opCode == INPUT) setValueByIndex(index.incrementAndGet(), input.removeFirst(), mode[0]);
        else if (opCode == OUTPUT) output.add(getValueByIndex(index.incrementAndGet(), mode[0]));
        else if (opCode == RELATIVE_BASE) {
            long value = getValueByIndex(index.incrementAndGet(), mode[0]);
            setValueByIndex(0, value + value, mode[1]);
        } else {
            long arg1 = getValueByIndex(index.incrementAndGet(), mode[0]);
            long arg2 = getValueByIndex(index.incrementAndGet(), mode[1]);
            if (isJumpOperation(opCode)) {
                if (opCode == JUMP_IF_TRUE && arg1 != 0) index.set((int) arg2 - 1);
                if (opCode == JUMP_IF_FALSE && arg1 == 0) index.set((int) arg2 - 1);
            } else {
                long result = getResult(opCode, arg1, arg2);
                setValueByIndex(index.incrementAndGet(), result, mode[2]);
            }
        }
    }

    private int getCurrentOpCode() {
        if (isSimpleOperation(index.get()))
            return (int) program[index.get()];
        String op = String.valueOf(program[index.get()]);
        return Integer.parseInt(op.substring(op.length() - 2));
    }

    private Mode[] getParamMode() {
        Mode[] mode = new Mode[]{Mode.POSITION, Mode.POSITION, Mode.POSITION};
        if (!isSimpleOperation(index.get())) {
            String op = String.valueOf(program[index.get()]);
            for (int i = op.length() - 3, j = 0; i >= 0; i--, j++) {
                mode[j] = Mode.getByValue(op.charAt(i));
            }
        }
        return mode;
    }

    private boolean isSimpleOperation(int index) {
        return String.valueOf(program[index]).length() == 1;
    }

    private long getValueByIndex(int index, Mode mode) {

        return switch (mode) {
            case POSITION -> program[(int) program[index]];
            case IMMEDIATE -> program[index];
            case RELATIVE -> program[0] + program[index];
        };
    }

    private void setValueByIndex(int index, long value, Mode mode) {
        switch (mode) {
            case POSITION -> program[(int) program[index]] = value;
            case IMMEDIATE -> program[index] = value;
            case RELATIVE -> program[index] += value;
        }
    }

    private boolean isJumpOperation(int code) {
        return code == JUMP_IF_FALSE || code == JUMP_IF_TRUE;
    }

    private long getResult(int operation, long arg1, long arg2) {
        long result = 0;
        if (operation == ADD) result = arg1 + arg2;
        if (operation == MULTIPLY) result = arg1 * arg2;
        if (operation == LESS) result = arg1 < arg2 ? 1 : 0;
        if (operation == EQUALS) result = arg1 == arg2 ? 1 : 0;
        return result;
    }

    private enum Mode {

        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2);

        int value;

        Mode(int value) {
            this.value = value;
        }

        public static Mode getByValue(char toFind) {
            return getByValue(Character.getNumericValue(toFind));
        }

        public static Mode getByValue(int toFind) {
            return Stream.of(Mode.values()).filter(m -> m.value == toFind).findFirst().orElseThrow(() -> new EnumConstantNotPresentException(Mode.class, String.valueOf(toFind)));
        }
    }
}
