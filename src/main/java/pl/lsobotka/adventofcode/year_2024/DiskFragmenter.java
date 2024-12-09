package pl.lsobotka.adventofcode.year_2024;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * https://adventofcode.com/2024/day/9
 * */
public class DiskFragmenter {
    Map<Integer, Integer> usedSpace;
    Map<Integer, Integer> freeSpace;

    public DiskFragmenter(final String line) {
        final AtomicInteger usedSpaceIdCounter = new AtomicInteger(0);
        final AtomicInteger freeSpaceIdCounter = new AtomicInteger(0);
        usedSpace = new HashMap<>();
        freeSpace = new HashMap<>();

        for (int i = 0; i < line.length(); i++) {
            final Integer blockSize = Integer.valueOf(String.valueOf(line.charAt(i)));
            if (i % 2 == 0) {
                usedSpace.put(usedSpaceIdCounter.getAndIncrement(), blockSize);
            } else {
                freeSpace.put(freeSpaceIdCounter.getAndIncrement(), blockSize);
            }
        }
    }

    long getChecksum() {
        final TreeMap<Integer, Integer> tempUsedSpace = new TreeMap<>(usedSpace);
        final Map<Integer, Integer> tempFreeSpace = new HashMap<>(freeSpace);
        final TreeMap<Integer, Integer> disc = new TreeMap<>();
        final AtomicInteger currentSector = new AtomicInteger(0);

        for (int index = 0; tempUsedSpace.containsKey(index); index++) {
            // used space from top
            final Integer sectors = tempUsedSpace.remove(index);
            for (int s = 0; s < sectors; s++) {
                disc.put(currentSector.getAndIncrement(), index);
            }

            final Integer freeSectors = tempFreeSpace.remove(index);
            for (int s = 0; s < freeSectors && !tempFreeSpace.isEmpty(); s++) {
                disc.put(currentSector.getAndIncrement(), getUsedSpaceLastIndex(tempUsedSpace));
            }
        }

        return disc.entrySet().stream().map(e -> (long) e.getKey() * e.getValue()).reduce(0L, Long::sum);

    }

    private int getUsedSpaceLastIndex(final TreeMap<Integer, Integer> tempUsedSpace) {
        if(tempUsedSpace.isEmpty()){
            return 0;
        }
        final Integer index = tempUsedSpace.lastKey();
        final Integer size = tempUsedSpace.get(index);
        if (size - 1 > 0) {
            tempUsedSpace.put(index, size - 1);
        } else {
            tempUsedSpace.remove(index);
        }

        return index;
    }

}
