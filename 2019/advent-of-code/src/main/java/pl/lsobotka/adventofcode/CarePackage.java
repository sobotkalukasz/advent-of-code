package pl.lsobotka.adventofcode;

import java.util.List;
import java.util.Objects;

/*
 * https://adventofcode.com/2019/day/13
 * */
public class CarePackage {

    private final int BLOCK = 2;
    private final int PADDLE = 3;
    private final int BALL = 4;

    final long[] program;

    public CarePackage(long[] program) {
        this.program = program;
    }

    public int countBlockTiles() {
        final IntCode intCode = new IntCode(program);
        final List<Long> output = intCode.execute();

        int count = 0;
        for (int index = 2; index < output.size(); index += 3) {
            if (output.get(index) == BLOCK) {
                count++;
            }
        }
        return count;
    }

    public long getTheScore() {
        program[0] = 2; //to play for free :)
        final IntCode intCode = new IntCode(program);

        long score = 0;
        Coordinate ball = null;
        Coordinate paddle = null;

        List<Long> output;

        do {
            if (Objects.nonNull(ball) && Objects.nonNull(paddle)) {
                final Long input = (long) Long.compare(ball.x, paddle.x);
                intCode.addInput(input);
                ball = null;
            }
            output = intCode.executeUntilExpectedOutputSize(3);

            if (isScore(output)) {
                score = output.get(2);
            } else if (isPaddle(output)) {
                paddle = new Coordinate(output.get(0), output.get(1));
            } else if (isBall(output)) {
                ball = new Coordinate(output.get(0), output.get(1));
            }

        } while (!intCode.isHalted());

        return score;
    }

    private boolean isScore(final List<Long> output) {
        return output.size() == 3 && output.get(0) == -1 && output.get(1) == 0;
    }

    private boolean isPaddle(final List<Long> output) {
        return output.size() == 3 && output.get(2) == PADDLE;
    }

    private boolean isBall(final List<Long> output) {
        return output.size() == 3 && output.get(2) == BALL;
    }

    private record Coordinate(long x, long y) {
    }
}
