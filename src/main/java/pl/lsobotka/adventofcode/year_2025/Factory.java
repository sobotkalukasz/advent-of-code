package pl.lsobotka.adventofcode.year_2025;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.LinearExpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2025/day/10
 * */
public class Factory {

    final List<Machine> machines;

    Factory(final List<String> input) {
        machines = input.stream().map(Machine::of).toList();
    }

    long countButtonToggle() {
        return machines.stream().mapToLong(this::countButtonToggle).sum();
    }

    private long countButtonToggle(final Machine machine) {
        final HashMap<Integer, Integer> visited = new HashMap<>();

        final Queue<State> states = new PriorityQueue<>();
        states.addAll(machine.initialStates());

        int best = Integer.MAX_VALUE;

        while (!states.isEmpty()) {
            final State actual = states.poll();

            if (actual.steps >= best) {
                continue;
            }

            final int hash = actual.stateHash();
            if (visited.getOrDefault(hash, Integer.MAX_VALUE) <= actual.steps) {
                continue;
            }
            visited.put(hash, actual.steps);

            if (Arrays.equals(actual.lights, machine.lights)) {
                best = actual.steps;
            }

            for (int[] button : machine.buttons) {
                if (!Arrays.equals(actual.toPress, button)) {
                    states.add(actual.press(button));
                }
            }

        }
        return best;
    }

    long countButtonPress() {
        final MachineIlpSolver solver = new MachineIlpSolver();
        return machines.parallelStream().mapToLong(solver::solve).sum();
    }

    record State(int[] lights, int[] toPress, int steps) implements Comparable<State> {

        State press(final int[] next) {
            final int[] copy = Arrays.copyOf(lights, lights.length);
            Arrays.stream(toPress).forEach(i -> copy[i] = 1 - copy[i]);
            return new State(copy, next, steps + 1);
        }

        int stateHash() {
            int h = Arrays.hashCode(lights);
            h = 31 * h + Arrays.hashCode(toPress);
            return h;
        }

        @Override
        public int compareTo(State o) {
            return Comparator.comparingInt(State::steps).compare(this, o);
        }
    }

    record Machine(int[] lights, int[][] buttons, int[] requirements) {

        private static final Pattern LIGHTS_PATTERN = Pattern.compile("\\[([.#]+)\\]");
        private static final Pattern REQUIREMENTS_PATTERN = Pattern.compile(
                "\\{\\s*([0-9]+(?:\\s*,\\s*[0-9]+)*)\\s*\\}");
        private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((\\d+(?:\\s*,\\s*\\d+)*)\\)");

        static Machine of(final String input) {

            Matcher lm = LIGHTS_PATTERN.matcher(input);
            if (!lm.find()) {
                throw new IllegalArgumentException("No lights [] in: " + input);
            }
            String lightsStr = lm.group(1);
            final int[] lights = lightsStr.chars().map(ch -> ch == '#' ? 1 : 0).toArray();

            final List<int[]> buttonsList = new ArrayList<>();
            Matcher bm = BUTTON_PATTERN.matcher(input);
            while (bm.find()) {
                String buttonStr = bm.group(1);
                int[] nums = Arrays.stream(buttonStr.split(","))
                        .map(String::trim)
                        .filter(t -> !t.isEmpty())
                        .mapToInt(Integer::parseInt)
                        .toArray();
                buttonsList.add(nums);
            }

            final int[][] buttons = buttonsList.toArray(new int[0][]);

            Matcher rm = REQUIREMENTS_PATTERN.matcher(input);
            if (!rm.find()) {
                throw new IllegalArgumentException("No requirements [] in: " + input);
            }
            String reqStr = rm.group(1);
            final int[] requirements = Arrays.stream(reqStr.split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .toArray();

            return new Machine(lights, buttons, requirements);
        }

        List<State> initialStates() {
            return Arrays.stream(buttons).map(b -> new State(new int[lights.length], b, 0)).toList();
        }

    }

    /**
     * ILP-based solver for the "minimum number of button presses" problem defined by {@link Machine}.
     *
     * The class converts the problem into an Integer Linear Programming model solved with
     * Google OR-Tools (CP-SAT). Each button is represented as a variable x[j] indicating how
     * many times it is pressed. For every index i in the requirements vector, a constraint is
     * added to ensure that the total number of presses affecting i matches the required value.
     * The objective minimizes the total number of presses: minimize Σ x[j].
     *
     * The solve(...) method returns the minimal number of presses if a feasible/optimal
     * solution exists, or -1 otherwise.
     *
     * Note: This implementation and documentation were generated with assistance from an
     * AI model (ChatGPT – GPT-5.1 Thinking by OpenAI).
     */
    private static class MachineIlpSolver {

        static {
            Loader.loadNativeLibraries();
        }

        int solve(Machine machine) {
            int[][] buttons = machine.buttons();
            int[] req = machine.requirements();

            int n = req.length;
            int k = buttons.length;

            CpModel model = new CpModel();

            int maxReq = 0;
            for (int v : req) if (v > maxReq) maxReq = v;
            if (maxReq <= 0) return 0;

            IntVar[] x = new IntVar[k];
            for (int j = 0; j < k; j++) {
                x[j] = model.newIntVar(0, maxReq, "x_" + j);
            }

            for (int i = 0; i < n; i++) {
                final List<IntVar> vars = new ArrayList<>();
                for (int j = 0; j < k; j++) {
                    for (int idx : buttons[j]) {
                        if (idx == i) {
                            vars.add(x[j]);
                            break;
                        }
                    }
                }
                if (req[i] == 0) {
                    if (!vars.isEmpty()) {
                        model.addEquality(LinearExpr.sum(vars.toArray(new IntVar[0])), 0);
                    }
                } else {
                    if (vars.isEmpty()) {
                        return -1;
                    }
                    model.addEquality(LinearExpr.sum(vars.toArray(new IntVar[0])), req[i]);
                }
            }

            model.minimize(LinearExpr.sum(x));

            CpSolver solver = new CpSolver();
            solver.getParameters().setMaxTimeInSeconds(2.0);
            solver.getParameters().setNumSearchWorkers(1);
            solver.getParameters().setLogSearchProgress(false);

            CpSolverStatus status = solver.solve(model);

            if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
                int sum = 0;
                for (int j = 0; j < k; j++) {
                    sum += (int) solver.value(x[j]);
                }
                return sum;
            } else {
                return -1;
            }
        }
    }
}
