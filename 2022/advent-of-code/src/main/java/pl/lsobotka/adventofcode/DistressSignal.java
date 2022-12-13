package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2022/day/13
 * */
public class DistressSignal {

    final List<String> input;

    public DistressSignal(List<String> input) {
        this.input = input;
    }

    int determineSumOfRightOrderPairs() {
        final List<Pair> pairs = ElementFactory.initPairs(input);
        return pairs.stream().filter(Pair::isRightOrder).mapToInt(Pair::id).sum();
    }

    int determineDecoderKey() {
        final List<String> dividers = List.of("[[2]]", "[[6]]");
        final List<String> rawData = Stream.concat(input.stream(), dividers.stream()).toList();
        final List<Row> rows = ElementFactory.initSortedRows(rawData);

        int decodedKey = 1;
        for (int i = 0; i < rows.size(); i++) {
            if (dividers.contains(rows.get(i).raw)) {
                decodedKey *= i + 1;
            }
        }

        return decodedKey;
    }

    record Pair(int id, Complex left, Complex right) {

        public boolean isRightOrder() {
            return left.compareTo(right) < 0;
        }
    }

    record Row(Element element, String raw) implements Comparable<Row> {

        @Override
        public int compareTo(final Row o) {
            return this.element.compareTo(o.element);
        }
    }

    interface Element extends Comparable<Element> {

        int accept(OrderVisitor v);

        interface OrderVisitor {
            int visit(Simple element);

            int visit(Complex element);
        }

    }

    record Simple(int val) implements Element {

        @Override
        public int accept(OrderVisitor v) {
            return v.visit(this);
        }

        @Override
        public int compareTo(Element o) {
            return o.accept(new OrderVisitor() {
                @Override
                public int visit(Simple element) {
                    return Integer.compare(Simple.this.val, element.val);
                }

                @Override
                public int visit(Complex element) {
                    final Complex thisComplex = Complex.from(Simple.this);
                    return thisComplex.compareTo(element);
                }
            });
        }
    }

    record Complex(List<Element> elements) implements Element {

        static Complex from(final Simple simple) {
            return new Complex(List.of(simple));
        }

        @Override
        public int accept(OrderVisitor v) {
            return v.visit(this);
        }

        @Override
        public int compareTo(Element o) {
            return o.accept(new OrderVisitor() {
                @Override
                public int visit(Simple element) {
                    final Complex other = Complex.from(element);
                    return Complex.this.compareTo(other);
                }

                @Override
                public int visit(Complex element) {
                    final List<Element> other = element.elements;

                    int order = 0;
                    if (elements.isEmpty() && !other.isEmpty()) {
                        order = -1;
                    } else {
                        for (int i = 0; i < elements.size() && order == 0; i++) {
                            if (i < other.size()) {
                                order = elements.get(i).compareTo(other.get(i));
                            } else {
                                order = 1;
                            }
                            if (order == 0 && i == elements.size() - 1 && elements.size() < other.size()) {
                                order = -1;
                            }
                        }
                    }
                    return order;
                }
            });
        }
    }

    protected static class ElementFactory {

        static List<Pair> initPairs(final List<String> input) {
            final List<Pair> pairs = new ArrayList<>();
            final List<String> temp = new ArrayList<>();
            final AtomicInteger pairId = new AtomicInteger();
            input.forEach(row -> {
                if (!row.isEmpty()) {
                    temp.add(row);
                    if (temp.size() == 2) {
                        pairs.add(from(temp, pairId.incrementAndGet()));
                        temp.clear();
                    }
                }
            });
            return pairs;
        }

        static List<Row> initSortedRows(final List<String> input) {
            return input.stream()
                    .filter(row -> !row.isEmpty())
                    .map(row -> new Row(from(row.substring(1)), row))
                    .sorted()
                    .collect(Collectors.toList());
        }

        static private Pair from(final List<String> rawPair, final int id) {
            final String first = rawPair.get(0);
            final Complex left = from(first.substring(1));

            final String second = rawPair.get(1);
            final Complex right = from(second.substring(1));
            return new Pair(id, left, right);
        }

        static private Complex from(final String raw) {
            final List<Element> elements = new ArrayList<>();
            for (int i = 0; i < raw.length(); i++) {
                final char current = raw.charAt(i);
                if (current == '[') {
                    final int closeIndex = determineClosingIndex(raw, i + 1);
                    elements.add(from(raw.substring(i + 1, closeIndex)));
                    i = closeIndex;
                } else if (Character.isDigit(current)) {
                    if (i == raw.length() - 1) {
                        elements.add(new Simple(Integer.parseInt(raw.substring(i))));
                    } else {
                        final int valueEnd = determineValueEndingIndex(raw, i + 1);
                        elements.add(new Simple(Integer.parseInt(raw.substring(i, valueEnd))));
                        i = valueEnd - 1;
                    }
                }
            }
            return new Complex(elements);
        }

        static protected int determineClosingIndex(final String raw, final int from) {
            int open = 0;
            int closeIndex = 0;
            for (int i = from; i < raw.length(); i++) {
                final char current = raw.charAt(i);
                if (current == '[') {
                    open++;
                } else if (current == ']') {
                    if (open == 0) {
                        closeIndex = i;
                        break;
                    } else {
                        open--;
                    }
                }
            }
            return closeIndex;
        }

        static private int determineValueEndingIndex(final String raw, final int from) {
            int endIndex = 0;
            for (int i = from; i < raw.length(); i++) {
                final char current = raw.charAt(i);
                if (current == ',' || current == '[' || current == ']') {
                    endIndex = i;
                    break;
                }
            }
            return endIndex == 0 ? raw.length() : endIndex;
        }
    }
}


