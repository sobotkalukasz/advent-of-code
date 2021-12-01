package pl.lsobotka.adventofcode;

import java.util.List;

/*
 * https://adventofcode.com/2021/day/1
 * */
public class SonarSweep {

    public int depthMeasurement(final List<Integer> measurements){

        final int size = measurements.size();

        int depthIncreaseCount = 0;
        for(int i = 1; i < size; i++ ){
            final Integer first = measurements.get(i - 1);
            final Integer second = measurements.get(i);
            if(first < second){
                depthIncreaseCount++;
            }
        }

        return depthIncreaseCount;
    }
}
