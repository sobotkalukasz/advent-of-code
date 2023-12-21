package pl.lsobotka.adventofcode.year_2020;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2020/day/16
 * */
public class TicketTranslation {

    private final static String YOUR_TICKET = "your ticket";
    private final static String NEARBY_TICKETS = "nearby tickets";
    private final static String OR = "or";

    Map<String, List<Integer>> validNumbersMap;
    List<Integer> validNumbers;
    List<Integer> ticket;
    List<List<Integer>> nearbyTickets;

    public TicketTranslation(List<String> data) {
        initData(data);
    }

    public long findErrorRate() {
        return nearbyTickets.stream().flatMap(List::stream).filter(num -> !validNumbers.contains(num)).reduce(Integer::sum).orElse(0);
    }

    public long findFieldValue(String field) {
        nearbyTickets = nearbyTickets.stream().filter(list -> validNumbers.containsAll(list)).collect(Collectors.toList());
        Map<Integer, List<String>> indexKeyMap = new HashMap<>();
        for (int i = 0; i < ticket.size(); i++) {
            int index = i;
            List<Integer> indexValues = nearbyTickets.stream().map(list -> list.get(index)).collect(Collectors.toList());
            List<Map.Entry<String, List<Integer>>> collect = validNumbersMap.entrySet().stream().filter(e -> e.getValue().containsAll(indexValues)).collect(Collectors.toList());
            indexKeyMap.put(index, collect.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        }

        return mapIndexToKey(indexKeyMap).entrySet().stream()
                .filter(e -> e.getKey().contains(field))
                .mapToLong(e -> ticket.get(e.getValue()))
                .reduce(Math::multiplyExact)
                .getAsLong();
    }

    private void initData(List<String> data) {
        validNumbersMap = new LinkedHashMap<>();
        ticket = new ArrayList<>();
        nearbyTickets = new ArrayList<>();
        String current = "";

        for (String row : data) {
            if (row.isEmpty()) continue;
            if (row.contains(":")) {
                if (row.contains(NEARBY_TICKETS)) {
                    current = NEARBY_TICKETS;
                } else if (row.contains(YOUR_TICKET)) {
                    current = YOUR_TICKET;
                } else {
                    addValidNumbers(row);
                }
            } else if (current.equals(YOUR_TICKET)) {
                ticket.addAll(getTicketNumbers(row));
            } else if (current.equals(NEARBY_TICKETS)) {
                nearbyTickets.add(getTicketNumbers(row));
            }
        }
        validNumbers = validNumbersMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    private List<Integer> getTicketNumbers(String row) {
        return Arrays.stream(row.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    private void addValidNumbers(String row) {
        String[] rowArr = row.split(":");
        String[] numbers = rowArr[1].replaceAll(" ", "").split(OR);

        List<Integer> collect = Arrays.stream(numbers).flatMapToInt(num -> {
            String[] split = num.split("-");
            return IntStream.rangeClosed(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }).boxed().collect(Collectors.toList());
        validNumbersMap.put(rowArr[0], collect);
    }

    private Map<String, Integer> mapIndexToKey(Map<Integer, List<String>> indexKeyMap) {
        Map<String, Integer> ticketMap = new HashMap<>();

        while (!indexKeyMap.isEmpty()) {
            List<Map.Entry<Integer, List<String>>> oneSize = indexKeyMap.entrySet().stream().filter(e -> e.getValue().size() == 1).collect(Collectors.toList());
            oneSize.forEach(e -> {
                String key = e.getValue().get(0);
                ticketMap.put(key, e.getKey());
                indexKeyMap.remove(e.getKey());
                indexKeyMap.forEach((key1, value) -> value.remove(key));
            });

        }
        return ticketMap;
    }

}
