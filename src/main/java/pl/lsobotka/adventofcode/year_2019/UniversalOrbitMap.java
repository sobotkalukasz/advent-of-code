package pl.lsobotka.adventofcode.year_2019;

import java.util.*;

/*
 * https://adventofcode.com/2019/day/6
 * */
public class UniversalOrbitMap {

    private Set<Orbit> orbits;

    public UniversalOrbitMap(List<String> input) {
        createOrbitMap(input);
    }

    private void createOrbitMap(List<String> input) {
        orbits = new HashSet<>();
        for (String s : input) {
            String[] split = s.split("\\)");
            Orbit orbitA = findByName(split[0]);
            Orbit orbitB = findByName(split[1]);
            orbitB.setParent(orbitA);
            orbits.add(orbitA);
            orbits.add(orbitB);
        }
    }

    private Orbit findByName(String name) {
        return orbits.stream().filter(o -> o.getName().equals(name)).findFirst().orElseGet(() -> new Orbit(name));
    }

    public int countOrbits() {
        return orbits.stream().map(this::countOrbits).reduce(Integer::sum).orElse(0);
    }

    private int countOrbits(Orbit orbit) {
        return countOrbits(orbit, null);
    }

    private int countOrbits(Orbit from, Orbit to) {
        int orbitCount = 0;
        Orbit parent = from.getParent();
        while (Objects.nonNull(parent) && !parent.equals(to)) {
            orbitCount++;
            parent = parent.getParent();
        }
        return orbitCount;
    }

    public int countOrbitalTransfer(String from, String to) {
        Orbit fromOrbit = findByName(from);
        Orbit toOrbit = findByName(to);
        Orbit common = findCommonOrbit(fromOrbit, toOrbit);
        return countOrbits(fromOrbit, common) + countOrbits(toOrbit, common);
    }

    private Orbit findCommonOrbit(Orbit from, Orbit to) {
        Orbit orbit = to;
        Orbit common = null;
        while (Objects.nonNull(orbit) && Objects.isNull(common)) {
            if (from.equals(orbit)) common = orbit;
            orbit = orbit.getParent();
        }
        return Objects.nonNull(common) ? common : findCommonOrbit(from.getParent(), to);
    }

    private static class Orbit {

        private final String name;
        private Orbit parent;

        public Orbit(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Orbit getParent() {
            return parent;
        }

        public void setParent(Orbit parent) {
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Orbit orbit = (Orbit) o;
            return Objects.equals(name, orbit.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
