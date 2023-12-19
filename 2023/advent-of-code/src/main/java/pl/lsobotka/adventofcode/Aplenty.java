package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2023/day/19
 * */
public class Aplenty {

    private final Map<String, Workflow> workflows;
    private final List<Rating> ratings;

    public Aplenty(final List<String> input) {
        this.workflows = Workflow.from(input);
        this.ratings = Rating.from(input);
    }

    long sumAcceptedRatings() {
        final String startingWorkflow = "in";
        final List<Rating> accepted = new ArrayList<>();

        for (Rating rating : ratings) {
            String workflowName = startingWorkflow;

            while (!workflowName.equals("A") && !workflowName.equals("R")) {
                final Workflow actual = workflows.get(workflowName);
                workflowName = actual.process(rating);
            }

            if (workflowName.equals("A")) {
                accepted.add(rating);
            }
        }

        return accepted.stream().map(Rating::totalValue).reduce(0L, Long::sum);
    }

    long solvePart2() {
        return 0;
    }

    record Workflow(String name, List<Operation> operations) {
        private static final Pattern workflowPattern = Pattern.compile("([a-z]+)\\{([ARa-z0-9,:<>]+)}");

        static Map<String, Workflow> from(final List<String> input) {
            final Map<String, Workflow> workflows = new HashMap<>();

            for (String row : input) {
                final Matcher matcher = workflowPattern.matcher(row);
                if (matcher.find()) {
                    final String name = matcher.group(1);
                    final List<Operation> operations = new ArrayList<>();
                    for (String op : matcher.group(2).split(",")) {
                        operations.add(Operation.from(op));
                    }
                    workflows.put(name, new Workflow(name, operations));
                }
            }
            return workflows;
        }

        String process(final Rating rating) {
            String targetWorkflow = "";
            for (Operation op : operations) {
                if (targetWorkflow.isEmpty()) {
                    final long partValue = rating.getPartNameValue(op.partName);
                    targetWorkflow = op.determineTarget(partValue);
                }
            }
            return targetWorkflow;
        }
    }

    record Operation(String partName, Predicate<Long> predicate, String target) {
        private static final Pattern comparePattern = Pattern.compile("([a-z]+)([<>])(\\d+):([A-z]+)");

        static Operation from(String rawOperation) {
            final Operation operation;

            final Matcher matcher = comparePattern.matcher(rawOperation);
            if (matcher.find()) {
                final String partName = matcher.group(1);
                final long value = Long.parseLong(matcher.group(3));
                final String target = matcher.group(4);

                final String operator = matcher.group(2);
                final Predicate<Long> test;
                if (operator.equals(">")) {
                    test = i -> i > value;
                } else {
                    test = i -> i < value;
                }
                operation = new Operation(partName, test, target);
            } else {
                operation = new Operation("", i -> true, rawOperation);
            }

            return operation;
        }

        String determineTarget(long value) {
            return predicate.test(value) ? target : "";
        }
    }

    record Rating(Map<String, Long> ratings) {
        private static final Pattern rattingPattern = Pattern.compile("([a-z])=(\\d+)");

        static List<Rating> from(final List<String> input) {
            final List<Rating> ratings = new ArrayList<>();
            for (String row : input) {
                final Map<String, Long> ratingMap = rattingPattern.matcher(row)
                        .results()
                        .collect(Collectors.toMap(r -> r.group(1), r -> Long.parseLong(r.group(2))));
                if (!ratingMap.isEmpty()) {
                    ratings.add(new Rating(ratingMap));
                }
            }
            return ratings;
        }

        long getPartNameValue(final String partName) {
            return ratings.getOrDefault(partName, 0L);
        }

        long totalValue() {
            return ratings.values().stream().reduce(0L, Long::sum);
        }
    }

}
