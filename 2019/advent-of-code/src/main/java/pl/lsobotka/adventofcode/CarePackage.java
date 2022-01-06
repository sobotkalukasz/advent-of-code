package pl.lsobotka.adventofcode;

import java.util.List;

/*
 * https://adventofcode.com/2019/day/13
 * */
public class CarePackage {

    final long[] program;

    public CarePackage(long[] program) {
        this.program = program;
    }

    public int countBlockTiles() {
        final IntCode intCode = new IntCode(program);
        final List<Long> output = intCode.execute();

        int count = 0;
        for (int index = 2; index < output.size(); index += 3) {
            if (output.get(index) == 2) {
                count++;
            }
        }

        return count;
    }
}
