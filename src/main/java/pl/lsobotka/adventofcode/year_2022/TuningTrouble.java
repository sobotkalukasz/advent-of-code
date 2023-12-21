package pl.lsobotka.adventofcode.year_2022;

import java.util.List;

/*
 * https://adventofcode.com/2022/day/6
 * */
public class TuningTrouble {

    private static final int PACKET_MARKER_SIZE = 4;
    private static final int MESSAGE_MARKER_SIZE = 14;

    private final String input;

    public TuningTrouble(final String input) {
        this.input = input;
    }

    int determineStartOfPacketIndex() {
        return determineStartIndexForDistinctCharacters(PACKET_MARKER_SIZE);
    }

    int determineStartOfMessageIndex() {
        return determineStartIndexForDistinctCharacters(MESSAGE_MARKER_SIZE);
    }

    private int determineStartIndexForDistinctCharacters(final int qtyDistinctChars){
        final List<Character> chars = input.chars().mapToObj(c -> (char) c).toList();

        int packageStartIndex = -1;
        for (int i = qtyDistinctChars; i < chars.size(); i++) {
            final long distinctChars = chars.subList(i - qtyDistinctChars, i).stream().distinct().count();
            if (distinctChars == qtyDistinctChars) {
                packageStartIndex = i;
                break;
            }
        }
        return packageStartIndex;
    }
}
