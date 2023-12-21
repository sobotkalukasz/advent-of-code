package pl.lsobotka.adventofcode.year_2021;

import java.util.List;

/*
 * https://adventofcode.com/2021/day/7
 * */
public abstract class TreacheryOfWhales {

    public long getMinFuelConsumption(final List<Integer> positions) {
        final int minPosition = positions.stream().min(Integer::compareTo).orElse(0);
        final int maxPosition = positions.stream().max(Integer::compareTo).orElse(0);
        return getMinFuelConsumption(positions, minPosition, maxPosition);
    }

    private long getMinFuelConsumption(final List<Integer> positions, final int minPosition, final int maxPosition) {

        if (minPosition == maxPosition) {
            return fuelConsumptionForPosition(positions, minPosition);
        }

        final int middlePosition = ((maxPosition - minPosition) / 2) + minPosition;

        final long previousConsumption = fuelConsumptionForPosition(positions, middlePosition - 1);
        final long middleConsumption = fuelConsumptionForPosition(positions, middlePosition);
        final long nextConsumption = fuelConsumptionForPosition(positions, middlePosition + 1);

        final long minFuelConsumption;
        if (middleConsumption < previousConsumption && middleConsumption < nextConsumption) {
            minFuelConsumption = middleConsumption;
        } else if (middleConsumption < nextConsumption) {
            minFuelConsumption = getMinFuelConsumption(positions, minPosition, middlePosition - 1);
        } else {
            minFuelConsumption = getMinFuelConsumption(positions, middlePosition + 1, maxPosition);
        }
        return minFuelConsumption;
    }

    protected abstract int fuelConsumptionForPosition(final List<Integer> positions, final int destination);

    public static class Simple extends TreacheryOfWhales {
        @Override
        protected int fuelConsumptionForPosition(List<Integer> positions, int destination) {
            return positions.stream().map(position -> Math.abs(position - destination)).reduce(Integer::sum).orElse(0);
        }
    }

    public static class Complex extends TreacheryOfWhales {

        @Override
        protected int fuelConsumptionForPosition(List<Integer> positions, int destination) {
            return positions.stream().map(position -> {
                int distance = Math.abs(position - destination);
                int cost = 0;
                int costRate = 0;
                while (costRate++ < distance) {
                    cost += costRate;
                }
                return cost;
            }).reduce(Integer::sum).orElse(0);

        }
    }

}
