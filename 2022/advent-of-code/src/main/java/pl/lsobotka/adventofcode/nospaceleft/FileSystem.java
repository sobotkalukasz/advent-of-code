package pl.lsobotka.adventofcode.nospaceleft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pl.lsobotka.adventofcode.nospaceleft.IFile.*;

public class FileSystem {

    private final static String ROOT = "/";

    private final Directory root;

    private Directory currentDir;

    FileSystem(final List<String> operations) {
        root = new Directory("/", null);
        toOperations(operations).forEach(op -> op.apply(this));
        currentDir = root;
    }

    Map<String, Long> getSizeOfAllDirectories() {
        return root.getSizeOfChildDirectories();
    }

    long getTotalUsedSpace() {
        return root.getContentSize();
    }

    private List<Operation> toOperations(final List<String> rawOperations) {
        final List<Operation> operations = new ArrayList<>();
        for (int i = 0; i < rawOperations.size(); i++) {
            final String row = rawOperations.get(i);
            final String[] split = row.split(" ");
            if (split[0].equals("$")) {
                if (split[1].equals("cd")) {
                    operations.add(new Operation.Move(split[2]));
                } else if (split[1].equals("ls")) {
                    final List<String> files = new ArrayList<>();
                    for (int f = i + 1; f < rawOperations.size() && rawOperations.get(f).charAt(0) != '$'; f++) {
                        files.add(rawOperations.get(f));
                    }
                    operations.add(new Operation.Make(files));
                }
            }
        }
        return operations;
    }

    private sealed interface Operation permits Operation.Move, Operation.Make {
        void apply(FileSystem fileSystem);

        final class Move implements Operation {
            private final String destination;

            Move(final String destination) {
                this.destination = destination;
            }

            @Override
            public void apply(final FileSystem fileSystem) {
                if (destination.equals(ROOT)) {
                    fileSystem.currentDir = fileSystem.root;
                } else if (destination.equals("..")) {
                    fileSystem.currentDir = fileSystem.currentDir.getParent();
                } else {
                    fileSystem.currentDir = fileSystem.currentDir.directories()
                            .stream()
                            .filter(d -> d.getName().equals(destination))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Unknown child directory: " + destination));
                }
            }

        }

        final class Make implements Operation {
            private final List<String> files;

            Make(final List<String> files) {
                this.files = files;
            }

            @Override
            public void apply(final FileSystem fileSystem) {
                files.stream().map(f -> {
                    final IFile file;
                    final String[] split = f.split(" ");
                    if (split[0].equals("dir")) {
                        file = new Directory(split[1], fileSystem.currentDir);
                    } else {
                        file = new File(split[1], Long.parseLong(split[0]));
                    }
                    return file;
                }).forEach(fileSystem.currentDir::add);
            }
        }
    }
}
