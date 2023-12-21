package pl.lsobotka.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class BaseTest {
    private static final String RESOURCE_PATH = "src/test/resources/";

    public List<String> getFileInput(String fileName) {
        try (FileReader fileReader = new FileReader(RESOURCE_PATH.concat(fileName))) {
            return new BufferedReader(fileReader).lines().toList();
        } catch (Exception e) {
            throw new RuntimeException("Can not read file " + fileName, e);
        }
    }

    public String getFileInputSingleLine(String fileName) {
        try (FileReader fileReader = new FileReader(RESOURCE_PATH.concat(fileName))) {
            return new BufferedReader(fileReader).lines().toList().get(0);
        } catch (Exception e) {
            throw new RuntimeException("Can not read file " + fileName, e);
        }
    }

    public List<Integer> getFileInputAsInteger(String fileName) {
        try (FileReader fileReader = new FileReader(RESOURCE_PATH.concat(fileName))) {
            return new BufferedReader(fileReader).lines().map(Integer::valueOf).toList();
        } catch (Exception e) {
            throw new RuntimeException("Can not read file " + fileName, e);
        }
    }

}
