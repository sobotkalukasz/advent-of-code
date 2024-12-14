package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.lsobotka.adventofcode.utils.Coord;

/*
 * https://adventofcode.com/2024/day/13
 * */
public class ClawContraption {
    private static final Pattern pattern = Pattern.compile(
            "Button A: X\\+(\\d+), Y\\+(\\d+)\\s+" + "Button B: X\\+(\\d+), Y\\+(\\d+)\\s+"
                    + "Prize: X=(\\d+), Y=(\\d+)");

    private final List<ClawMachine> clawMachines;

    public ClawContraption(String input) {
        clawMachines = new ArrayList<>();
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Button buttonA = new Button("A",
                    new Coord(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))), 3);
            Button buttonB = new Button("B",
                    new Coord(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))), 1);
            Prize prize = new Prize(new Coord(Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))));

            clawMachines.add(new ClawMachine(buttonA, buttonB, prize));
        }

    }

    long tokenCost() {
        return clawMachines.stream().mapToLong(this::tokenCost).sum();
    }

    long tokenCostWithoutLimit() {
        return clawMachines.stream().mapToLong(this::tokenCostWithoutLimit).sum();
    }

    private long tokenCost(final ClawMachine clawMachine) {
        final Queue<Path> paths = new PriorityQueue<>(Comparator.comparingLong(p -> p.cost() + p.heuristic()));
        paths.add(Path.empty());
        final Map<Coord, Long> costCache = new HashMap<>();
        final Coord prize = clawMachine.prize().coord();

        long cost = 0;
        while (!paths.isEmpty()) {
            final Path current = paths.poll();
            if (current.a() < 100 && current.b() < 100) {
                if (current.coord().equals(prize)) {
                    cost = current.cost();
                    break;
                }
                if (current.coord.row() < prize.row() && current.coord.col() < prize.col()
                        && costCache.getOrDefault(current.coord(), Long.MAX_VALUE) > current.cost()) {
                    costCache.put(current.coord(), current.cost());
                    paths.add(current.moveA(clawMachine.a(), prize));
                    paths.add(current.moveB(clawMachine.b(), prize));
                }
            }
        }

        return cost;
    }

    private long tokenCostWithoutLimit(final ClawMachine clawMachine) {
        long aRow = clawMachine.a().moveBy().row();
        long aCol = clawMachine.a().moveBy().col();

        long bRow = clawMachine.b().moveBy().row();
        long bCol = clawMachine.b().moveBy().col();

        long priceRow = clawMachine.prize().coord().row() + 10_000_000_000_000L;
        long priceCol = clawMachine.prize().coord().col() + 10_000_000_000_000L;

        double determinant = (double) aRow * bCol - aCol * bRow;
        double potentialA = (priceRow * bCol - priceCol * bRow) / determinant;
        double potentialB = (aRow * priceCol - aCol * priceRow) / determinant;

        if (isNotInteger(potentialA) || isNotInteger(potentialB)) {
            return 0;
        }

        long a = Math.round(potentialA);
        long b = Math.round(potentialB);

        return a * clawMachine.a().cost() + b * clawMachine.b().cost();
    }

    private boolean isNotInteger(double value) {
        return value - Math.round(value) != 0;
    }

    record Button(String name, Coord moveBy, int cost) {
    }

    record Prize(Coord coord) {
    }

    record ClawMachine(Button a, Button b, Prize prize) {
    }

    record Path(Coord coord, long a, long b, long cost, long heuristic) {

        static Path empty() {
            return new Path(new Coord(0, 0), 0, 0, 0, 0);
        }

        Path moveA(final Button button, final Coord target) {
            return new Path(coord.moveBy(button.moveBy()), a + 1, b, cost + button.cost(), heuristic(target));
        }

        Path moveB(final Button button, final Coord target) {
            return new Path(coord.moveBy(button.moveBy()), a, b + 1, cost + button.cost(), heuristic(target));
        }

        private int heuristic(Coord target) {
            return Math.abs(coord.row() - target.row()) + Math.abs(coord.col() - target.col());
        }
    }

}
