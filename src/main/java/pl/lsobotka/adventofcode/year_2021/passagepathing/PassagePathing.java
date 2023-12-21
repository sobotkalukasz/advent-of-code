package pl.lsobotka.adventofcode.year_2021.passagepathing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/12
 * */
public class PassagePathing {

    private final Map<Cave, List<Cave>> caves;

    public PassagePathing(final List<String> input) {
        this.caves = parseMapOfCaves(input);
    }

    public int countAllPathSingleVisit() {
        final BiPredicate<List<Cave>, Cave> singleVisitPredicate = (caves, cave) -> cave.isSmall() && caves.contains(
                cave);
        final List<List<Cave>> paths = countAllPaths(singleVisitPredicate);
        return paths.size();
    }

    public int countAllPathsVisitFirstSmallTwice() {
        final BiPredicate<List<Cave>, Cave> visitSmallTwicePredicate = (caves, cave) -> {
            if (cave.isSmall() && caves.contains(cave)) {
                final List<Cave> smallCaves = caves.stream().filter(Cave::isSmall).collect(Collectors.toList());
                return smallCaves.stream().anyMatch(c -> Collections.frequency(smallCaves, c) > 1);
            }
            return false;
        };
        final List<List<Cave>> paths = countAllPaths(visitSmallTwicePredicate);
        return paths.size();
    }

    private List<List<Cave>> countAllPaths(final BiPredicate<List<Cave>, Cave> smallCavePredicate) {
        return caves.keySet()
                .stream()
                .filter(Cave::isStart)
                .findFirst()
                .map(cave -> findAllPathsFor(cave, new ArrayList<>(), smallCavePredicate))
                .orElse(Collections.emptyList());
    }

    private List<List<Cave>> findAllPathsFor(final Cave cave, final List<Cave> currentPath,
            final BiPredicate<List<Cave>, Cave> smallCavePredicate) {
        currentPath.add(cave);
        final List<Cave> childCaves = this.caves.get(cave);

        final List<List<Cave>> foundPaths = new ArrayList<>();

        for (Cave childCave : childCaves) {
            if (childCave.isStart() || smallCavePredicate.test(currentPath, childCave)) {
                continue;
            }
            final List<Cave> caves = new ArrayList<>(currentPath);
            if (childCave.isEnd()) {
                caves.add(childCave);
                foundPaths.add(caves);
            } else {
                foundPaths.addAll(findAllPathsFor(childCave, caves, smallCavePredicate));
            }
        }

        return foundPaths;
    }

    private Map<Cave, List<Cave>> parseMapOfCaves(final List<String> input) {
        final Map<Cave, List<Cave>> caves = new HashMap<>();
        input.forEach(row -> {
            final String[] splitRow = row.split("-");
            final Cave firstCave = new Cave(splitRow[0]);
            final Cave secondCave = new Cave(splitRow[1]);

            caves.compute(firstCave, (key, value) -> {
                value = Objects.isNull(value) ? new ArrayList<>() : value;
                value.add(secondCave);
                return value;
            });
            caves.compute(secondCave, (key, value) -> {
                value = Objects.isNull(value) ? new ArrayList<>() : value;
                value.add(firstCave);
                return value;
            });
        });
        return caves;
    }
}
