package pl.lsobotka.adventofcode.year_2021.passagepathing;

import java.util.Objects;

public record Cave(String name) {

    public boolean isSmall() {
        final char c = name.charAt(0);
        return Character.isLowerCase(c);
    }

    public boolean isStart() {
        return name.equals("start");
    }

    public boolean isEnd() {
        return name.equals("end");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cave cave = (Cave) o;
        return Objects.equals(name, cave.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

