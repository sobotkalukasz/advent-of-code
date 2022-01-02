package pl.lsobotka.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTest {

    private static final String RESOURCE_PATH = "src/test/resources/";

    public List<String> getFileInput(String fileName) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(RESOURCE_PATH.concat(fileName)));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();
        return input;
    }

    public List<Integer> getFileInputAsIntegerList(String fileName) throws Exception {
        return getFileInput(fileName).stream()
                .reduce(String::concat)
                .map(input -> input.split(","))
                .map(arr -> Arrays.stream(arr).map(Integer::valueOf).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
