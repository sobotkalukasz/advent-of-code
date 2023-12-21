package pl.lsobotka.adventofcode.year_2019;

import java.util.List;

/*
 * https://adventofcode.com/2019/day/1
 * */
public class RocketEquation {

    public int calcRequiredFuel(List<Integer> moduleMass) {
        return moduleMass.stream().mapToInt(this::calcRequiredFuel).sum();
    }

    private int calcRequiredFuel(int moduleMass) {
        return Math.max(moduleMass / 3 - 2, 0);
    }

    public int calcRequiredFuelWithFuel(List<Integer> moduleMass) {
        return moduleMass.stream().mapToInt(this::calcRequiredFuelWithFuel).sum();
    }

    private int calcRequiredFuelWithFuel(int moduleMass) {
        int moduleFuel = calcRequiredFuel(moduleMass);
        int fuelMass = calcRequiredFuel(moduleFuel);
        int tempFuelMass = fuelMass;

        while (tempFuelMass != 0) {
            tempFuelMass = calcRequiredFuel(tempFuelMass);
            fuelMass += tempFuelMass;
        }

        return moduleFuel + fuelMass;
    }

}
