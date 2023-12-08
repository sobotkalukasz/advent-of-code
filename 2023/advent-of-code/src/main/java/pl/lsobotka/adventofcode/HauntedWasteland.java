package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HauntedWasteland {

    private final static String START_NODE = "AAA";
    private final static String END_NODE = "ZZZ";

    private final List<Instruction> instructions;
    private final Map<String, Node> nodes;
    private final Map<String, String> cache;

    public HauntedWasteland(final List<String> input) {
        this.instructions = Instruction.list(input.get(0));
        this.nodes = Node.map(input);
        this.cache = calculateCache(nodes, instructions);
    }

    public long countIterations() {
        long iterationCount = countIterations(START_NODE, END_NODE);
        return iterationCount * instructions.size();
    }

    public long countSimultaneousIterations() {
        List<String> actualPositions = nodes.keySet().stream().filter(key -> key.endsWith("A")).toList();

        final Map<String, Long> nodeToEnd = new HashMap<>();
        for (String node : actualPositions) {
            long iterationCount = countIterations(node, "Z");
            nodeToEnd.put(node, iterationCount);
        }

        final long multipliedIterations = nodeToEnd.values().stream().reduce(1L, (a, b) -> a * b);
        return multipliedIterations * instructions.size();
    }

    private long countIterations(final String startNode, final String endNode) {
        long iterationCount = 0;
        String currentNode = startNode;
        while (!currentNode.endsWith(endNode)) {
            currentNode = cache.get(currentNode);
            iterationCount++;
        }
        return iterationCount;
    }

    private Map<String, String> calculateCache(Map<String, Node> nodes, final List<Instruction> instructions) {
        Map<String, String> cache = new HashMap<>();
        for (String node : nodes.keySet()) {
            cache.put(node, calculateEndNode(node, instructions));
        }
        return cache;
    }

    private String calculateEndNode(final String startNode, final List<Instruction> instructions) {
        String currentNode = startNode;
        for (Instruction instruction : instructions) {
            currentNode = nodes.get(currentNode).apply(instruction);
        }
        return currentNode;
    }

    record Node(String node, String left, String right) {
        private final static Pattern nodePattern = Pattern.compile(
                "([A-Z0-9]{3})\\s=\\s\\(([A-Z0-9]{3}),\\s([A-Z0-9]{3})\\)");

        static Map<String, Node> map(final List<String> rows) {
            Map<String, Node> nodes = new HashMap<>();

            for (String row : rows) {
                final Matcher nodeMatcher = nodePattern.matcher(row);
                if (nodeMatcher.find()) {
                    final String nodeName = nodeMatcher.group(1);
                    final String left = nodeMatcher.group(2);
                    final String right = nodeMatcher.group(3);
                    nodes.put(nodeName, new Node(nodeName, left, right));
                }
            }

            return nodes;
        }

        public String apply(final Instruction instruction) {
            return switch (instruction) {
                case L -> left;
                case R -> right;
            };
        }
    }

    enum Instruction {
        L, R;

        static List<Instruction> list(final String row) {
            return row.chars().mapToObj(c -> Instruction.valueOf(String.valueOf((char) c))).toList();
        }
    }
}
