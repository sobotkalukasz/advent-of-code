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

    public HauntedWasteland(final List<String> input) {
        this.instructions = Instruction.list(input.get(0));
        this.nodes = Node.map(input);
    }

    public int solveIt() {

        String currentNode = START_NODE;
        int iterationCount = 0;

        while (!currentNode.equals(END_NODE)) {
            for (Instruction instruction : instructions) {
                currentNode = nodes.get(currentNode).apply(instruction);
            }
            iterationCount++;
        }

        return iterationCount * instructions.size();
    }

    record Node(String node, String left, String right) {
        private final static Pattern nodePattern = Pattern.compile("([A-Z]{3})\\s=\\s\\(([A-Z]{3}),\\s([A-Z]{3})\\)");

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
