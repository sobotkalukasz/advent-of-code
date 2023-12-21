package pl.lsobotka.adventofcode.year_2021.smokebasin;

import java.util.List;

/*
 * https://adventofcode.com/2021/day/9
 * */
public class SmokeBasin {

    final CaveMap caveMap;

    public SmokeBasin(final List<String> inputRows) {
        this.caveMap = new CaveMap(inputRows);
    }

    public int getRiskLevel() {
        return caveMap.calculateRiskLevel();
    }

    public int getSizeOfLargestBasins(final int basinsToCount) {
        return caveMap.calculateSizeOfBasins(basinsToCount);
    }

}
