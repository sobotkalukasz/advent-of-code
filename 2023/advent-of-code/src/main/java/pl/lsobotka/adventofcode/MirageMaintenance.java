package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MirageMaintenance {

    private final List<History> histories;

    public MirageMaintenance(final List<String> input) {
        histories = input.stream()
                .map(row -> Arrays.stream(row.split(" ")).map(Integer::valueOf).toList())
                .map(History::of)
                .toList();
    }

    public long sumOfNext() {
        return histories.stream().map(History::getLastNumber).reduce(0, Integer::sum);
    }

    public long sumOfPrevious() {
        return histories.stream().map(History::getPreviousNumber).reduce(0, Integer::sum);
    }

    record History(List<List<Integer>> numbers) {

        public History(final List<List<Integer>> numbers) {
            this.numbers = numbers;
            fillTillZero();
            calculateLast();
            calculateFirst();
        }

        public int getLastNumber() {
            return numbers.getFirst().getLast();
        }

        public int getPreviousNumber() {
            return numbers.getFirst().getFirst();
        }

        static History of(final List<Integer> row) {
            final List<List<Integer>> numbers = new ArrayList<>();
            numbers.add(new ArrayList<>(row));
            return new History(numbers);
        }

        private void fillTillZero() {

            while (!numbers.getLast().stream().allMatch(i -> i == 0)) {
                final List<Integer> last = numbers.getLast();
                final List<Integer> newRow = new ArrayList<>();
                for (int i = 1; i < last.size(); i++) {
                    newRow.add(last.get(i) - last.get(i - 1));
                }
                numbers.add(newRow);
            }
        }

        private void calculateLast() {
            final int lastIndex = numbers.size() - 1;

            int previous = 0;
            for (int i = lastIndex; i >= 0; i--) {
                if (i != lastIndex) {
                    previous = numbers.get(i).getLast() + previous;
                }
                numbers.get(i).add(previous);
            }
        }

        private void calculateFirst() {
            final int lastIndex = numbers.size() - 1;

            int previous = 0;
            for (int i = lastIndex; i >= 0; i--) {
                if (i != lastIndex) {
                    previous = numbers.get(i).getFirst() - previous;
                }
                numbers.get(i).addFirst(previous);
            }
        }

    }

}
