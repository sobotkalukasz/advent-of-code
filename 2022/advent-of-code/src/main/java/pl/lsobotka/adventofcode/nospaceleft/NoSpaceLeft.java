package pl.lsobotka.adventofcode.nospaceleft;

import java.util.List;
import java.util.Map;

/*
 * https://adventofcode.com/2022/day/7
 * */
public class NoSpaceLeft {

    private final FileSystem fileSystem;

    NoSpaceLeft(final List<String> input) {
        fileSystem = new FileSystem(input);
    }

    long getSizeOfAllDirectoriesAtMost(final long maxSize) {
        return fileSystem.getSizeOfAllDirectories()
                .values()
                .stream()
                .filter(size -> size <= maxSize)
                .mapToLong(Long::longValue)
                .sum();
    }

    long getSizeOfDirectoryToDelete(final long totalSpace, final long requiredSpace) {
        final long usedSpace = fileSystem.getTotalUsedSpace();
        final long freeSpace = totalSpace - usedSpace;
        final long missingSpace = requiredSpace - freeSpace;

        final Map<String, Long> sizes = fileSystem.getSizeOfAllDirectories();
        return sizes.values().stream().filter(size -> size >= missingSpace).mapToLong(Long::longValue).min().orElse(0);
    }

}
