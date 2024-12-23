package pl.lsobotka.adventofcode.year_2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LanParty {

    private final Map<String, Set<String>> connection;

    public LanParty(List<String> lines) {
        this.connection = new HashMap<>();

        for (String line : lines) {
            String[] split = line.split("-");
            connection.computeIfAbsent(split[0], k -> new HashSet<>()).add(split[1]);
            connection.computeIfAbsent(split[1], k -> new HashSet<>()).add(split[0]);
        }
    }

    long countConnected() {
        final Set<Set<String>> connected = new HashSet<>();
        for (String first : connection.keySet()) {
            final Set<String> connectedToFirst = connection.get(first);
            for (String second : connectedToFirst) {
                final Set<String> connectedToSecond = connection.get(second);
                for (String third : connectedToSecond) {
                    final Set<String> connectedToThird = connection.get(third);
                    if (connectedToThird.contains(first)) {
                        connected.add(Set.of(first, second, third));
                    }
                }
            }
        }
        return connected.stream().filter(set -> set.stream().anyMatch(s -> s.startsWith("t"))).count();

    }
}
