package pl.lsobotka.adventofcode;

import java.util.List;

/*
 * https://adventofcode.com/2019/day/5
 * */
public class SunnyAsteroids {

    private IntCode intCode;

    public SunnyAsteroids(int[] instructions, Integer... input) {
        intCode = new IntCode(instructions, input);
    }

    public List<Integer> execute(){
        return intCode.execute();
    }
}
