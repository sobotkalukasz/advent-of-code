package pl.lsobotka.adventofcode;

import java.util.List;

/*
 * https://adventofcode.com/2019/day/9
 * */
public class SensorBoost {

    private final IntCode intCode;

    public SensorBoost(long[] instructions, Long... input) {
        intCode = new IntCode(instructions, input);
    }

    public List<Long> execute(){
        return intCode.execute();
    }
}
