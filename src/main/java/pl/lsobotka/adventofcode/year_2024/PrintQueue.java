package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2024/day/5
 * */
public class PrintQueue {

    private static final Pattern ORDER_RULE_PATTERN = Pattern.compile("(\\d+)\\|(\\d+)");

    private final Map<Integer, List<Integer>> orderingRule;
    private final List<PageNumbers> pageNumbers;

    PrintQueue(final List<String> input) {
        orderingRule = prepareRules(input);
        pageNumbers = preparePages(input);
    }

    long sumMiddleOfCorrectlyOrderedPages() {
        return pageNumbers.stream()
                .filter(p -> p.isInCorrectOrder(orderingRule))
                .map(PageNumbers::getMiddlePage)
                .reduce(Integer::sum)
                .orElse(0);
    }

    long fixOrderAndSumMiddlePages() {
        return pageNumbers.stream()
                .filter(p -> !p.isInCorrectOrder(orderingRule))
                .map(p -> p.fixOrder(orderingRule))
                .map(PageNumbers::getMiddlePage)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private Map<Integer, List<Integer>> prepareRules(final List<String> input) {
        final Map<Integer, List<Integer>> rules = new HashMap<>();

        for (String line : input) {
            final Matcher matcher = ORDER_RULE_PATTERN.matcher(line);
            if (matcher.find()) {
                final int beforeNumber = Integer.parseInt(matcher.group(1));
                final int afterNumber = Integer.parseInt(matcher.group(2));
                rules.computeIfAbsent(beforeNumber, v -> new ArrayList<>()).add(afterNumber);
            }
        }

        return rules;
    }

    private List<PageNumbers> preparePages(final List<String> input) {
        final List<PageNumbers> numbers = new ArrayList<>();

        for (String line : input) {
            final Matcher matcher = PageNumbers.LINE_PATTERN.matcher(line);
            if (matcher.find()) {
                numbers.add(PageNumbers.of(line));
            }
        }
        return numbers;
    }

}

record PageNumbers(List<Integer> pages) {

    static final Pattern LINE_PATTERN = Pattern.compile("^(\\d+,)*\\d+$");
    static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    static PageNumbers of(final String line) {
        final List<Integer> pages = new ArrayList<>();
        final Matcher matcher = NUMBER_PATTERN.matcher(line);
        while (matcher.find()) {
            pages.add(Integer.parseInt(matcher.group()));
        }
        return new PageNumbers(pages);
    }

    boolean isInCorrectOrder(final Map<Integer, List<Integer>> orderingRule) {
        return isInCorrectOrder(pages, orderingRule);
    }

    private boolean isInCorrectOrder(List<Integer> toValidate, final Map<Integer, List<Integer>> orderingRule) {
        boolean isCorrectOrder = true;
        for (int i = 0; i < toValidate.size() && isCorrectOrder; i++) {
            final Integer page = toValidate.get(i);
            if (orderingRule.containsKey(page)) {
                final List<Integer> beforeRule = orderingRule.get(page);
                for (int j = i - 1; j >= 0; j--) {
                    if (beforeRule.contains(toValidate.get(j))) {
                        isCorrectOrder = false;
                        break;
                    }
                }
            }
        }
        return isCorrectOrder;
    }

    PageNumbers fixOrder(final Map<Integer, List<Integer>> orderingRule) {
        final List<Integer> fixedOrder = new ArrayList<>(pages);

        while (!isInCorrectOrder(fixedOrder, orderingRule)) {
            swap(fixedOrder, orderingRule);
        }

        return new PageNumbers(fixedOrder);
    }

    private void swap(final List<Integer> numbers, final Map<Integer, List<Integer>> orderingRule) {
        int index = -1;
        int moveToIndex = -1;

        for (int i = 1; i < pages.size() && index == -1; i++) {
            final Integer page = numbers.get(i);
            if (orderingRule.containsKey(page)) {
                final List<Integer> beforeRule = orderingRule.get(page);
                for (int j = 0; j < i; j++) {
                    if (beforeRule.contains(numbers.get(j))) {
                        moveToIndex = j;
                        index = i;
                        break;
                    }
                }
            }
        }

        if (index != -1) {
            final Integer removed = numbers.remove(index);
            numbers.add(moveToIndex, removed);
        }
    }

    Integer getMiddlePage() {
        return pages.get(pages.size() / 2);
    }

}
