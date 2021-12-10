package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2021/day/10
 * */
public class SyntaxScoring {

    private final List<Row> rows;

    public SyntaxScoring(List<String> input) {
        rows = initRows(input);
    }

    private List<Row> initRows(List<String> input) {
        return input.stream().map(Row::new).collect(Collectors.toList());
    }

    public int getScoreOfIllegalCharacters() {
        return rows.stream()
                .filter(row -> State.INCORRECT.equals(row.state))
                .map(Row::getFirstIncorrect)
                .map(this::getScoreOfIllegalCharacter)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public long getScoreOfCompleteCharacters() {
        final List<Long> scores = rows.stream()
                .filter(row -> State.INCOMPLETE.equals(row.state))
                .map(Row::completeTheRow)
                .map(this::getScoreOfCompletedRow)
                .sorted()
                .collect(Collectors.toList());

        final int size = scores.size();
        final int middleScoreIndex = size - 1 - size / 2;
        return scores.get(middleScoreIndex);
    }

    private int getScoreOfIllegalCharacter(final String illegal) {
        return switch (illegal) {
            case ")" -> 3;
            case "]" -> 57;
            case "}" -> 1197;
            case ">" -> 25137;
            default -> 0;
        };
    }

    private long getScoreOfCompletedRow(final String row) {
        return Arrays.stream(row.split(""))
                .map(this::getScoreOfCompletedCharacter)
                .reduce((a, b) -> a * 5 + b)
                .orElse(0L);
    }

    private long getScoreOfCompletedCharacter(final String complete) {
        return switch (complete) {
            case ")" -> 1;
            case "]" -> 2;
            case "}" -> 3;
            case ">" -> 4;
            default -> 0;
        };
    }

    private static class Row {

        final static List<String> opening = Arrays.asList("(", "[", "{", "<");

        final List<Chunk> chunks;
        private State state;

        public Row(final String row) {
            chunks = initChunks(row);
            state = initState();
        }

        public String getFirstIncorrect() {
            return chunks.stream()
                    .filter(chunk -> chunk.isClosed() && chunk.isNotValid())
                    .map(Chunk::getClose)
                    .findFirst()
                    .orElse("");
        }

        public String completeTheRow() {
            final StringBuilder complete = new StringBuilder();
            Chunk tempChunk = getLastNotClosed(chunks);
            while (Objects.nonNull(tempChunk)) {
                tempChunk.closeIt();
                complete.append(tempChunk.close);
                tempChunk = getLastNotClosed(chunks);
            }
            state = State.OK;
            return complete.toString();
        }

        private List<Chunk> initChunks(final String row) {
            final List<Chunk> chunks = new ArrayList<>();
            final String[] parenthesis = row.split("");

            for (final String str : parenthesis) {
                final Chunk chunk;
                if (isOpeningCharacter(str)) {
                    chunk = new Chunk(str);
                    chunks.add(chunk);
                } else {
                    chunk = getLastNotClosed(chunks);
                    if (Objects.nonNull(chunk)) {
                        chunk.setClose(str);
                        if (chunk.isNotValid()) {
                            break;
                        }
                    }
                }
            }
            return chunks;
        }

        private State initState() {
            final boolean anyIncorrect = chunks.stream().anyMatch(chunk -> chunk.isClosed() && chunk.isNotValid());
            final boolean anyIncomplete = chunks.stream().anyMatch(chunk -> !chunk.isClosed());
            final State state;
            if (anyIncorrect) {
                state = State.INCORRECT;
            } else if (anyIncomplete) {
                state = State.INCOMPLETE;
            } else {
                state = State.OK;
            }
            return state;
        }

        private boolean isOpeningCharacter(final String parenthesis) {
            return opening.contains(parenthesis);
        }

        private Chunk getLastNotClosed(List<Chunk> chunks) {
            final int size = chunks.size();
            for (int i = size - 1; i >= 0; i--) {
                final Chunk chunk = chunks.get(i);
                if (!chunk.isClosed()) {
                    return chunk;
                }
            }
            return null;
        }
    }

    private static class Chunk {

        private final String open;
        private String close;

        public Chunk(String open) {
            this.open = open;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }

        public void closeIt() {
            if (open.equals("(")) {
                close = ")";
            }
            if (open.equals("[")) {
                close = "]";
            }
            if (open.equals("{")) {
                close = "}";
            }
            if (open.equals("<")) {
                close = ">";
            }
        }

        public boolean isNotValid() {
            if (open.equals("(")) {
                return !")".equals(close);
            }
            if (open.equals("[")) {
                return !"]".equals(close);
            }
            if (open.equals("{")) {
                return !"}".equals(close);
            }
            if (open.equals("<")) {
                return !">".equals(close);
            }
            return true;
        }

        public boolean isClosed() {
            return Objects.nonNull(close);
        }
    }

    private enum State {
        OK, INCOMPLETE, INCORRECT
    }
}
