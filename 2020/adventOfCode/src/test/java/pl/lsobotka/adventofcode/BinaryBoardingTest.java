package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinaryBoardingTest {

    private static Stream<Arguments> data(){
        return Stream.of(
                Arguments.of("src/test/resources/BinaryBoarding", 835, 649)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void dayFiveFile(String path, int expectedBiggestID, int expectedSeatId) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> testInput = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        BinaryBoarding binaryBoarding = new BinaryBoarding();
        List<BinaryBoarding.Boarding> binaryBordings = testInput.stream().map(code -> binaryBoarding.encodeSeat(code, 128, 8)).collect(Collectors.toList());
        int biggestID = binaryBordings.stream().max(Comparator.comparing(BinaryBoarding.Boarding::seatID)).get().seatID();
        assertEquals(expectedBiggestID, biggestID);

        int mySeatId = binaryBoarding.findMySeatId(binaryBordings.stream().map(BinaryBoarding.Boarding::seatID).sorted().collect(Collectors.toList()));
        assertEquals(expectedSeatId, mySeatId);


    }

    private static Stream<Arguments> simpleTestData(){
        return Stream.of(
                Arguments.of("BFFFBBFRRR", 128, 8, new BinaryBoarding.Boarding("BFFFBBFRRR", 70, 7, 567)),
                Arguments.of("FFFBBBFRRR", 128, 8, new BinaryBoarding.Boarding("FFFBBBFRRR", 14, 7, 119)),
                Arguments.of("BBFFBBFRLL", 128, 8, new BinaryBoarding.Boarding("BBFFBBFRLL", 102, 4, 820)),
                Arguments.of("FFFFFFFLLL", 128, 8, new BinaryBoarding.Boarding("FFFFFFFLLL", 0, 0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource("simpleTestData")
    public void dayFive(String code, int rowQty, int columnQty, BinaryBoarding.Boarding expected){
        BinaryBoarding binaryBoarding = new BinaryBoarding();
        BinaryBoarding.Boarding seat = binaryBoarding.encodeSeat(code, rowQty, columnQty);
        assertEquals(expected, seat);
    }
}
