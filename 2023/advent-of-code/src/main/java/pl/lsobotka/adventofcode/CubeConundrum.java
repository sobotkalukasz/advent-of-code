package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2023/day/2
 * */
public class CubeConundrum {

    private final List<Game> games;

    public CubeConundrum(List<String> input) {
        this.games = input.stream().map(Game::of).toList();
    }

    int sumIdsOfPossibleGames() {
        final Map<Colour, Integer> minValues = Map.of(Colour.RED, 12, Colour.GREEN, 13, Colour.BLUE, 14);
        return games.stream().filter(g -> g.isPossible(minValues)).map(Game::id).mapToInt(Integer::intValue).sum();
    }

    int sumThePowerOfGames() {
        return games.stream().map(Game::getPowerOfGame).mapToInt(Integer::intValue).sum();
    }

    enum Colour {
        BLUE(Pattern.compile("([0-9]+) blue")), RED(Pattern.compile("([0-9]+) red")), GREEN(
                Pattern.compile("([0-9]+) green"));

        final Pattern pattern;

        Colour(Pattern pattern) {
            this.pattern = pattern;
        }
    }

    record Game(int id, Map<Colour, Integer> cubes) {

        private static final Pattern gameIdPattern = Pattern.compile("Game ([0-9]+):");

        static Game of(final String gameRow) {
            final Map<Colour, Integer> valueMap = new HashMap<>(Map.of(Colour.RED, 0, Colour.GREEN, 0, Colour.BLUE, 0));
            int id = 0;

            final Matcher idMatcher = gameIdPattern.matcher(gameRow);
            if (idMatcher.find()) {
                id = Integer.parseInt(idMatcher.group(1));
            }

            for (Colour colour : Colour.values()) {
                final Matcher matcher = colour.pattern.matcher(gameRow);
                while (matcher.find()) {
                    final int actual = Integer.parseInt(matcher.group(1));
                    valueMap.computeIfPresent(colour, (key, val) -> val > actual ? val : actual);
                }
            }

            return new Game(id, valueMap);
        }

        boolean isPossible(final Map<Colour, Integer> forValues) {
            for (Colour colour : forValues.keySet()) {
                if (cubes.get(colour).compareTo(forValues.get(colour)) > 0) {
                    return false;
                }
            }
            return true;
        }

        int getPowerOfGame() {
            return cubes.values().stream().reduce(1, (a, b) -> a * b);
        }

    }
}
