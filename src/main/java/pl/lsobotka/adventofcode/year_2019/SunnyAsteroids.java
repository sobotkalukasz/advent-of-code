package pl.lsobotka.adventofcode.year_2019;

import java.util.List;

/*
 * https://adventofcode.com/2019/day/5
 * */
public class SunnyAsteroids {

    private final IntCode intCode;

    public SunnyAsteroids(long[] instructions, Long... input) {
        intCode = new IntCode(instructions, input);
    }

    public List<Long> execute(){
        return intCode.execute();
    }
}
