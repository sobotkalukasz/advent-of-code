package pl.lsobotka.adventofcode.year_2021;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BinaryDiagnostic {
    final Diagnostic diagnostic;

    public BinaryDiagnostic(final List<String> diagnosticReport) {
        this.diagnostic = new Diagnostic(diagnosticReport);
    }

    public long getPowerConsumption() {
        return diagnostic.getDecimalGamma() * diagnostic.getDecimalEpsilon();
    }

    public long getLifeSupport() {
        return diagnostic.getDecimalOxygen() * diagnostic.getDecimalCo();
    }

    private static class Diagnostic {
        private String gamma;
        private String epsilon;
        private String oxygen;
        private String co;

        private Diagnostic(final List<String> data) {
            initGamma(data);
            initEpsilon(this.gamma);
            initOxygen(data);
            initCO(data);
        }

        public long getDecimalGamma() {
            return Long.parseLong(gamma, 2);
        }

        public long getDecimalEpsilon() {
            return Long.parseLong(epsilon, 2);
        }

        public long getDecimalOxygen() {
            return Long.parseLong(oxygen, 2);
        }

        public long getDecimalCo() {
            return Long.parseLong(co, 2);
        }

        private void initGamma(final List<String> data) {
            final StringBuilder gammaBuilder = new StringBuilder();
            final int length = data.get(0).length();
            for (int i = 0; i < length; i++) {
                gammaBuilder.append(determineCommonBit(data, i));
            }
            this.gamma = gammaBuilder.toString();
        }

        private void initEpsilon(final String gamma) {
            final StringBuilder epsilonBuilder = new StringBuilder();
            for (char c : gamma.toCharArray()) {
                epsilonBuilder.append(c == '1' ? '0' : '1');
            }
            this.epsilon = epsilonBuilder.toString();
        }

        private void initOxygen(final List<String> data) {
            this.oxygen = filterData(data, c -> c);
        }

        private void initCO(final List<String> data) {
            this.co = filterData(data, c -> c == '1' ? '0' : '1');
        }

        private String filterData(final List<String> data, final Function<Character, Character> postProcess) {
            List<String> tempData = new ArrayList<>(data);
            final int length = data.get(0).length();
            for (int i = 0; i < length; i++) {
                final char c = postProcess.apply(determineCommonBit(tempData, i));
                final int index = i;
                tempData = tempData.stream().filter(str -> str.charAt(index) == c).collect(Collectors.toList());
                if (tempData.size() == 1) {
                    break;
                }
            }
            return tempData.get(0);
        }

        private char determineCommonBit(final List<String> data, final int position) {
            final double size = data.size();
            final long count = data.stream()
                    .map(str -> str.charAt(position))
                    .filter(c -> c.equals('1'))
                    .count();
            return count >= size / 2 ? '1' : '0';
        }
    }
}
