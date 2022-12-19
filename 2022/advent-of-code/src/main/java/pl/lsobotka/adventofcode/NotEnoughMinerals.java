package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2022/day/19
 * */
public class NotEnoughMinerals {

    final List<Blueprint> blueprints;

    NotEnoughMinerals(final List<String> input) {
        blueprints = initBlueprints(input);
    }

    public int qualityLevel() {
        final Map<Integer, Integer> collect = blueprints.stream()
                .parallel()
                .collect(Collectors.toMap(b -> b.number, b -> determineGeodes(b, 24)));
        return collect.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue()).sum();
    }

    public int qualityLevelOfFirst(int amount) {
        final List<Blueprint> blueprints = this.blueprints.subList(0, amount);
        return blueprints.stream() //
                .parallel() //
                .mapToInt(b -> determineGeodes(b, 32)) //
                .reduce((a, b) -> a * b).orElse(0);
    }

    private int determineGeodes(final Blueprint blueprint, final int time) {
        final AtomicInteger geodeCount = new AtomicInteger(0);
        final Set<Cache> cache = new HashSet<>();

        final Path initPath = new Path(time, Minerals.empty(), Robots.of(Type.ORE), 0);
        final Queue<Path> paths = new PriorityQueue<>();
        paths.add(initPath);

        while (!paths.isEmpty()) {
            final Path path = paths.poll();
            final int currentMax = geodeCount.get();

            if (path.timeLeft == 0) {
                if (currentMax < path.minerals.geo) {
                    geodeCount.set(path.minerals.geo);
                }
                continue;
            }

            final List<Path> nextPaths = new ArrayList<>();

            for (Robot next : path.getPossibleToCreate(blueprint)) {
                final Minerals afterCreating = path.minerals.applyCost(next.cost);
                final Minerals afterCollecting = afterCreating.collect(path.robots);
                final Robots robots = path.robots.add(next.type);
                nextPaths.add(new Path(path.timeLeft - 1, afterCollecting, robots, 0));
            }

            if (path.waiting < 4) {
                final Minerals afterCollecting = path.minerals.collect(path.robots);
                final Path noRobotCreated = new Path(path.timeLeft - 1, afterCollecting, path.robots, path.waiting + 1);
                nextPaths.add(noRobotCreated);
            }

            for (Path next : nextPaths) {
                if (!cache.contains(next.cache()) && getMaxPossibleGeo(next) > currentMax) {
                    cache.add(next.cache());
                    paths.add(next);
                }
            }
        }

        return geodeCount.get();
    }

    private int getMaxPossibleGeo(final Path path) {
        int temp = path.minerals.geo + (path.robots.geo * path.timeLeft);
        for (int i = path.timeLeft; i > 0; i--) {
            temp += i;
        }
        return temp;
    }

    record Cache(int time, Minerals minerals, Robots robots) {

    }

    record Path(int timeLeft, Minerals minerals, Robots robots, int waiting) implements Comparable<Path> {

        List<Robot> getPossibleToCreate(final Blueprint blueprint) {
            return blueprint.robots.stream()
                    .filter(r -> shouldCreate(blueprint, r.type))
                    .filter(r -> r.cost.stream().allMatch(minerals::enough))
                    .collect(Collectors.toList());
        }

        Cache cache() {
            return new Cache(timeLeft, minerals, robots);
        }

        private boolean shouldCreate(final Blueprint blueprint, final Type toCreate) {
            return switch (toCreate) {
                case ORE -> blueprint.maxOre() + 2 > minerals.ore && blueprint.maxOre() > robots.ore;
                case CLAY -> blueprint.maxClay() + 4 > minerals.clay && blueprint.maxClay() > robots.clay;
                case OBSIDIAN -> blueprint.maxObs() + 3 > minerals.obs && blueprint.maxObs() > robots.obs;
                case GEODE -> true;
            };
        }

        @Override
        public int compareTo(Path o) {
            return Comparator.comparing(Path::compareBy).compare(o, this);
        }

        private int compareBy() {
            return robots.geo;
        }
    }

    record Minerals(int ore, int clay, int obs, int geo) {

        static Minerals empty() {
            return new Minerals(0, 0, 0, 0);
        }

        private boolean enough(final Cost cost) {
            int actual = switch (cost.type) {
                case ORE -> ore;
                case CLAY -> clay;
                case OBSIDIAN -> obs;
                case GEODE -> geo;
            };
            return actual >= cost.amount;
        }

        Minerals applyCost(final List<Cost> costs) {
            Minerals actual = this;
            for (Cost cost : costs) {
                actual = actual.applyCost(cost);
            }
            return actual;
        }

        private Minerals applyCost(final Cost cost) {
            return switch (cost.type) {
                case ORE -> new Minerals(ore - cost.amount, clay, obs, geo);
                case CLAY -> new Minerals(ore, clay - cost.amount, obs, geo);
                case OBSIDIAN -> new Minerals(ore, clay, obs - cost.amount, geo);
                case GEODE -> new Minerals(ore, clay, obs, geo - cost.amount);
            };
        }

        private Minerals collect(final Robots robots) {
            return new Minerals(this.ore + robots.ore, this.clay + robots.clay, this.obs + robots.obs,
                    this.geo + robots.geo);
        }
    }

    record Robots(int ore, int clay, int obs, int geo) {

        static Robots of(Type type) {
            return switch (type) {
                case ORE -> new Robots(1, 0, 0, 0);
                case CLAY -> new Robots(0, 1, 0, 0);
                case OBSIDIAN -> new Robots(0, 0, 1, 0);
                case GEODE -> new Robots(0, 0, 0, 1);
            };
        }

        Robots add(Type type) {
            return switch (type) {
                case ORE -> new Robots(ore + 1, clay, obs, geo);
                case CLAY -> new Robots(ore, clay + 1, obs, geo);
                case OBSIDIAN -> new Robots(ore, clay, obs + 1, geo);
                case GEODE -> new Robots(ore, clay, obs, geo + 1);
            };
        }
    }

    record Blueprint(int number, List<Robot> robots, int maxOre, int maxClay, int maxObs, int maxGeo) {

        static Blueprint of(int number, List<Robot> robots) {
            final List<Cost> allCosts = robots.stream().map(Robot::cost).flatMap(Collection::stream).toList();

            final int maxOre = allCosts.stream()
                    .filter(c -> c.type.equals(Type.ORE))
                    .mapToInt(Cost::amount)
                    .max()
                    .orElse(0);

            final int maxClay = allCosts.stream()
                    .filter(c -> c.type.equals(Type.CLAY))
                    .mapToInt(Cost::amount)
                    .max()
                    .orElse(0);

            final int maxObs = allCosts.stream()
                    .filter(c -> c.type.equals(Type.OBSIDIAN))
                    .mapToInt(Cost::amount)
                    .max()
                    .orElse(0);

            final int maxGeo = allCosts.stream()
                    .filter(c -> c.type.equals(Type.GEODE))
                    .mapToInt(Cost::amount)
                    .max()
                    .orElse(0);

            return new Blueprint(number, robots, maxOre, maxClay, maxObs, maxGeo);
        }
    }

    record Robot(Type type, List<Cost> cost) {
    }

    record Cost(Type type, int amount) {
    }

    enum Type {
        ORE, CLAY, OBSIDIAN, GEODE
    }

    private List<Blueprint> initBlueprints(List<String> input) {
        final List<Blueprint> blueprints = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            final String row = input.get(i);

            final int blueprintNumber = i + 1;
            final String[] robots = row.split(":")[1].split("\\.");

            final int oreRobotCostInOre = Integer.parseInt(
                    robots[0].replace("Each ore robot costs", "").replace("ore", "").replaceAll(" ", ""));
            final Robot oreRobot = new Robot(Type.ORE,
                    Collections.singletonList(new Cost(Type.ORE, oreRobotCostInOre)));

            final int clayRobotCostInOre = Integer.parseInt(
                    robots[1].replace("Each clay robot costs", "").replace("ore", "").replaceAll(" ", ""));
            final Robot clayRobot = new Robot(Type.CLAY,
                    Collections.singletonList(new Cost(Type.ORE, clayRobotCostInOre)));

            final String[] obsidianCost = robots[2].replace("Each obsidian robot costs", "")
                    .replace("ore", "")
                    .replace("clay", "")
                    .replaceAll(" ", "")
                    .split("and");
            final int obsidianRobotCostInOre = Integer.parseInt(obsidianCost[0]);
            final int obsidianRobotCostInClay = Integer.parseInt(obsidianCost[1]);
            final Robot obsidianRobot = new Robot(Type.OBSIDIAN,
                    List.of(new Cost(Type.ORE, obsidianRobotCostInOre), new Cost(Type.CLAY, obsidianRobotCostInClay)));

            final String[] geodeCost = robots[3].replace("Each geode robot costs", "")
                    .replace("ore", "")
                    .replace("obsidian", "")
                    .replaceAll(" ", "")
                    .split("and");
            final int geodeRobotCostInOre = Integer.parseInt(geodeCost[0]);
            final int geodeRobotCostInObsidian = Integer.parseInt(geodeCost[1]);
            final Robot geodeRobot = new Robot(Type.GEODE, List.of(new Cost(Type.ORE, geodeRobotCostInOre),
                    new Cost(Type.OBSIDIAN, geodeRobotCostInObsidian)));

            blueprints.add(Blueprint.of(blueprintNumber, List.of(geodeRobot, obsidianRobot, clayRobot, oreRobot)));
        }
        return blueprints;
    }

}
