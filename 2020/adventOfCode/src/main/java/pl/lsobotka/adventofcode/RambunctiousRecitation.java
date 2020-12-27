package pl.lsobotka.adventofcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2020/day/15
 * */
public class RambunctiousRecitation {

    private Map<Long, long[]> numbers;

    public long findNumber(long spoken, List<Long> starting){
        init(starting);

        long previous = starting.get(starting.size()-1);
        long round = starting.size();
        while(round++ != spoken){
            long current = getCurrentNumber(previous);
            merge(current, round);
            previous = current;
        }
        return previous;
    }

    private void init(List<Long> starting){
        numbers = new HashMap<>();
        for(int i = 0; i < starting.size(); i++){
            numbers.put(starting.get(i), new long[]{i+1L});
        }
    }

    private long getCurrentNumber(long key){
        long[] previousArray = numbers.getOrDefault(key, new long[0]);
        return previousArray.length <= 1 ? 0 : previousArray[1] - previousArray[0];
    }

    private void merge(long key, long value){
        long[] current = numbers.getOrDefault(key, new long[]{});
        int length = current.length;
        if(length == 2){
            current[0] = current[1];
            current[1] = value;
        }
        else if(length == 1){
            current = new long[]{current[0], value};
        } else{
            current = new long[]{value};
        }
        numbers.put(key, current);
    }

}
