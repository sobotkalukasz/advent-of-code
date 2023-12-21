package pl.lsobotka.adventofcode.year_2020;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2020/day/11
 * */
public class SeatingSystem {

    private static AtomicInteger tempRow;
    private static AtomicInteger tempColumn;
    private static int rows;
    private static int columns;

    private final static char FLOOR = '.';
    private final static  char EMPTY = 'L';
    private final static char OCCUPIED = '#';

    private static final List<Test<Boolean>> seatTests;
    static{
        seatTests = new ArrayList<>();
        seatTests.add(() -> tempRow.decrementAndGet() >= 0);
        seatTests.add(() -> tempRow.decrementAndGet() >= 0 && tempColumn.incrementAndGet() < columns);
        seatTests.add(() -> tempColumn.incrementAndGet() < columns);
        seatTests.add(() -> tempRow.incrementAndGet() < rows && tempColumn.incrementAndGet() < columns);
        seatTests.add(() -> tempRow.incrementAndGet() < rows);
        seatTests.add(() -> tempRow.incrementAndGet() < rows && tempColumn.decrementAndGet() >= 0);
        seatTests.add(() -> tempColumn.decrementAndGet() >= 0);
        seatTests.add(() -> tempRow.decrementAndGet() >= 0 && tempColumn.decrementAndGet() >= 0);
    }

    public long simpleRule(List<String> data){
        char[][] seats = data.stream().map(String::toCharArray).toArray(char[][]::new);
        rows = seats.length;
        columns = seats[0].length;

        seats = takeSeats(seats);
        long seatCount = 0;
        while(seatCount != countOccupySeats(seats)){
            seatCount = countOccupySeats(seats);
            seats = takeSeats(seats);
        }
        return seatCount;
    }

    public long complexRule(List<String> test){
        char[][] seats = test.stream().map(String::toCharArray).toArray(char[][]::new);
        rows = seats.length;
        columns = seats[0].length;
        seats = takeSeatsComplex(seats);
        long seatCount = 0;

        while(seatCount != countOccupySeats(seats)){
            seatCount = countOccupySeats(seats);
            seats = takeSeatsComplex(seats);
        }

        return seatCount;
    }

    private char[][] takeSeats(char[][] seats){
        char[][] copy = new char[seats.length][seats[0].length];
        for(int i = 0; i < seats.length; i++){
            int row = i;
            for(int j = 0; j < seats[i].length; j++){
                int column = j;
                if(seats[i][j] == OCCUPIED && seatTests.stream().map(test -> isNearestSeatOccupy(test, seats, row, column)).filter(t-> t).count() >= 4){
                    copy[i][j] = EMPTY;
                } else if(seats[i][j] == EMPTY && seatTests.stream().allMatch(test -> isNearestSeatEmpty(test, seats, row, column))){
                    copy[i][j] = OCCUPIED;
                } else {
                    copy[i][j] = seats[i][j];
                }
            }
        }
        return copy;
    }

    private char[][] takeSeatsComplex(char[][] seats){
        char[][] copy = new char[seats.length][seats[0].length];
        for(int i = 0; i < seats.length; i++){
            int row = i;
            for(int j = 0; j < seats[i].length; j++){
                int column = j;
                if(seats[row][column] == OCCUPIED && seatTests.stream().map(test -> isFirstSeatOccupy(test, seats, row, column)).filter(t->t).count() >= 5){
                    copy[row][column] = EMPTY;
                } else if(seats[row][column] == EMPTY && seatTests.stream().allMatch(test -> isFirstSeatEmpty(test, seats, row, column))){
                    copy[row][column] = OCCUPIED;
                } else {
                    copy[row][column] = seats[row][column];
                }
            }
        }
        return copy;
    }

    private boolean isNearestSeatOccupy(Test<Boolean> condition, char[][] seats, int row, int col) {
        tempRow = new AtomicInteger(row);
        tempColumn = new AtomicInteger(col);
        if(condition.test()){
            char seat = seats[tempRow.get()][tempColumn.get()];
            return isOccupied(seat);
        }
        return false;
    }

    private boolean isFirstSeatOccupy(Test<Boolean> condition, char[][] seats, int row, int col) {
        tempRow = new AtomicInteger(row);
        tempColumn = new AtomicInteger(col);
        while(condition.test()){
            char seat = seats[tempRow.get()][tempColumn.get()];
            if(isNotFloor(seat)){
                return isOccupied(seat);
            }
        }
        return false;
    }

    private boolean isNearestSeatEmpty(Test<Boolean> condition, char[][] seats, int row, int col) {
        tempRow = new AtomicInteger(row);
        tempColumn = new AtomicInteger(col);
        if(condition.test()){
            char seat = seats[tempRow.get()][tempColumn.get()];
            return !isOccupied(seat);
        }
        return true;
    }

    private boolean isFirstSeatEmpty(Test<Boolean> condition, char[][] seats, int row, int col) {
        tempRow = new AtomicInteger(row);
        tempColumn = new AtomicInteger(col);
        while(condition.test()){
            char seat = seats[tempRow.get()][tempColumn.get()];
            if(isNotFloor(seat)){
                return !isOccupied(seat);
            }
        }
        return true;
    }

    private boolean isOccupied(char c){
        return c == OCCUPIED;
    }
    private boolean isNotFloor(char c){
        return c != FLOOR;
    }

    private long countOccupySeats(char[][] seats){
        return Stream.of(seats).flatMapToInt(c -> CharBuffer.wrap(c).chars()).filter(c -> c == OCCUPIED).count();
    }

}

@FunctionalInterface
interface Test<V> {
    V test();
}
