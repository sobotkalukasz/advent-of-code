package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2020/day/13
 * */
public class ShuttleSearch {

    public int find(int timestamp, List<Integer> buses){
        Map<Integer, Integer> collect = buses.stream().collect(Collectors.toMap(bus -> bus - timestamp % bus, Function.identity(), Integer::compareTo));
        Integer min = collect.keySet().stream().min(Integer::compareTo).orElse(0);
        return collect.get(min)*min;
    }

    public long findTimestamp(List<String> buses){
        Map<Long, Long> busMap = IntStream.range(0, buses.size()).boxed()
                .collect(Collectors.toMap(Function.identity(), buses::get))
                .entrySet().stream().filter(e -> !e.getValue().equals("x"))
                .collect(Collectors.toMap(e -> Long.valueOf(e.getKey()),
                        e -> Long.valueOf(e.getValue()),
                        (k1, k2) -> k1, LinkedHashMap::new));

        long timestamp = 0;
        long lcm = 0;
        Iterator<Map.Entry<Long, Long>> iterator = busMap.entrySet().iterator();
        List<Long> values = new ArrayList<>();

        while(iterator.hasNext()){
            Map.Entry<Long, Long> next = iterator.next();
            values.add(next.getValue());
            if(values.size() == 2){
                timestamp = findFirstCommon(values.get(0), next);
            }else if(values.size() > 2) {
                do {
                    timestamp += lcm;
                }while((timestamp + next.getKey())% next.getValue() != 0);
            }
            lcm = values.stream().mapToLong(Long::longValue).reduce((a,b) -> a*b).getAsLong();
        }
        return timestamp;
    }

    private long findFirstCommon(long first, Map.Entry<Long, Long> next){
        for(int i = 1;; i++){
            if ((first * i + next.getKey())%next.getValue() == 0)
                return first * i;
        }
    }

}
