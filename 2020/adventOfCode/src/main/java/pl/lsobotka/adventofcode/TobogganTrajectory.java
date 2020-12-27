package pl.lsobotka.adventofcode;

/*
 * https://adventofcode.com/2020/day/3
 * */

import java.util.List;

public class TobogganTrajectory {

    private final char TREE = '#';

    public long walkTheForest(List<String> testInput, int rowIncrease, int columnIncrease){
        int currentIndex = 0;
        int currentRow = 0;

        long trees = 0;

        int rows = testInput.size();
        int rowLen = testInput.get(0).length();

        for(currentRow = setRow(currentRow, rowIncrease, rows); currentRow < rows; currentRow = setRow(currentRow, rowIncrease, rows)){
            currentIndex = setIndex(currentIndex, columnIncrease, rowLen);
            if (isTree(testInput.get(currentRow), currentIndex))
                trees++;
        }
        return trees;
    }

    private int setRow(int actualRow, int increaseBy, int maxRowNumber){
        if(actualRow + increaseBy <= maxRowNumber-1)
            return actualRow + increaseBy;
        return maxRowNumber;
    }

    private int setIndex(int actualPos, int increaseBy, int maxRowLength){
        if(actualPos + increaseBy < maxRowLength)
            return actualPos + increaseBy;
        return actualPos + increaseBy - maxRowLength;
    }

    private boolean isTree(String row, int index){
        return row.charAt(index) == TREE;
    }
}
