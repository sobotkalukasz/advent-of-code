package pl.lsobotka.adventofcode;

/*
 * https://adventofcode.com/2020/day/1
 * */

public class ReportRepair {

    public record IntPair(Integer first, Integer second){}
    public record IntTrio(Integer first, Integer second, Integer third){}

    //DayOneA
    public IntPair findPairOfSum(int sum, int[] numbers) {
        for(int i = 0; i < numbers.length-1; i++){
            for (int j = i+1; j < numbers.length; j++){
                if(numbers[i] + numbers[j] == sum){
                    return new IntPair(numbers[i], numbers[j]);
                }
            }
        }
        return null;
    }

    //DayOneB
    public IntTrio findTrioOfSum(int sum, int[] numbers) {
        for(int i = 0; i < numbers.length-2; i++){
            for (int j = i+1; j < numbers.length-1; j++){
                for (int k = i+1; k < numbers.length; k++){
                    if(numbers[i] + numbers[j] + numbers[k] == sum){
                        return new IntTrio(numbers[i], numbers[j], numbers[k]);
                    }
                }
            }
        }
        return null;
    }
}
