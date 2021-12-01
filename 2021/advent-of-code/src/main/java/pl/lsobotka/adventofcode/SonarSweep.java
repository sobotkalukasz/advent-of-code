package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;

/*
 * https://adventofcode.com/2021/day/1
 * */
public class SonarSweep {

    public int depthMeasurement(final List<Integer> measurements) {

        final int size = measurements.size();

        int depthIncreaseCount = 0;
        for (int i = 1; i < size; i++) {
            final Integer first = measurements.get(i - 1);
            final Integer second = measurements.get(i);
            if (first < second) {
                depthIncreaseCount++;
            }
        }

        return depthIncreaseCount;
    }

    public int depthMeasurementSlidingWindow(final List<Integer> measurements, final int measurementsToSum) {

        final int size = measurements.size();
        final List<Integer> sumMeasurements = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (i + measurementsToSum <= size) {
                int sum = 0;
                for (int j = 0; j < measurementsToSum; j++) {
                    sum += measurements.get(i + j);
                }
                sumMeasurements.add(sum);
            }
        }

        return depthMeasurement(sumMeasurements);
    }
}

