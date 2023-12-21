package pl.lsobotka.adventofcode.year_2020;

import java.util.List;
import java.util.function.Predicate;

/*
 * https://adventofcode.com/2020/day/2
 * */

public class PasswordPhilosophy {

    record TestData(int min, int max, char letter, String pass){}

    public static Predicate<TestData> countPredicate = testData -> {
        long count = testData.pass.chars().filter(c -> c == testData.letter).count();
        return count >= testData.min && count <= testData.max;
    };

    public static Predicate<TestData> indexPredicate = testData -> testData.pass().charAt(testData.min - 1) == testData.letter ^ testData.pass().charAt(testData.max - 1) == testData.letter;


    public TestData mapToTestData(String testData){
        String[] split = testData.split("-");
        int min = Integer.parseInt(split[0]);

        String[] s = split[1].split(" ");
        int max = Integer.parseInt(s[0]);

        char letter = s[1].replace(":", "").charAt(0);
        String pass = s[2];

        return new TestData(min, max, letter, pass);
    }

    public long countValidPasswords(List<TestData> passwords, Predicate<TestData> predicate){
        return passwords.stream().filter(predicate).count();
    }

}
