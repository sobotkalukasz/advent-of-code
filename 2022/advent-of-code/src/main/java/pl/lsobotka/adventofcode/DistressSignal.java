package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.lsobotka.adventofcode.DistressSignal.Result.EQUAL;
import static pl.lsobotka.adventofcode.DistressSignal.Result.RIGHT_ORDER;
import static pl.lsobotka.adventofcode.DistressSignal.Result.WRONG_ORDER;

/*
 * https://adventofcode.com/2022/day/13
 * */
public class DistressSignal {

    final List<Pair> pairs;

    public DistressSignal(List<String> input) {
        final PairFactory factory = new PairFactory();
        this.pairs = factory.initPairs(input);
    }

    int determineSumOfRightOrderPairs() {
        return pairs.stream().filter(Pair::isRightOrder).mapToInt(Pair::getId).sum();
    }

    static class Pair {
        final int id;
        final Complex left;
        final Complex right;

        final boolean rightOrder;

        public Pair(final int id, final Complex left, final Complex right) {
            this.id = id;
            this.left = left;
            this.right = right;
            final Result compareOrder = left.compareOrder(right);
            this.rightOrder = compareOrder.equals(RIGHT_ORDER);
        }

        public int getId() {
            return id;
        }

        public boolean isRightOrder() {
            return rightOrder;
        }
    }

    interface Element {

        Result compareOrder(Element other);

        Result accept(OrderVisitor v);

        interface OrderVisitor {
            Result visit(Simple element);

            Result visit(Complex element);
        }

    }

    static class Simple implements Element {

        final int val;

        Simple(final int val) {
            this.val = val;
        }

        @Override
        public Result compareOrder(final Element other) {
            return other.accept(new OrderVisitor() {
                @Override
                public Result visit(Simple element) {
                    if (Simple.this.val < element.val) {
                        return RIGHT_ORDER;
                    } else if (Simple.this.val == element.val) {
                        return EQUAL;
                    } else {
                        return WRONG_ORDER;
                    }
                }

                @Override
                public Result visit(Complex element) {
                    final Complex thisComplex = new Complex(Simple.this);
                    return thisComplex.compareOrder(element);
                }
            });
        }

        @Override
        public Result accept(OrderVisitor v) {
            return v.visit(this);
        }

    }

    enum Result{
        RIGHT_ORDER, EQUAL, WRONG_ORDER;
    }

    static class Complex implements Element {
        final List<Element> elements;

        Complex(final List<Element> elements) {
            this.elements = elements;
        }

        Complex(final Simple simple) {
            this.elements = List.of(simple);
        }

        @Override
        public Result compareOrder(Element other) {
            return other.accept(new OrderVisitor() {
                @Override
                public Result visit(Simple element) {
                    final Complex other = new Complex(element);
                    return Complex.this.compareOrder(other);
                }

                @Override
                public Result visit(Complex element) {
                    final List<Element> other = element.elements;

                    Result order = EQUAL;
                    if (elements.isEmpty()) {
                        if(!other.isEmpty()){
                            order = RIGHT_ORDER;
                        }
                    } else {
                        for (int i = 0; i < elements.size() && order.equals(EQUAL); i++) {
                            if (i < other.size()) {
                                order = elements.get(i).compareOrder(other.get(i));
                            } else {
                                order = WRONG_ORDER;
                            }
                            if (order.equals(EQUAL) && i == elements.size() - 1 && elements.size() < other.size()) {
                                order = RIGHT_ORDER;
                            }
                        }
                    }
                    return order;
                }
            });
        }

        @Override
        public Result accept(OrderVisitor v) {
            return v.visit(this);
        }

    }

    protected static class PairFactory {

        List<Pair> initPairs(final List<String> rowPairs) {
            final List<Pair> pairs = new ArrayList<>();
            final List<String> temp = new ArrayList<>();
            final AtomicInteger pairId = new AtomicInteger();
            rowPairs.forEach(row -> {
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

        private Pair from(final List<String> rawPair, final int id) {
            final String first = rawPair.get(0);
            final Complex left = from(first.substring(1, first.length()));

            final String second = rawPair.get(1);
            final Complex right = from(second.substring(1, second.length()));
            return new Pair(id, left, right);
        }

        private Complex from(final String raw) {
            final List<Element> elements = new ArrayList<>();
            for (int i = 0; i < raw.length(); i++) {
                final char current = raw.charAt(i);
                if (current == '[') {
                    final int closeIndex = determineCloseIndex(raw, i + 1);
                    elements.add(from(raw.substring(i + 1, closeIndex)));
                    i = closeIndex;
                } else if (Character.isDigit(current)) {
                    if (i == raw.length() - 1) {
                        elements.add(new Simple(Integer.valueOf(raw.substring(i, raw.length()))));
                    } else {
                        final int valueEnd = determineValueEndIndex(raw, i + 1);
                        elements.add(new Simple(Integer.valueOf(raw.substring(i, valueEnd))));
                        i = valueEnd - 1;
                    }
                }
            }
            return new Complex(elements);
        }

        protected int determineCloseIndex(final String raw, final int from) {
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

        private int determineValueEndIndex(final String raw, final int from) {
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


