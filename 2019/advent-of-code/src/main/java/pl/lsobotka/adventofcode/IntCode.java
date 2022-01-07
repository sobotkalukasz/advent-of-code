package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntCode {

    Deque<Long> input;
    List<Long> output;

    Memory memory;
    AtomicInteger index;
    long relativeBase;

    public IntCode(long[] instructions, Long... input) {
        memory = new Memory(instructions);
        this.input = Stream.of(input).collect(Collectors.toCollection(LinkedList::new));
        output = new ArrayList<>();
        index = new AtomicInteger(-1);
        relativeBase = 0;
    }

    public void addInput(long... input) {
        for (long i : input) {
            this.input.addLast(i);
        }
    }

    public boolean isHalted() {
        return memory.isHalted(index.get());
    }

    public List<Long> execute() {
        while (canExecute()) {
            executeOperation();
        }
        return output;
    }

    public List<Long> executeUntilOutput() {
        return executeUntilExpectedOutputSize(1);
    }

    public List<Long> executeUntilExpectedOutputSize(final int expectedOutputSize) {
        output.clear();
        while (output.size() != expectedOutputSize && canExecute()) {
            executeOperation();
        }
        return output;
    }

    private boolean canExecute(){
        return memory.canExecute(index.incrementAndGet());
    }

    private void executeOperation() {
        Operation opCode = getCurrentOpCode();
        Mode[] mode = getParamMode();
        if (opCode == Operation.INPUT) setValueAtPosition(index.incrementAndGet(), input.removeFirst(), mode[0]);
        else if (opCode == Operation.OUTPUT) output.add(getValueAtPosition(index.incrementAndGet(), mode[0]));
        else if (opCode == Operation.RELATIVE_BASE)
            relativeBase += getValueAtPosition(index.incrementAndGet(), mode[0]);
        else {
            long arg1 = getValueAtPosition(index.incrementAndGet(), mode[0]);
            long arg2 = getValueAtPosition(index.incrementAndGet(), mode[1]);
            if (isJumpOperation(opCode)) {
                if (opCode == Operation.JUMP_IF_TRUE && arg1 != 0) index.set((int) arg2 - 1);
                if (opCode == Operation.JUMP_IF_FALSE && arg1 == 0) index.set((int) arg2 - 1);
            } else {
                long result = getResult(opCode, arg1, arg2);
                setValueAtPosition(index.incrementAndGet(), result, mode[2]);
            }
        }
    }

    private Operation getCurrentOpCode() {
        if (isSimpleOperation(index.get()))
            return Operation.getByValue((int) memory.getValue(index.get()));
        String op = String.valueOf(memory.getValue(index.get()));
        return Operation.getByValue(Integer.parseInt(op.substring(op.length() - 2)));
    }

    private Mode[] getParamMode() {
        Mode[] mode = new Mode[]{Mode.POSITION, Mode.POSITION, Mode.POSITION};
        if (!isSimpleOperation(index.get())) {
            String op = String.valueOf(memory.getValue(index.get()));
            for (int i = op.length() - 3, j = 0; i >= 0; i--, j++) {
                mode[j] = Mode.getByValue(op.charAt(i));
            }
        }
        return mode;
    }

    private boolean isSimpleOperation(int index) {
        return String.valueOf(memory.getValue(index)).length() == 1;
    }

    private long getValueAtPosition(int index, Mode mode) {
        return switch (mode) {
            case POSITION -> memory.getValue((int) memory.getValue(index));
            case IMMEDIATE -> memory.getValue(index);
            case RELATIVE -> memory.getValue((int) Math.addExact(relativeBase, memory.getValue(index)));
        };
    }

    private void setValueAtPosition(int index, long value, Mode mode) {
        switch (mode) {
            case POSITION -> memory.setValue((int) memory.getValue(index), value);
            case IMMEDIATE -> memory.setValue(index, value);
            case RELATIVE -> memory.setValue((int) Math.addExact(relativeBase, memory.getValue(index)), value);
        }
    }

    private boolean isJumpOperation(Operation code) {
        return code == Operation.JUMP_IF_FALSE || code == Operation.JUMP_IF_TRUE;
    }

    private long getResult(Operation op, long arg1, long arg2) {
        long result = 0;
        if (op == Operation.ADD) result = arg1 + arg2;
        if (op == Operation.MULTIPLY) result = arg1 * arg2;
        if (op == Operation.LESS) result = arg1 < arg2 ? 1 : 0;
        if (op == Operation.EQUALS) result = arg1 == arg2 ? 1 : 0;
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

    private enum Operation {

        ADD(1),
        MULTIPLY(2),
        INPUT(3),
        OUTPUT(4),
        JUMP_IF_TRUE(5),
        JUMP_IF_FALSE(6),
        LESS(7),
        EQUALS(8),
        RELATIVE_BASE(9);

        int value;

        Operation(int value) {
            this.value = value;
        }

        public static Operation getByValue(int toFind) {
            return Stream.of(Operation.values()).filter(m -> m.value == toFind).findFirst().orElseThrow(() -> new EnumConstantNotPresentException(Mode.class, String.valueOf(toFind)));
        }
    }

    private static class Memory {
        private static final int STOP = 99;
        private long[] program;

        Memory(long[] instructions) {
            program = instructions;
        }

        protected long getValue(int index) {
            if (program.length <= index) extendMemory(index + 1);
            return program[index];
        }

        protected void setValue(int index, long value) {
            if (program.length <= index) extendMemory(index + 1);
            program[index] = value;
        }

        protected boolean canExecute(int index) {
            return !isHalted(index);
        }

        protected boolean isHalted(int index) {
            return getValue(index) == STOP || getValue(index) + 1 == STOP;
        }

        private void extendMemory(int targetIndexSize) {
            long[] temp = new long[targetIndexSize];
            System.arraycopy(program, 0, temp, 0, program.length);
            program = temp;
        }

    }
}
