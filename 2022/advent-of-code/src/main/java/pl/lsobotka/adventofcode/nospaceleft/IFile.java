package pl.lsobotka.adventofcode.nospaceleft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public sealed interface IFile permits IFile.File, IFile.Directory {

    boolean isDir();

    String getName();

    final class File implements IFile {

        private final String name;
        private final long size;

        File(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public boolean isDir() {
            return false;
        }

        @Override
        public String getName() {
            return name;
        }

        public long getSize() {
            return size;
        }
    }

    final class Directory implements IFile {

        private final String name;

        private final List<IFile> content;

        private final Directory parent;

        Directory(final String name, final Directory parent) {
            this.name = name;
            this.content = new ArrayList<>();
            this.parent = parent;
        }

        void add(final IFile file) {
            content.add(file);
        }

        List<File> files() {
            return content.stream().filter(not(IFile::isDir)).map(File.class::cast).collect(Collectors.toList());
        }

        List<Directory> directories() {
            return content.stream().filter(IFile::isDir).map(Directory.class::cast).collect(Collectors.toList());
        }

        @Override
        public String getName() {
            return name;
        }

        Directory getParent() {
            return parent;
        }

        private String path() {
            return Optional.ofNullable(parent).map(p -> p.path() + "/" + name).orElse("");
        }

        long getContentSize() {
            final long directoriesSize = directories().stream().mapToLong(Directory::getContentSize).sum();
            return directoriesSize + getFilesSize();
        }

        long getFilesSize() {
            return files().stream().mapToLong(File::getSize).sum();
        }

        Map<String, Long> getSizeOfChildDirectories() {
            final Map<String, Long> sizes = new HashMap<>();
            sizes.put(path(), getContentSize());
            directories().forEach(child -> sizes.putAll(child.getSizeOfChildDirectories()));
            return sizes;
        }

        @Override
        public boolean isDir() {
            return true;
        }
    }
}
