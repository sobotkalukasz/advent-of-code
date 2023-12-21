package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/*
 * https://adventofcode.com/2023/day/19
 * */
public class Aplenty {

    private static final String START = "in";
    private static final String ACCEPTED = "A";
    private static final String REJECTED = "R";

    private final Map<String, Workflow> workflows;
    private final List<Rating> ratings;

    public Aplenty(final List<String> input) {
        this.workflows = Workflow.from(input);
        this.ratings = Rating.from(input);
    }

    long sumAcceptedRatings() {
        final List<Rating> accepted = new ArrayList<>();

        for (Rating rating : ratings) {
            String workflowName = START;

            while (!workflowName.equals(ACCEPTED) && !workflowName.equals(REJECTED)) {
                final Workflow actual = workflows.get(workflowName);
                workflowName = actual.process(rating);
            }

            if (workflowName.equals(ACCEPTED)) {
                accepted.add(rating);
            }
        }

        return accepted.stream().map(Rating::totalValue).reduce(0L, Long::sum);
    }

    long countAcceptedPermutations() {
        long permutations = 0;
        final List<Path> paths = new ArrayList<>(List.of(Path.start()));

        while (!paths.isEmpty()) {
            final Path path = paths.removeLast();
            if (path.actual.equals(ACCEPTED) || path.actual.equals(REJECTED)) {
                if (path.actual.equals(ACCEPTED)) {
                    permutations += path.permutations();
                }
                continue;
            }

            final Map<String, Range> ranges = new HashMap<>();
            for (Operation op : workflows.get(path.actual).operations) {
                final Map<String, Range> actualRanges = new HashMap<>(ranges);
                final Range commonPassRange = actualRanges.getOrDefault(op.partName, Range.def())
                        .commonRange(op.toPass());
                actualRanges.put(op.partName, commonPassRange);

                paths.add(path.next(op.target, actualRanges));

                final Range commonFailureRange = ranges.getOrDefault(op.partName, Range.def())
                        .commonRange(op.toFailure());
                ranges.put(op.partName, commonFailureRange);
            }
        }

        return permutations;
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

    record Operation(String partName, Predicate<Long> predicate, long value, String target) {
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
                operation = new Operation(partName, test, value, target);
            } else {
                operation = new Operation("", i -> true, -1, rawOperation);
            }

            return operation;
        }

        String determineTarget(long value) {
            return predicate.test(value) ? target : "";
        }

        Range toPass() {
            if (value == -1) {
                return Range.def();
            } else if (predicate.test(value + 1)) {
                return new Range(value + 1, 4000);
            } else {
                return new Range(1, value - 1);
            }
        }

        Range toFailure() {
            if (value == -1) {
                return Range.def();
            } else if (predicate.test(value + 1)) {
                return new Range(1, value);
            } else {
                return new Range(value, 4000);
            }
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

    record Path(String actual, List<String> visited, Map<String, Range> ranges) {

        static Path start() {
            return new Path("in", new ArrayList<>(),
                    Map.of("x", Range.def(), "m", Range.def(), "a", Range.def(), "s", Range.def()));
        }

        Path next(final String next, final Map<String, Range> nextRanges) {
            final List<String> nextVisited = new ArrayList<>(visited);
            nextVisited.add(actual);

            final Map<String, Range> commonRanges = new HashMap<>(ranges);
            for (var entry : commonRanges.entrySet()) {
                final Range common = entry.getValue().commonRange(nextRanges.get(entry.getKey()));
                commonRanges.put(entry.getKey(), common);
            }
            return new Path(next, nextVisited, commonRanges);
        }

        long permutations() {
            return ranges.values().stream().map(Range::diff).reduce(1L, (a, b) -> a * b);
        }
    }

    record Range(long from, long to) {
        static Range def() {
            return new Range(1, 4000);
        }

        boolean in(final long value) {
            return from <= value && value <= to;
        }

        long diff() {
            return to - from + 1;
        }

        Range commonRange(final Range other) {
            if (other == null) {
                return this;
            }
            final List<Long> common = LongStream.range(from, to + 1).boxed().filter(other::in).toList();
            return new Range(common.getFirst(), common.getLast());
        }
    }

}
