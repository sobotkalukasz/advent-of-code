package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportRepairTest {

    @ParameterizedTest
    @MethodSource("dayOneAData")
    public void dayOneFromFile(int sum, String path, ReportRepair.IntPair expected) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<Integer> collect = bufferedReader.lines().map(Integer::valueOf).collect(Collectors.toList());
        bufferedReader.close();

        ReportRepair report = new ReportRepair();
        ReportRepair.IntPair output = report.findPairOfSum(sum, collect.stream().mapToInt(Integer::intValue).toArray());

        assertEquals(expected, output);

    }

    private static Stream<Arguments> dayOneAData(){
        return Stream.of(
                Arguments.of(2020, "src/test/resources/ReportRepair", new ReportRepair.IntPair(437, 1583))
        );
    }

    @ParameterizedTest
    @MethodSource("dayOneBData")
    public void dayTwoFromFile(int sum, String path, ReportRepair.IntTrio expected) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<Integer> collect = bufferedReader.lines().map(Integer::valueOf).collect(Collectors.toList());
        bufferedReader.close();

        ReportRepair report = new ReportRepair();
        ReportRepair.IntTrio output = report.findTrioOfSum(sum, collect.stream().mapToInt(Integer::intValue).toArray());

        assertEquals(expected, output);

    }

    private static Stream<Arguments> dayOneBData(){
        return Stream.of(
                Arguments.of(2020, "src/test/resources/ReportRepair", new ReportRepair.IntTrio(717, 335, 968))
        );
    }
}
