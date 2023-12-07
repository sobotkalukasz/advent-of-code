package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CamelCards {
    private final List<String> input;

    public CamelCards(final List<String> input) {
        this.input = input;
    }

    long winningScore() {
        final List<Hand> hands = input.stream().map(Hand::from).sorted(Hand.regularComparator).toList();
        return countScore(hands);
    }

    long winningScoreWithJoker() {
        final List<Hand> hands = input.stream().map(Hand::from).sorted(Hand.jokerComparator).toList();
        return countScore(hands);
    }

    private long countScore(final List<Hand> hands) {
        long score = 0;
        for (int i = 0; i < hands.size(); i++) {
            score += hands.get(i).bid * (i + 1);
        }

        return score;
    }

    record Hand(List<Card> cards, HandType type, HandType jokerType, long bid) {
        static final Pattern rowPattern = Pattern.compile("([A-Z0-9]{5})\\s(\\d+)");
        static final Comparator<Hand> regularComparator = Comparator.comparing(Hand::type, Enum::compareTo)
                .thenComparing(Hand::cards, (c1, c2) -> {
                    int comparisonResult = 0;
                    for (int i = 0; i < c1.size(); i++) {
                        final int compare = c1.get(i).compareTo(c2.get(i));
                        if (compare != 0) {
                            comparisonResult = compare;
                            break;
                        }
                    }
                    return comparisonResult;
                });

        static final Comparator<Hand> jokerComparator = Comparator.comparing(Hand::jokerType, Enum::compareTo)
                .thenComparing(Hand::cards, (c1, c2) -> {
                    int comparisonResult = 0;
                    for (int i = 0; i < c1.size(); i++) {
                        final int compare = Card.jokerComparator.compare(c1.get(i), c2.get(i));
                        if (compare != 0) {
                            comparisonResult = compare;
                            break;
                        }
                    }
                    return comparisonResult;
                });

        static Hand from(final String row) {
            final Matcher matcher = rowPattern.matcher(row);
            if (matcher.find()) {
                final List<Card> cards = matcher.group(1).chars().mapToObj(c -> Card.of((char) c)).toList();
                final HandType handType = HandType.of(cards);
                final HandType jokerType = handType.transformWithJoker(cards);
                final long bid = Long.parseLong(matcher.group(2));
                return new Hand(cards, handType, jokerType, bid);
            }
            throw new IllegalArgumentException("Unable to create hand from: " + row);
        }
    }

    enum Card {

        TWO('2', 1), //
        THREE('3', 2), //
        FOUR('4', 3), //
        FIVE('5', 4), //
        SIX('6', 5), //
        SEVEN('7', 6), //
        EIGHT('8', 7), //
        NINE('9', 8), //
        T('T', 9), //
        J('J', 0), //
        Q('Q', 10), //
        K('K', 11), //
        A('A', 12);

        static final Comparator<Card> jokerComparator = Comparator.comparing(Card::getStrength, Integer::compareTo);

        final char value;
        final int strength;

        Card(char value, int strength) {
            this.value = value;
            this.strength = strength;
        }

        public int getStrength() {
            return strength;
        }

        static Card of(final char c) {
            return Arrays.stream(Card.values())
                    .filter(card -> card.value == c)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unable to determine Card for : " + c));
        }

    }

    enum HandType {
        HIGH_CARD(hand -> hand.contains(1L) && hand.size() == 5), //
        ONE_PAIR(hand -> hand.contains(2L) && hand.contains(1L) && hand.size() == 4), //
        TWO_PAIR(hand -> hand.contains(2L) && hand.contains(1L) && hand.size() == 3), //
        THREE(hand -> hand.contains(3L) && hand.contains(1L) && hand.size() == 3), //
        FULL_HOUSE(hand -> hand.contains(3L) && hand.contains(2L) && hand.size() == 2), //
        FOUR(hand -> hand.contains(4L) && hand.contains(1L) && hand.size() == 2), //
        FIVE(hand -> hand.contains(5L) && hand.size() == 1);
        final Predicate<Collection<Long>> handPredicate;

        HandType(Predicate<Collection<Long>> handPredicate) {
            this.handPredicate = handPredicate;
        }

        static HandType of(final List<Card> cards) {
            final Collection<Long> values = cards.stream()
                    .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                    .values();

            return Arrays.stream(HandType.values())
                    .filter(t -> t.handPredicate.test(values))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unable to determine type for hand: " + cards));
        }

        HandType transformWithJoker(final List<Card> cards) {
            final long joker = cards.stream().filter(Card.J::equals).count();
            if (joker == 0) {
                return this;
            }

            return switch (this) {
                case HIGH_CARD -> {
                    if (joker == 1) {
                        yield HandType.ONE_PAIR;
                    }
                    yield HandType.HIGH_CARD;
                }
                case ONE_PAIR -> {
                    if (joker == 1 || joker == 2) {
                        yield HandType.THREE;
                    }
                    yield ONE_PAIR;
                }
                case TWO_PAIR -> {
                    if (joker == 1) {
                        yield HandType.FULL_HOUSE;
                    } else if (joker == 2) {
                        yield HandType.FOUR;
                    }
                    yield FULL_HOUSE;
                }
                case THREE -> {
                    if (joker == 1 || joker == 3) {
                        yield HandType.FOUR;
                    }
                    yield THREE;
                }
                case FULL_HOUSE -> {
                    if (joker == 2 || joker == 3) {
                        yield HandType.FIVE;
                    }
                    yield FULL_HOUSE;
                }
                case FOUR -> {
                    if (joker == 1 || joker == 4) {
                        yield HandType.FIVE;
                    }
                    yield FOUR;
                }
                case FIVE -> HandType.FIVE;
            };

        }

    }

}
