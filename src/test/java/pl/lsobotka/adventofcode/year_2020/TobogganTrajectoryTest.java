package pl.lsobotka.adventofcode.year_2020;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TobogganTrajectoryTest extends BaseTest {

    public List<String> testInput = new ArrayList<>();

    @ParameterizedTest
    @MethodSource("dataA")
    void dayThreeFile(String fileName, int rowIncrease, int indexIncrease, long expectedNumberOfTrees) {
        TobogganTrajectory trajectory = new TobogganTrajectory();

        if (testInput.isEmpty()) {
            testInput = getFileInput(fileName);
        }

        long output = trajectory.walkTheForest(testInput, rowIncrease, indexIncrease);

        assertEquals(expectedNumberOfTrees, output);
    }

    private static Stream<Arguments> dataA() {
        return Stream.of(Arguments.of("2020/TobogganTrajectory", 1, 1, 84), //
                Arguments.of("2020/TobogganTrajectory", 1, 3, 195), //
                Arguments.of("2020/TobogganTrajectory", 1, 5, 70), //
                Arguments.of("2020/TobogganTrajectory", 1, 7, 70), //
                Arguments.of("2020/TobogganTrajectory", 2, 1, 47));
    }

}
