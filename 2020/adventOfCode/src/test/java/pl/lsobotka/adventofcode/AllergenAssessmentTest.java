package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllergenAssessmentTest {

    private static Stream<Arguments> allergen() {
        return Stream.of(
                Arguments.of(Arrays.asList("mxmxvkd kfcds sqjhc nhms (contains dairy, fish)", "trh fvjkl sbzzf mxmxvkd (contains dairy)", "sqjhc fvjkl (contains soy)", "sqjhc mxmxvkd sbzzf (contains fish)"), 5)

        );
    }

    @ParameterizedTest
    @MethodSource("allergen")
    public void allergenTest(List<String> input, int expected) {

        AllergenAssessment aa = new AllergenAssessment(input);
        long actual = aa.countTimes();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenFile() {
        return Stream.of(
                Arguments.of("src/test/resources/AllergenAssessment", 2374)
        );
    }

    @ParameterizedTest
    @MethodSource("allergenFile")
    public void allergenFileTest(String path, int expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        AllergenAssessment aa = new AllergenAssessment(input);
        long actual = aa.countTimes();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenIngredients() {
        return Stream.of(
                Arguments.of(Arrays.asList("mxmxvkd kfcds sqjhc nhms (contains dairy, fish)", "trh fvjkl sbzzf mxmxvkd (contains dairy)", "sqjhc fvjkl (contains soy)", "sqjhc mxmxvkd sbzzf (contains fish)"), "mxmxvkd,sqjhc,fvjkl")

        );
    }

    @ParameterizedTest
    @MethodSource("allergenIngredients")
    public void allergenIngredientsTest(List<String> input, String expected) throws IOException {

        AllergenAssessment aa = new AllergenAssessment(input);
        String actual = aa.getAllergenicIngredients();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenIngredientsFile() {
        return Stream.of(
                Arguments.of("src/test/resources/AllergenAssessment", "fbtqkzc,jbbsjh,cpttmnv,ccrbr,tdmqcl,vnjxjg,nlph,mzqjxq")
        );
    }

    @ParameterizedTest
    @MethodSource("allergenIngredientsFile")
    public void allergenIngredientsFileTest(String path, String expected) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> input = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        AllergenAssessment aa = new AllergenAssessment(input);
        String actual = aa.getAllergenicIngredients();
        assertEquals(expected, actual);
    }
}
