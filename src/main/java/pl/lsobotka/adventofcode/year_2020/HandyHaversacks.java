package pl.lsobotka.adventofcode.year_2020;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/7
 * */

public class HandyHaversacks {

    private static final Pattern CONTAINS = Pattern.compile("contain");
    private static final Pattern NO_OTHER = Pattern.compile("no other bags");

    private Set<Bag> bags = new HashSet<>();

    public long countHowManyCanContainType(String type, Set<Bag> givenBags){
        bags = givenBags;
        return countHowManyCanContain(type, new HashSet<>()).size();
    }

    private Set<String> countHowManyCanContain(String type, Set<String> possible){
        Bag bagByType = bags.stream().filter(bag -> bag.type.equals(type)).findFirst().orElseGet(() -> new Bag(type));
        possible.addAll(bagByType.canBeHoldDirectly);
        bagByType.canBeHoldDirectly.forEach(bagType -> countHowManyCanContain(bagType, possible));
        return possible;
    }

    public long countRequiredInside(String type, Set<Bag> givenBags){
        bags = givenBags;
        return countRequiredInside(type);
    }

    private long countRequiredInside(String type){
        Bag bagByType = bags.stream().filter(bag -> bag.type.equals(type)).findFirst().orElseGet(() -> new Bag(type));
        Map<String, Long> holdingBags = bagByType.holdingBags;
        long count = holdingBags.values().stream().reduce(Long::sum).orElse(0L);
        count += holdingBags.keySet().stream().mapToLong(key -> holdingBags.get(key) * countRequiredInside(key)).sum();
        return count;
    }

    public Set<Bag> createBagFromRules(List<String> rawInput){
        bags = new HashSet<>();
        rawInput.forEach(this::putBag);
        return bags;
    }

    private void putBag(String rule) {
        String[] split = CONTAINS.split(rule);
        String mainBagType = split[0];
        Bag mainBag = getBagByType(mainBagType.replace("bags", "").trim());

        String otherBags = split[1];
        if(isBagCanHoldOtherBags(otherBags)){
            Map<String, Long> canHoldBags = createMapOfCanHoldBags(otherBags);
            mainBag.holdingBags.putAll(canHoldBags);

            canHoldBags.keySet().forEach(bagType -> {
                Bag bagByType = getBagByType(bagType);
                bagByType.canBeHoldDirectly.add(mainBag.type);
                addOrReplaceBag(bagByType);
            });
        }
        addOrReplaceBag(mainBag);
    }

    private Bag getBagByType(String type){
        return bags.stream().filter(bag -> bag.type.equals(type)).findFirst().orElseGet(() -> new Bag(type));
    }

    private boolean isBagCanHoldOtherBags(String test){
        return !test.contains(NO_OTHER.pattern());
    }

    private Map<String, Long> createMapOfCanHoldBags(String rule) {
        Map<String, Long> canHoldBags = new HashMap<>();
        String[] split = rule.replace("bags", "").replace("bag", "").replace(".", "").split(",");
        Arrays.asList(split).forEach(bagRule -> {
            bagRule = bagRule.trim();
            Long value = Long.valueOf(String.valueOf(bagRule.charAt(0)));
            String key = bagRule.substring(2);
            canHoldBags.put(key, value);
        });
        return canHoldBags;
    }

    private void addOrReplaceBag(Bag bag){
        if(!bags.add(bag)){
            bags.remove(bag);
            bags.add(bag);
        }
    }

    static class Bag {
        private final String type;
        private final Map<String, Long> holdingBags;
        private final Set<String> canBeHoldDirectly;

        public Bag(String type){
            this.type = type;
            holdingBags = new HashMap<>();
            canBeHoldDirectly = new HashSet<>();
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Bag bag = (Bag) o;
            return type.equals(bag.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }

        @Override
        public String toString() {
            return "Bag{" +
                    "type='" + type + '\'' +
                    ", holdingBags=" + holdingBags +
                    ", canBeHoldDirectly=" + canBeHoldDirectly +
                    '}';
        }
    }

    static class BagGraph {
        private final String type;
        private final Set<BagGraph> canBeHoldDirectly;

        public BagGraph(String type) {
            this.type = type;
            canBeHoldDirectly = new HashSet<>();
        }

        public void print() {
            print(this, 0, 0);
        }

        private void print(BagGraph node, int level, int space){
            String spaces = " ".repeat(space);
            String lines = level != 0 ? "-".repeat(6) : "";
            System.out.printf("%s[%d]|%s %s%n", spaces,level, lines, node.type);
            node.canBeHoldDirectly.forEach(temp -> print(temp, level+1, space +4));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BagGraph bag = (BagGraph) o;
            return type.equals(bag.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }

        @Override
        public String toString() {
            return "BagGraph{" +
                    "type='" + type + '\'' +
                    ", canBeHoldDirectly=" + canBeHoldDirectly +
                    '}';
        }
    }

    public BagGraph createGraph(String type, Set<Bag> givenBags){
        bags = givenBags;
        BagGraph top = new BagGraph(type);
        Bag bagByType = bags.stream().filter(bag -> bag.type.equals(type)).findFirst().orElseGet(() -> new Bag(type));
        Set<BagGraph> collect = bagByType.canBeHoldDirectly.stream().map(BagGraph::new).collect(Collectors.toSet());
        top.canBeHoldDirectly.addAll(collect);
        top.canBeHoldDirectly.forEach(this::createGraphNode);
        return top;
    }

    private void createGraphNode(BagGraph bagGraph){
        Bag tempBag = bags.stream().filter(bag -> bag.type.equals(bagGraph.type)).findFirst().orElseGet(() -> new Bag(bagGraph.type));
        Set<BagGraph> collect = tempBag.canBeHoldDirectly.stream().map(BagGraph::new).collect(Collectors.toSet());
        bagGraph.canBeHoldDirectly.addAll(collect);
        bagGraph.canBeHoldDirectly.forEach(this::createGraphNode);
    }
}


