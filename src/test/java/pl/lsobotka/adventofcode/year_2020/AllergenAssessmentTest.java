package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AllergenAssessmentTest extends BaseTest {

    private static Stream<Arguments> allergen() {
        return Stream.of(Arguments.of("2020/AllergenAssessment_example", 5)

        );
    }

    @ParameterizedTest
    @MethodSource("allergen")
    void allergenTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        AllergenAssessment aa = new AllergenAssessment(input);
        long actual = aa.countTimes();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenFile() {
        return Stream.of(Arguments.of("2020/AllergenAssessment", 2374));
    }

    @ParameterizedTest
    @MethodSource("allergenFile")
    void allergenFileTest(String fileName, int expected) {
        List<String> input = getFileInput(fileName);

        AllergenAssessment aa = new AllergenAssessment(input);
        long actual = aa.countTimes();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenIngredients() {
        return Stream.of(Arguments.of("2020/AllergenAssessment_example", "mxmxvkd,sqjhc,fvjkl")

        );
    }

    @ParameterizedTest
    @MethodSource("allergenIngredients")
    public void allergenIngredientsTest(String fileName, String expected) throws Exception {
        List<String> input = getFileInput(fileName);

        AllergenAssessment aa = new AllergenAssessment(input);
        String actual = aa.getAllergenicIngredients();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> allergenIngredientsFile() {
        return Stream.of(
                Arguments.of("2020/AllergenAssessment", "fbtqkzc,jbbsjh,cpttmnv,ccrbr,tdmqcl,vnjxjg,nlph,mzqjxq"));
    }

    @ParameterizedTest
    @MethodSource("allergenIngredientsFile")
    public void allergenIngredientsFileTest(String fileName, String expected) throws Exception {
        List<String> input = getFileInput(fileName);

        AllergenAssessment aa = new AllergenAssessment(input);
        String actual = aa.getAllergenicIngredients();
        assertEquals(expected, actual);
    }
}
