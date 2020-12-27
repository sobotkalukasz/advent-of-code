package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TobogganTrajectoryTest {

    public List<String> testInput = new ArrayList<>();

    @ParameterizedTest
    @MethodSource("dataA")
    public void dayThreeFile(String path, int rowIncrease, int indexIncrease, long expectedNumberOfTrees) throws Exception {

        TobogganTrajectory trajectory = new TobogganTrajectory();

        if(testInput.isEmpty()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            testInput = bufferedReader.lines().collect(Collectors.toList());
            bufferedReader.close();
        }

        long output = trajectory.walkTheForest(testInput, rowIncrease, indexIncrease);

        assertEquals(expectedNumberOfTrees, output);

    }

    private static Stream<Arguments> dataA(){
        return Stream.of(
                Arguments.of("src/test/resources/TobogganTrajectory", 1, 1, 84),
                Arguments.of("src/test/resources/TobogganTrajectory", 1, 3, 195),
                Arguments.of("src/test/resources/TobogganTrajectory", 1, 5, 70),
                Arguments.of("src/test/resources/TobogganTrajectory", 1, 7, 70),
                Arguments.of("src/test/resources/TobogganTrajectory", 2, 1, 47)
        );
    }

}
