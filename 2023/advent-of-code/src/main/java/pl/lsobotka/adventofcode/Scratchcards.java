package pl.lsobotka.adventofcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2023/day/4
 * */
public class Scratchcards {

    private final List<Card> cards;

    public Scratchcards(List<String> input) {
        cards = input.stream().map(Card::of).toList();
    }

    int sumCardPoints() {
        return cards.stream().map(Card::getPoints).reduce(0, Integer::sum);
    }

    record Card(int id, Set<Integer> winNumbers, Set<Integer> numbers) {
        private static final Pattern cardPattern = Pattern.compile("Card\\s+([0-9]+): ([0-9\\s]+)\\|([0-9\\s]+)");
        private static final Pattern numberPattern = Pattern.compile("([0-9]+)");

        int getPoints() {
            final Set<Integer> integers = myWiningNumbers();
            int points;

            if (integers.size() > 1) {
                points = countPoints(1, integers.size() - 1);
            } else {
                points = integers.size();
            }

            return points;
        }

        private int countPoints(int actualPoints, int leftToCount) {
            if (leftToCount > 0) {
                return countPoints(actualPoints * 2, --leftToCount);
            }
            return actualPoints;
        }

        private Set<Integer> myWiningNumbers() {
            final Set<Integer> myWiningNumbers = new HashSet<>(numbers);
            myWiningNumbers.retainAll(winNumbers);
            return myWiningNumbers;
        }

        static Card of(final String row) {
            final Matcher cardMatcher = cardPattern.matcher(row);

            if (cardMatcher.find()) {
                final int cardId = Integer.parseInt(cardMatcher.group(1));
                final Set<Integer> winNumbers = numbers(cardMatcher.group(2));
                final Set<Integer> numbers = numbers(cardMatcher.group(3));
                return new Card(cardId, winNumbers, numbers);
            } else {
                throw new IllegalArgumentException("Unknown card: " + row);
            }
        }

        private static Set<Integer> numbers(final String numberString) {
            return numberPattern.matcher(numberString)
                    .results()
                    .map(MatchResult::group)
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        }

    }

}
