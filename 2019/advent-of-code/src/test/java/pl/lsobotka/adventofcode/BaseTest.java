package pl.lsobotka.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTest {

    private static final String RESOURCE_PATH = "src/test/resources/";

    public List<String> getFileInput(String fileName) {
        try (FileReader fileReader = new FileReader(RESOURCE_PATH.concat(fileName))) {
            return new BufferedReader(fileReader).lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Can not read file " + fileName, e);
        }
    }
}
