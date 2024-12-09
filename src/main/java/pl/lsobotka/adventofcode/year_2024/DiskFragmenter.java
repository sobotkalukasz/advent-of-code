package pl.lsobotka.adventofcode.year_2024;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * https://adventofcode.com/2024/day/9
 * */
public class DiskFragmenter {
    private final List<Block> disc;

    public DiskFragmenter(final String line) {
        final AtomicInteger usedSpaceIdCounter = new AtomicInteger(0);
        disc = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            final int blockSize = Integer.parseInt(String.valueOf(line.charAt(i)));
            if (i % 2 == 0) {
                final int fileId = usedSpaceIdCounter.getAndIncrement();
                disc.add(new File(fileId, blockSize));
            } else {
                disc.add((new Empty(0, blockSize)));
            }
        }
    }

    long getChecksum() {
        final List<Block> tempDisc = new ArrayList<>(disc);
        final TreeMap<Integer, Integer> discMap = new TreeMap<>();
        final AtomicInteger currentSector = new AtomicInteger(0);

        for (int index = 0; !tempDisc.isEmpty(); ) {
            final Block removed = tempDisc.remove(index);
            if (removed instanceof File(int id, int size)) {
                for (int s = 0; s < size; s++) {
                    discMap.put(currentSector.getAndIncrement(), id);
                }
            } else if (removed instanceof Empty e) {
                final int freeSectors = e.size();
                for (int s = 0; s < freeSectors && !tempDisc.isEmpty(); s++) {
                    discMap.put(currentSector.getAndIncrement(), getUsedSpaceLastIndex(tempDisc));
                }
            }
        }

        return discMap.entrySet().stream().map(e -> (long) e.getKey() * e.getValue()).reduce(0L, Long::sum);
    }

    private int getUsedSpaceLastIndex(final List<Block> disc) {
        for (int i = disc.size() - 1; i >= 0; i--) {
            if (disc.get(i) instanceof File(int id, int size)) {
                if (size - 1 > 0) {
                    disc.remove(i);
                    disc.add(i, new File(id, size - 1));
                } else {
                    disc.remove(i);
                }
                return id;
            }
        }
        return 0;
    }

    long getChecksumWithFileMove() {
        final List<Block> tempDisc = new ArrayList<>(this.disc);

        for (int index = tempDisc.size() - 1; index > 0; index--) {
            if (tempDisc.get(index) instanceof File file) {
                for (int emptyIndex = 0; emptyIndex < index; emptyIndex++) {
                    if (tempDisc.get(emptyIndex) instanceof Empty empty && empty.size() >= file.size()) {
                        if (file.size() == empty.size()) {
                            tempDisc.remove(emptyIndex);
                            tempDisc.add(emptyIndex, file);
                            tempDisc.remove(index);
                            tempDisc.add(index, empty);
                        } else {
                            tempDisc.remove(emptyIndex);
                            tempDisc.add(emptyIndex, file);

                            tempDisc.remove(index);
                            tempDisc.add(index, new Empty(0, file.size()));

                            tempDisc.add(emptyIndex + 1, new Empty(0, empty.size() - file.size()));
                        }
                        break;
                    }
                }
            }
        }

        final TreeMap<Integer, Integer> discMap = new TreeMap<>();
        final AtomicInteger currentSector = new AtomicInteger(0);

        for (Block block : tempDisc) {
            for (int s = 0; s < block.size(); s++) {
                discMap.put(currentSector.getAndIncrement(), block.id());
            }
        }

        return discMap.entrySet().stream().map(e -> (long) e.getKey() * e.getValue()).reduce(0L, Long::sum);
    }

}

interface Block {
    int id();

    int size();
}

record File(int id, int size) implements Block {
}

record Empty(int id, int size) implements Block {
}


