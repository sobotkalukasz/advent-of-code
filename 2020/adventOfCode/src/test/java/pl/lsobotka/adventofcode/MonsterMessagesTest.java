package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * https://adventofcode.com/2020/day/19
 * */
public class MonsterMessagesTest extends BaseTest {

    private static Stream<Arguments> monsterData() {
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("0: 4 1 5", "1: 2 3 | 3 2", "2: 4 4 | 5 5", "3: 4 5 | 5 4", "4: \"a\"", "5: \"b\"", "",
                        "ababbb", "bababa", "abbbab", "aaabbb", "aaaabbb")), 2)
        );
    }

    @ParameterizedTest
    @MethodSource("monsterData")
    public void mathDataTest(List<String> input, int expected) {

        MonsterMessages regexp = new MonsterMessages();
        int actual = regexp.validateData(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> monsterDataFile() {
        return Stream.of(
                Arguments.of("MonsterMessages", 230)
        );
    }

    @ParameterizedTest
    @MethodSource("monsterDataFile")
    public void monsterDataFileTest(String fileName, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        MonsterMessages regexp = new MonsterMessages();
        int actual = regexp.validateData(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> monsterDataImprovedFile() {
        return Stream.of(
                Arguments.of("MonsterMessages", 341)
        );
    }

    @ParameterizedTest
    @MethodSource("monsterDataImprovedFile")
    public void monsterDataImprovedFileTest(String fileName, long expected) throws Exception {
        List<String> input = getFileInput(fileName);

        MonsterMessages regexp = new MonsterMessages();
        int actual = regexp.validateDataFixedRule(input);
        assertEquals(expected, actual);
    }
}
