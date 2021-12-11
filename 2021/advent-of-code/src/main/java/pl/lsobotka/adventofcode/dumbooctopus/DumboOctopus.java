package pl.lsobotka.adventofcode.dumbooctopus;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.lsobotka.adventofcode.Coordinate;

/*
 * https://adventofcode.com/2021/day/11
 * */
public class DumboOctopus {

    private final Map<Coordinate, Octopus> octopus;
    private final int maxRow;
    private final int maxColumn;

    public DumboOctopus(final List<String> input) {
        octopus = new HashMap<>();

        final List<List<String>> splitInput = input.stream()
                .map(str -> str.split(""))
                .map(Arrays::asList)
                .collect(Collectors.toList());
        maxRow = splitInput.size() - 1;
        maxColumn = splitInput.get(0).size() - 1;

        for (int row = 0; row <= maxRow; row++) {
            for (int col = 0; col <= maxColumn; col++) {
                final int value = Integer.parseInt(splitInput.get(row).get(col));
                final Coordinate coordinate = new Coordinate(row, col);
                octopus.put(coordinate, new Octopus(coordinate, value));
            }
        }
    }

    public long countFlashesAfterSteps(int steps) {
        long flashes = 0;
        while (steps-- > 0) {
            flashes += performStepAndCountFlashes();
        }
        return flashes;
    }

    public long countStepsToFlashAll() {
        long steps = 0;

        do {
            steps++;
        } while (performStepAndCountFlashes() != octopus.size());

        return steps;
    }

    private long performStepAndCountFlashes() {
        List<Coordinate> affected = increaseValueAndFlash(octopus.keySet());
        while (!affected.isEmpty()) {
            affected = increaseValueAndFlash(affected);
        }
        final long count = octopus.values().stream().filter(Octopus::isFlash).count();
        octopus.values().forEach(Octopus::resetFlash);
        return count;
    }

    private List<Coordinate> increaseValueAndFlash(final Collection<Coordinate> coordinates) {
        coordinates.forEach(c -> octopus.get(c).increaseValue());
        return octopus.values()
                .stream()
                .map(octopus -> octopus.flashAndGetAffected(maxRow, maxColumn))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
