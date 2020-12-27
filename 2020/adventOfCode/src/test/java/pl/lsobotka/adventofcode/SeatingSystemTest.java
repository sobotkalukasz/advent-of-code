package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeatingSystemTest {

    private static Stream<Arguments> seats(){
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of("L.LL.LL.LL", "LLLLLLL.LL", "L.L.L..L..", "LLLL.LL.LL", "L.LL.LL.LL", "L.LLLLL.LL", "..L.L.....", "LLLLLLLLLL", "L.LLLLLL.L", "L.LLLLL.LL")), 37)
        );
    }

    @ParameterizedTest
    @MethodSource("seats")
    public void occupiedSeatsTest(List<String> seats, int expected) {

        SeatingSystem system = new SeatingSystem();
        long actual = system.simpleRule(seats);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> seatsFromFile(){
        return Stream.of(
                Arguments.of("src/test/resources/SeatingSystem", 2368)
        );
    }

    @ParameterizedTest
    @MethodSource("seatsFromFile")
    public void occupiedSeatsFromFileTest(String path, int expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> seats = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        SeatingSystem system = new SeatingSystem();
        long actual = system.simpleRule(seats);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> seatsComplex(){
        return Stream.of(
                Arguments.of(new ArrayList<>(List.of("L.LL.LL.LL", "LLLLLLL.LL", "L.L.L..L..", "LLLL.LL.LL", "L.LL.LL.LL", "L.LLLLL.LL", "..L.L.....", "LLLLLLLLLL", "L.LLLLLL.L", "L.LLLLL.LL")), 26)
        );
    }

    @ParameterizedTest
    @MethodSource("seatsComplex")
    public void occupiedSeatsComplexTest(List<String> seats, int expected) {

        SeatingSystem system = new SeatingSystem();
        long actual = system.complexRule(seats);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> seatsComplexFromFile(){
        return Stream.of(
                Arguments.of("src/test/resources/SeatingSystem", 2124)
        );
    }

    @ParameterizedTest
    @MethodSource("seatsComplexFromFile")
    public void occupiedSeatsComplexFromFileTest(String path, int expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> seats = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        SeatingSystem system = new SeatingSystem();
        long actual = system.complexRule(seats);
        assertEquals(expected, actual);
    }
}
