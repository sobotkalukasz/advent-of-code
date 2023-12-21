package pl.lsobotka.adventofcode.year_2019;

/*
 * https://adventofcode.com/2019/day/2
 * */
public class ProgramAlarm {

    private static final int STOP = 99;
    private static final int ADD = 1;

    public static int[] processProgram(int[] program) {
        int opIndex = -1;

        while (program[++opIndex] != STOP) {
            int operation = program[opIndex];
            int arg1 = program[program[++opIndex]];
            int arg2 = program[program[++opIndex]];
            program[program[++opIndex]] = operation == ADD ? arg1 + arg2 : arg1 * arg2;
        }
        return program;
    }

    public static Params findParamsFor(int[] program, int value) {
        int[] copy = new int[program.length];
        for (int i = 0; i <= 99; i++) {
            for (int j = 0; j < 99; j++) {
                System.arraycopy(program, 0, copy, 0, program.length);
                copy[1] = i;
                copy[2] = j;
                int[] ints = processProgram(copy);
                if (ints[0] == value) return new Params(i, j);
            }

        }
        return new Params(-1, -1);
    }

    public static record Params(int noun, int verb) {
    }
}
