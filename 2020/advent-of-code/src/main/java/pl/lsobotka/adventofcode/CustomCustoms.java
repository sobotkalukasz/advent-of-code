package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/6
 * */

public class CustomCustoms {

    public interface AnswerStrategy {
        List<Character> apply (List<String> data);
    }

    public static AnswerStrategy UNIQUE = CustomCustoms::createListOfUniqueAnswers;
    public static AnswerStrategy SAME = CustomCustoms::createListOfSameAnswers;

    public long countAllAnswers(List<List<Character>> answers){
        return answers.stream().map(List::size).reduce(Integer::sum).orElse(0);
    }

    public List<List<Character>> mapRawDataAnswers(List<String> rawData, AnswerStrategy strategy){

        List<List<Character>> answers = new ArrayList<>();
        List<String> tempData = new ArrayList<>();

        Iterator<String> iter = rawData.iterator();

        while(iter.hasNext()){
            String row = iter.next();

            if(!row.isBlank()){
                tempData.add(row);
                if(iter.hasNext()){
                    continue;
                }
            }
            answers.add(strategy.apply(tempData));
            tempData.clear();
        }
        return answers;
    }

    private static List<Character> createListOfUniqueAnswers(List<String> tempData){
        return tempData.stream()
                .reduce((a, b) -> a + b)
                .map(String::chars)
                .stream().flatMap(ints -> ints.mapToObj(c -> (char) c))
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Character> createListOfSameAnswers(List<String> tempData){
        Map<Character, Long> collect = tempData.stream()
                .reduce((a, b) -> a + b)
                .map(String::chars)
                .stream().flatMap(ints -> ints.mapToObj(c -> (char) c))
                .sorted()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return collect.keySet().stream()
                .filter(key -> collect.get(key) == tempData.size())
                .collect(Collectors.toList());
    }
}
