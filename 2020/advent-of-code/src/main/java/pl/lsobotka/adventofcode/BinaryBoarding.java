package pl.lsobotka.adventofcode;

/*
 * https://adventofcode.com/2020/day/5
 * */

import java.util.List;
import java.util.stream.IntStream;

public class BinaryBoarding {

    private final char ROW_LOWER = 'F';
    private final char COLUMN_LOWER = 'L';

    public record Boarding(String binaryCode, int row, int column, int seatID){}

    public Boarding encodeSeat(String binaryCode, int rowQty, int columnQty) {
        String rowCode = binaryCode.substring(0, 7);
        int row = encode(rowCode, 0, rowQty - 1, ROW_LOWER);

        String columnCode = binaryCode.substring(7);
        int column = encode(columnCode, 0, columnQty - 1, COLUMN_LOWER);

        return new Boarding(binaryCode, row, column, seatID(row, column));
    }

    private int encode(String binaryCode, int min, int max, char charCode){
        char code = binaryCode.charAt(0);
        int half = (int)IntStream.rangeClosed(min, max).count()/2;
        if(binaryCode.length() == 1){
            if(code == charCode)
                return min;
            return max;
        } else {
            if(code == charCode)
                return encode(binaryCode.substring(1), min, max-half, charCode);
            return encode(binaryCode.substring(1), min + half, max, charCode);
        }
    }

    public int findMySeatId(List<Integer> ids){
        for(int index = 0; index < ids.size(); index++){
            if(ids.get(index+1) != ids.get(index) + 1 && ids.get(index+1) == ids.get(index) + 2)
                return ids.get(index) + 1;
        }
        return 0;
    }

    private int seatID(int row, int column){
        return row * 8 + column;
    }
}
