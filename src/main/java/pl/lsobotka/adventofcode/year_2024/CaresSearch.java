package pl.lsobotka.adventofcode.year_2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
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
        return firstLetterCoords.stream().map(c -> countWordForCoord(c, chars)).reduce(0L, Long::sum);
    }

    private long countWordForCoord(Coord c, char[] chars) {
        return Stream.of(AllDir.values()).filter(dir -> isWordPresent(dir, c, chars)).count();
    }

    private boolean isWordPresent(final AllDir dir, final Coord coord, final char[] chars) {
        boolean isValid = true;
        Coord actual = coord;
        for (int i = 1; i < chars.length && isValid; i++) {
            actual = actual.getAdjacent(dir);
            isValid = isCharPresent(actual, chars[i]);
        }

        return isValid;
    }

    public long countXmasStars() {
        final List<Coord> centerChar = letterMap.entrySet()
                .stream()
                .filter(e -> e.getValue().equals('A'))
                .map(Map.Entry::getKey)
                .toList();
        return centerChar.stream().filter(this::countXmasStars).count();
    }

    private boolean countXmasStars(final Coord coord) {
        final char[] xWord = "MMSS".toCharArray();
        return IntStream.rangeClosed(0, xWord.length - 1).anyMatch(offset -> isXmasStar(coord, xWord, offset));
    }

    private boolean isXmasStar(final Coord coord, final char[] chars, final int offset) {
        final Coord upLeft = coord.getAdjacent(AllDir.UP_LEFT);
        final Coord downLeft = coord.getAdjacent(AllDir.DOWN_LEFT);
        final Coord upRight = coord.getAdjacent(AllDir.UP_RIGHT);
        final Coord downRight = coord.getAdjacent(AllDir.DOWN_RIGHT);

        return isCharPresent(upLeft, chars[(offset) % chars.length])
                && isCharPresent(upRight,chars[(1 + offset) % chars.length])
                && isCharPresent(downRight, chars[(2 + offset) % chars.length])
                && isCharPresent(downLeft, chars[(3 + offset) % chars.length]);
    }

    private boolean isCharPresent(final Coord coord, final char c) {
        return letterMap.getOrDefault(coord, ' ').equals(c);
    }

}
