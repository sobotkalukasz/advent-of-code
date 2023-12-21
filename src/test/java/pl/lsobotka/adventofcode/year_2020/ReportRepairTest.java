package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportRepairTest extends BaseTest {

    @ParameterizedTest
    @MethodSource("dayOneAData")
    void dayOneFromFile(int sum, String fileName, ReportRepair.IntPair expected) {
        List<Integer> collect = getFileInput(fileName).stream().map(Integer::valueOf).toList();

        ReportRepair report = new ReportRepair();
        ReportRepair.IntPair output = report.findPairOfSum(sum, collect.stream().mapToInt(Integer::intValue).toArray());

        assertEquals(expected, output);
    }

    private static Stream<Arguments> dayOneAData() {
        return Stream.of(Arguments.of(2020, "2020/ReportRepair", new ReportRepair.IntPair(437, 1583)));
    }

    @ParameterizedTest
    @MethodSource("dayOneBData")
    void dayTwoFromFile(int sum, String fileName, ReportRepair.IntTrio expected) {
        List<Integer> collect = getFileInput(fileName).stream().map(Integer::valueOf).toList();

        ReportRepair report = new ReportRepair();
        ReportRepair.IntTrio output = report.findTrioOfSum(sum, collect.stream().mapToInt(Integer::intValue).toArray());

        assertEquals(expected, output);

    }

    private static Stream<Arguments> dayOneBData() {
        return Stream.of(Arguments.of(2020, "2020/ReportRepair", new ReportRepair.IntTrio(717, 335, 968)));
    }
}
