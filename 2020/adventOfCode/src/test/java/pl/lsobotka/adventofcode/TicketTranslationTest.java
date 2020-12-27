package pl.lsobotka.adventofcode;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTranslationTest {

    private static Stream<Arguments> ticketData(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("class: 1-3 or 5-7", "row: 6-11 or 33-44", "seat: 13-40 or 45-50", "", "your ticket:", "7,1,14", "",
                        "nearby tickets:", "7,3,47", "40,4,50", "55,2,20", "38,6,12")), 71)
        );
    }

    @ParameterizedTest
    @MethodSource("ticketData")
    public void ticketDataTest(List<String> data, long expected) {

        TicketTranslation tt = new TicketTranslation(data);
        long actual = tt.findErrorRate();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> ticketDataFile(){
        return Stream.of(
                Arguments.of("src/test/resources/TicketTranslation", 20231)
        );
    }

    @ParameterizedTest
    @MethodSource("ticketDataFile")
    public void ticketDataFileTest(String path, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        TicketTranslation tt = new TicketTranslation(data);
        long actual = tt.findErrorRate();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> ticketFieldData(){
        return Stream.of(
                Arguments.of(new ArrayList<>(Arrays.asList("class: 0-1 or 4-19", "row: 0-5 or 8-19", "seat: 0-13 or 16-19", "", "your ticket:", "11,12,13", "",
                        "nearby tickets:", "3,9,18", "15,1,5", "5,14,9")), "seat", 13)
        );
    }

    @ParameterizedTest
    @MethodSource("ticketFieldData")
    public void ticketFieldDataTest(List<String> data, String field, long expected) {

        TicketTranslation tt = new TicketTranslation(data);
        long actual = tt.findFieldValue(field);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> ticketFieldDataFile(){
        return Stream.of(
                Arguments.of("src/test/resources/TicketTranslation", "departure", 1940065747861L)
        );
    }

    @ParameterizedTest
    @MethodSource("ticketFieldDataFile")
    public void ticketFieldDataFileTest(String path, String field, long expected) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        List<String> data = bufferedReader.lines().collect(Collectors.toList());
        bufferedReader.close();

        TicketTranslation tt = new TicketTranslation(data);
        long actual = tt.findFieldValue(field);
        assertEquals(expected, actual);
    }

    
    
}
