package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.List;

/*
 * https://adventofcode.com/2020/day/9
 * */
public class EncodingError {

    public long findInvalidNumber(List<Long> input, int preambleSize){
        long invalid = 0;
        for(int i = preambleSize; i < input.size() || invalid == 0; i++){
            List<Long> preamble = input.subList(i - preambleSize, i);
            if(!isValidNumber(input.get(i), preamble))
                invalid = input.get(i);
        }
        return invalid;
    }

    private boolean isValidNumber(long toTest, List<Long> preamble){
        Long max = preamble.stream().max(Long::compareTo).orElse(0L);
        if(toTest >= max*2)
            return false;
        for(int i = 0; i < preamble.size() -1; i++){
            long first = preamble.get(i);
            for(int j = i+1; j< preamble.size(); j++){
                long second = preamble.get(j);
                if (first + second == toTest)
                    return true;
            }
        }
        return false;
    }

    public List<Long> findSumOfInvalidNumber(List<Long> input, int preambleSize){
        long invalid = findInvalidNumber(input, preambleSize);
        input.remove(invalid);

        List<Long> numbers = new ArrayList<>();

        for(int i = 0; i<input.size(); i++){
            long sum = input.get(i);
            for(int j = i+1; j<input.size(); j++){
                sum += input.get(j);
                if(sum == invalid){
                    numbers = input.subList(i, j+1);
                    break;
                }
                if(sum > invalid){
                    break;
                }
            }
            if(!numbers.isEmpty())
                break;
        }

        return numbers;
    }
}
