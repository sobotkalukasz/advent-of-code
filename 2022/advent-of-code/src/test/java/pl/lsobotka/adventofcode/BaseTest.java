package pl.lsobotka.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
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

    public String getFileInputSingleLine(String fileName) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(RESOURCE_PATH.concat(fileName)));
        List<String> input = bufferedReader.lines().toList();
        bufferedReader.close();
        return input.get(0);
    }
}