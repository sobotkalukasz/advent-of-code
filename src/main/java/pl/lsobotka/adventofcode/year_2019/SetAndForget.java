package pl.lsobotka.adventofcode.year_2019;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import pl.lsobotka.adventofcode.utils.Coord;
import pl.lsobotka.adventofcode.utils.Dir;
import pl.lsobotka.adventofcode.utils.Rotate;

/*
 * https://adventofcode.com/2019/day/17
 * */
public class SetAndForget {

    final long[] program;
    final Set<Coord> map;
    final Pos start;

    SetAndForget(final List<String> input) {
        this.program = input.stream()
                .flatMapToLong(row -> Arrays.stream(row.split(",")).mapToLong(Long::valueOf))
                .toArray();

        final long[] programCopy = Arrays.copyOf(program, program.length);
        final List<Long> asciiCodes = new IntCode(programCopy).execute();
        printMap(asciiCodes);

        this.map = prepareMap(asciiCodes);
        this.start = determineStartingPos(asciiCodes);
    }

    long getAlignmentPattern() {
        return map.stream().filter(this::isIntersection).mapToLong(c -> (long) c.row() * c.col()).sum();
    }

    long collectTheDust() {
        final List<String> path = determinePath();

        final FunctionHolder functionHolder = FunctionHolder.from(path);

        final List<Long> functionOrder = functionHolder.getFunctionOrder();
        final List<Long> aFunction = functionHolder.a.getFunctionOrder();
        final List<Long> bFunction = functionHolder.b.getFunctionOrder();
        final List<Long> cFunction = functionHolder.c.getFunctionOrder();

        final long[] programCopy = Arrays.copyOf(program, program.length);
        programCopy[0] = 2;

        final IntCode intCode = new IntCode(programCopy);
        intCode.addInput(functionOrder.stream().mapToLong(l -> l).toArray());
        intCode.addInput(aFunction.stream().mapToLong(l -> l).toArray());
        intCode.addInput(bFunction.stream().mapToLong(l -> l).toArray());
        intCode.addInput(cFunction.stream().mapToLong(l -> l).toArray());
        intCode.addInput('n', '\n');

        final List<Long> execute = intCode.execute();
        printMap(execute);

        return execute.getLast();
    }

    private boolean isIntersection(final Coord coord) {
        final Set<Coord> adjacent = coord.getDirectAdjacent();
        return map.containsAll(adjacent);
    }

    private List<String> determinePath() {
        Coord current = start.coord;
        Dir dir = start.dir;
        final List<String> log = new ArrayList<>();
        int dirCounter = 0;

        boolean searching = true;
        while (searching) {
            final Coord nextMove = current.getAdjacent(dir);
            final Coord nextLeft = current.getAdjacent(dir.rotate(Rotate.L));
            final Coord nextRight = current.getAdjacent(dir.rotate(Rotate.R));

            if (map.contains(nextMove)) {
                current = nextMove;
                dirCounter++;
            } else if (map.contains(nextLeft)) {
                if (dirCounter != 0) {
                    log.add(String.valueOf(dirCounter));
                    dirCounter = 0;
                }
                log.add(Rotate.L.name());
                dir = dir.rotate(Rotate.L);
            } else if (map.contains(nextRight)) {
                if (dirCounter != 0) {
                    log.add(String.valueOf(dirCounter));
                    dirCounter = 0;
                }
                log.add(Rotate.R.name());
                dir = dir.rotate(Rotate.R);
            } else {
                if (dirCounter != 0) {
                    log.add(String.valueOf(dirCounter));
                    searching = false;
                }
            }
        }
        return log;
    }

    record Pos(Coord coord, Dir dir) {
    }

    record FunctionHolder(Function a, Function b, Function c) {

        List<Long> getFunctionOrder() {
            final Map<Integer, Character> order = new HashMap<>();
            a.startIndexes().forEach(index -> order.put(index, 'A'));
            b.startIndexes().forEach(index -> order.put(index, 'B'));
            c.startIndexes().forEach(index -> order.put(index, 'C'));

            final List<Integer> keys = new ArrayList<>(order.keySet());
            Collections.sort(keys);

            List<Long> orderInstruction = new ArrayList<>();
            final Iterator<Integer> iter = keys.iterator();

            while (iter.hasNext()) {
                final Integer key = iter.next();
                orderInstruction.add((long) order.get(key));
                if (iter.hasNext()) {
                    orderInstruction.add((long) ',');
                } else {
                    orderInstruction.add((long) '\n');
                }
            }

            return orderInstruction;
        }

        static FunctionHolder from(final List<String> instructions) {
            int functionMaxLength = 10;
            FunctionHolder result = null;
            while (functionMaxLength-- > 0) {
                final List<Integer> reserved = new ArrayList<>();

                final Function functionA = Function.from(instructions, reserved);
                reserved.addAll(functionA.getReservedIndexes());

                final Function functionB = Function.from(instructions, reserved);
                reserved.addAll(functionB.getReservedIndexes());

                final Function functionC = Function.from(instructions, reserved);
                reserved.addAll(functionC.getReservedIndexes());
                if (reserved.size() == instructions.size()) {
                    result = new FunctionHolder(functionA, functionB, functionC);
                    break;
                }
            }
            return result;
        }
    }

    record Function(List<String> instructions, List<Integer> startIndexes) {
        int length() {
            return instructions.size();
        }

        List<Long> getFunctionOrder() {

            List<Long> instruction = new ArrayList<>();
            final Iterator<String> iter = instructions.iterator();

            while (iter.hasNext()) {
                final String next = iter.next();
                next.chars().forEach(c -> instruction.add((long)c));
                if (iter.hasNext()) {
                    instruction.add((long) ',');
                } else {
                    instruction.add((long) '\n');
                }
            }

            return instruction;
        }

        List<Integer> getReservedIndexes() {
            return startIndexes.stream()
                    .flatMapToInt(index -> IntStream.range(index, index + length()))
                    .boxed()
                    .collect(Collectors.toList());
        }

        static Function from(final List<String> instructions, List<Integer> reserved) {
            final List<Integer> foundIndexes = new ArrayList<>();
            int aLength = 1;

            int startFrom = 0;
            for (int index = 0; index < instructions.size(); index++) {
                if (!reserved.contains(index)) {
                    startFrom = index;
                    break;
                }
            }

            final String startChar = instructions.get(startFrom);
            for (int index = startFrom; index < instructions.size(); index++) {
                if (!reserved.contains(index) && instructions.get(index).equals(startChar)) {
                    foundIndexes.add(index);
                }
            }

            String nextPossible;
            for (int i = 1; i < 8; i++) {
                boolean stop = false;
                nextPossible = instructions.get(startFrom + i);
                final List<Integer> toRemove = new ArrayList<>();
                for (Integer index : foundIndexes) {
                    if (index + i >= instructions.size() || reserved.contains(index + i)) {
                        if (index == startFrom) {
                            stop = true;
                            break;
                        }
                        toRemove.add(index);
                    } else if (!instructions.get(index + i).equals(nextPossible)) {
                        toRemove.add(index);
                    }
                }
                if (toRemove.size() == foundIndexes.size() - 1 || stop) {
                    break;
                } else {
                    foundIndexes.removeAll(toRemove);
                    aLength++;
                }
            }
            final Function found;
            if (foundIndexes.isEmpty()) {
                found = new Function(Collections.emptyList(), foundIndexes);
            } else {
                found = new Function(instructions.subList(foundIndexes.get(0), foundIndexes.get(0) + aLength),
                        foundIndexes);
            }
            return found;
        }
    }

    private Set<Coord> prepareMap(final List<Long> asciiCodes) {
        final Set<Coord> map = new HashSet<>();

        int row = 0;
        int col = 0;
        for (Long code : asciiCodes) {
            if (code == '#' || Dir.isDir(code)) {
                map.add(Coord.of(row, col));
                col++;
            } else if (code == '\n') {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return map;
    }

    private void printMap(final List<Long> asciiCodes) {
        final StringBuilder builder = new StringBuilder();
        asciiCodes.forEach(l -> builder.append((char) l.longValue()));
        System.out.println(builder);
    }

    private Pos determineStartingPos(final List<Long> asciiCodes) {
        Pos start = null;
        int row = 0;
        int col = 0;
        for (Long code : asciiCodes) {
            if (Dir.isDir(code)) {
                start = new Pos(Coord.of(row, col), Dir.of(code));
                break;
            } else if (code == '\n') {
                col = 0;
                row++;
            } else {
                col++;
            }
        }
        return Objects.requireNonNull(start);
    }

}
