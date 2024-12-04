package pl.lsobotka.adventofcode.year_2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.utils.AllDir;
import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/4
 * */
public class CaresSearch {

    private final Map<Coord, Character> letterMap;

    public CaresSearch(List<String> input) {
        letterMap = new HashMap<>();
        for (int row = 0; row < input.size(); row++) {
            final char[] splitRow = input.get(row).toCharArray();
            for (int col = 0; col < splitRow.length; col++) {
                final Coord coord = new Coord(row, col);
                letterMap.put(coord, splitRow[col]);
            }
        }
    }

    public long countWord(final String word) {
        final char[] chars = word.toCharArray();

        final List<Coord> firstLetterCoords = letterMap.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(chars[0]))
                .map(Map.Entry::getKey)
                .toList();
        return firstLetterCoords.stream()
                .map(c -> Stream.of(AllDir.values()).filter(dir -> isWordPresent(dir, c, chars)).count())
                .reduce(0L, Long::sum);
    }

    private boolean isWordPresent(final AllDir dir, final Coord coord, final char[] chars) {
        boolean isValid = true;
        Coord actual = coord;
        for (int i = 1; i < chars.length && isValid; i++) {
            actual = actual.getAdjacent(dir);
            final Character toTest = letterMap.getOrDefault(actual, ' ');
            isValid = toTest.equals(chars[i]);
        }

        return isValid;
    }
}
