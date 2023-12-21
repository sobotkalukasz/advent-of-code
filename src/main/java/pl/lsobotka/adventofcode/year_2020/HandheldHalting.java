package pl.lsobotka.adventofcode.year_2020;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
* https://adventofcode.com/2020/day/8
* */
public class HandheldHalting {

    enum Type {ACC, JMP, NOP}

    private final Pattern SPACE = Pattern.compile(" ");
    private final Pattern ACC_PATTERN = Pattern.compile("acc");
    private final Pattern JMP_PATTERN = Pattern.compile("jmp");

    private int accValue = 0;
    private int line = 0;
    private boolean execute;

    private final Runnable terminate = () -> execute = false;
    private final Runnable noOp = () -> line++;

    private Runnable createAcc(int value) {
        return () -> {
            accValue += value;
            line++;
        };
    }

    private Runnable createJmp(int value) {
        return () -> line += value;
    }

    public List<Instruction> createInstructions(List<String> rawInput) {
        return rawInput.stream().map(this::createInstruction).collect(Collectors.toList());
    }

    private Instruction createInstruction(String rawOp) {
        String[] split = SPACE.split(rawOp);
        int value = Integer.parseInt(split[1]);
        if (ACC_PATTERN.matcher(split[0]).matches())
            return new Instruction(Type.ACC, value, createAcc(value), terminate);
        else if (JMP_PATTERN.matcher(split[0]).matches())
            return new Instruction(Type.JMP, value, createJmp(value), terminate);
        return new Instruction(Type.NOP, value, noOp, terminate);
    }

    // solution for 1st star - detect infinite loop and break the program
    public int executeProgram(List<Instruction> instructions) {
        init();
        while (execute) {
            if(line == instructions.size() -1)
                terminate.run();
            instructions.get(line).execute();
        }
        return accValue;
    }

    private void init() {
        accValue = 0;
        line = 0;
        execute = true;
    }

    // solution for 2nd star - find corrupted instruction and reach last line
    public int fixProgram(List<Instruction> ins){
        boolean isFixed = false;
        int lastIndex = ins.size() - 1;

        for(int i = 0; i <= lastIndex && !isFixed; i++){
            List<Instruction> copy = ins.stream().map(Instruction::copy).collect(Collectors.toList());
            switchInstruction(copy.get(i));
            executeProgram(copy);
            isFixed = copy.get(lastIndex).executed;
        }
        return accValue;
    }

    public void switchInstruction(Instruction ins){
        if(ins.type == Type.JMP){
            ins.type = Type.NOP;
            ins.operation = noOp;
        } else if(ins.type == Type.NOP){
            ins.type = Type.JMP;
            ins.operation = createJmp(ins.value);
        }
    }

    public static class Instruction {
        Type type;
        int value;
        Runnable operation;
        Runnable terminate;
        boolean executed;

        Instruction(Type type, int value, Runnable operation, Runnable terminate) {
            this.type = type;
            this.value = value;
            this.operation = operation;
            this.terminate = terminate;
            this.executed = false;
        }

        Instruction copy() {
            return new Instruction(type, value, operation, terminate);
        }

        public void execute() {
            if (executed) {
                terminate.run();
            } else {
                executed = true;
                operation.run();
            }
        }
    }
}
