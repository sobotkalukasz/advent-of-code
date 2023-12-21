package pl.lsobotka.adventofcode.year_2020;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/10
 * */
public class AdapterArray {

    private final static int[] PERMUTATIONS = {1,1,1,2,4,7};

    record JoltRecord(int oneJoltCount, int threeJoltCount){}

    public JoltRecord findChain(List<Integer> adapters){
        adapters = prepareData(adapters);

        AtomicInteger one = new AtomicInteger();
        AtomicInteger three = new AtomicInteger();

        adapters.stream().reduce((a,b) -> {
            if (b-a == 1) one.incrementAndGet();
            if (b-a == 3) three.incrementAndGet();
            return b;
        });
        return new JoltRecord(one.get(), three.get());
    }

    public long countChains(List<Integer> adapters){
        adapters = prepareData(adapters);

        long total = 1;
        int curLen = 0;

        for(int i = 0; i < adapters.size()-1; i++){
            curLen++;
            int delta = adapters.get(i+1) - adapters.get(i);
            if(delta == 3){
                total *= PERMUTATIONS[curLen];
                curLen = 0;
            }
        }
        return total;
    }

    private List<Integer> prepareData(List<Integer> adapters){
        adapters.add(0);
        Integer max = adapters.stream().max(Integer::compareTo).orElse(0);
        adapters.add(max+3);
        return adapters.stream().sorted().collect(Collectors.toList());
    }

}
