package pl.lsobotka.adventofcode.year_2023;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2023/day/4
 * */
public class Scratchcards {

    private final Map<Integer, Card> cards;

    public Scratchcards(List<String> input) {
        cards = input.stream().map(Card::of).collect(Collectors.toMap(Card::id, Function.identity()));
    }

    int sumCardPoints() {
        return cards.values().stream().map(Card::points).reduce(0, Integer::sum);
    }

    int countCards() {
        final Map<Integer, Card> tempCards = new HashMap<>(cards);
        final List<Integer> cardIds = tempCards.keySet().stream().sorted().toList();

        for (Integer cardId : cardIds) {
            final Card actualCard = tempCards.get(cardId);
            final int matchQty = actualCard.countMatch();
            for (int i = cardId + 1; i <= cardId + matchQty; i++) {
                int next = i > cardIds.size() ? i - cardIds.size() : i;
                tempCards.computeIfPresent(next, (key, card) -> card.increaseQty(actualCard.qty));
            }
        }

        return tempCards.values().stream().map(Card::qty).reduce(0, Integer::sum);
    }

    record Card(int id, Set<Integer> winNumbers, Set<Integer> numbers, int qty) {
        private static final Pattern cardPattern = Pattern.compile("Card\\s+([0-9]+): ([0-9\\s]+)\\|([0-9\\s]+)");
        private static final Pattern numberPattern = Pattern.compile("([0-9]+)");

        int points() {
            final Set<Integer> match = match();
            int points;

            if (match.size() > 1) {
                points = countPoints(1, match.size() - 1);
            } else {
                points = match.size();
            }

            return points;
        }

        int countMatch() {
            return match().size();
        }

        Card increaseQty(int by) {
            return new Card(id, winNumbers, numbers, qty + by);
        }

        private int countPoints(int actualPoints, int leftToCount) {
            if (leftToCount > 0) {
                return countPoints(actualPoints * 2, --leftToCount);
            }
            return actualPoints;
        }

        private Set<Integer> match() {
            final Set<Integer> match = new HashSet<>(numbers);
            match.retainAll(winNumbers);
            return match;
        }

        static Card of(final String row) {
            final Matcher cardMatcher = cardPattern.matcher(row);

            if (cardMatcher.find()) {
                final int cardId = Integer.parseInt(cardMatcher.group(1));
                final Set<Integer> winNumbers = numbers(cardMatcher.group(2));
                final Set<Integer> numbers = numbers(cardMatcher.group(3));
                return new Card(cardId, winNumbers, numbers, 1);
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
