package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PasswordPhilosophyTest extends BaseTest {

    @ParameterizedTest
    @MethodSource("dataA")
    public void dayTwoAFile(String fileName, long expected) throws Exception {
        PasswordPhilosophy passwordPhilosophy = new PasswordPhilosophy();
        List<PasswordPhilosophy.TestData> testDataList = getFileInput(fileName).stream().map(passwordPhilosophy::mapToTestData).collect(Collectors.toList());
        long output = passwordPhilosophy.countValidPasswords(testDataList, PasswordPhilosophy.countPredicate);
        assertEquals(expected, output);
    }

    private static Stream<Arguments> dataA() {
        return Stream.of(
                Arguments.of("PasswordPhilosophy", 439)
        );
    }

    @ParameterizedTest
    @MethodSource("dataB")
    public void dayTwoBFile(String fileName, long expected) throws Exception {
        PasswordPhilosophy passwordPhilosophy = new PasswordPhilosophy();
        List<PasswordPhilosophy.TestData> testDataList = getFileInput(fileName).stream().map(passwordPhilosophy::mapToTestData).collect(Collectors.toList());
        long output = passwordPhilosophy.countValidPasswords(testDataList, PasswordPhilosophy.indexPredicate);
        assertEquals(expected, output);
    }

    private static Stream<Arguments> dataB() {
        return Stream.of(
                Arguments.of("PasswordPhilosophy", 584)
        );
    }
}
