package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassportProcessingTest extends BaseTest {

    private static final Pattern EMPTY_SPACE_PATTERN = Pattern.compile(" ");
    private static final Pattern ENTRY_PATTERN = Pattern.compile(":");

    private static Stream<Arguments> simpleData() {
        return Stream.of(
                Arguments.of("PassportProcessing", 210)
        );
    }

    @ParameterizedTest
    @MethodSource("simpleData")
    public void dayFourSimpleValidation(String fileName, int expectedValidPassports) throws Exception {
        List<Map<String, String>> passports = mapRawDataToPassports(getFileInput(fileName));

        PassportProcessing pass = new PassportProcessing();
        long validPassports = pass.countValidPassports(passports, PassportProcessing.simplePredicate);

        assertEquals(expectedValidPassports, validPassports);

    }

    private static Stream<Arguments> complexData() {
        return Stream.of(
                Arguments.of("PassportProcessing", 131)
        );
    }

    @ParameterizedTest
    @MethodSource("complexData")
    public void dayFourComplexValidation(String fileName, int expectedValidPassports) throws Exception {
        List<Map<String, String>> passports = mapRawDataToPassports(getFileInput(fileName));

        PassportProcessing pass = new PassportProcessing();
        long validPassports = pass.countValidPassports(passports, PassportProcessing.complexPredicate);

        assertEquals(expectedValidPassports, validPassports);

    }

    private List<Map<String, String>> mapRawDataToPassports(List<String> rawData) {

        List<Map<String, String>> passports = new ArrayList<>();
        List<String> tempData = new ArrayList<>();

        Iterator<String> iter = rawData.iterator();

        while (iter.hasNext()) {
            String row = iter.next();

            if (!row.isBlank()) {
                tempData.add(row);
                if (iter.hasNext()) {
                    continue;
                }
            }
            passports.add(createPassport(tempData));
            tempData.clear();
        }
        return passports;
    }

    private Map<String, String> createPassport(List<String> tempData) {
        return tempData.stream()
                .flatMap(EMPTY_SPACE_PATTERN::splitAsStream)
                .map(ENTRY_PATTERN::split)
                .collect(Collectors.toMap(x -> x[0], x -> x[1]));
    }

}
