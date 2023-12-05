package pl.lsobotka.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * https://adventofcode.com/2023/day/5
 * */
public class SeedFertilizer {

    private static final List<String> MAPPING_NAMES = Arrays.asList("seed-to-soil", "soil-to-fertilizer",
            "fertilizer-to-water", "water-to-light", "light-to-temperature", "temperature-to-humidity",
            "humidity-to-location");
    private static final Pattern SEED_PATTERN = Pattern.compile("seeds:\\s([0-9\\s]+)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("([0-9]+)");

    private final List<Long> seedNumbers;
    private final List<MappingHolder> mappingHolders;

    public SeedFertilizer(final List<String> input) {

        final String inputAsSingleRow = String.join(" ", input);

        this.seedNumbers = getSeeds(inputAsSingleRow);
        this.mappingHolders = MAPPING_NAMES.stream().map(name -> MappingHolder.from(name, inputAsSingleRow)).toList();

    }

    long findLowestLocation() {
        return seedNumbers.stream().map(s -> {
            long actual = s;
            for (MappingHolder mappingHolder : mappingHolders) {
                actual = mappingHolder.map(actual);
            }
            return actual;
        }).reduce(Long.MAX_VALUE, Long::min);
    }

    /*
    * A reverse brute force solution that I'm not satisfied with
    * */
    long findLowestLocationForSeedRange() {

        List<Seed> seeds = new ArrayList<>();
        for (int i = 0; i < seedNumbers.size() - 1; i = i + 2) {
            seeds.add(new Seed(seedNumbers.get(i), seedNumbers.get(i + 1)));
        }

        final List<MappingHolder> reversedMappings = new ArrayList<>(mappingHolders);
        Collections.reverse(reversedMappings);

        long location = 0;
        for (; location < Integer.MAX_VALUE; location++) {
            long actual = location;
            for (MappingHolder mappingHolder : reversedMappings) {
                actual = mappingHolder.reverse(actual);
            }
            final long possibleSeed = actual;
            if (seeds.stream().anyMatch(s -> s.exists(possibleSeed))) {
                break;
            }
        }
        return location;
    }

    private List<Long> getSeeds(final String inputAsSingleRow) {

        final Matcher seedMatcher = SEED_PATTERN.matcher(inputAsSingleRow);
        if (seedMatcher.find()) {
            return NUMBER_PATTERN.matcher(seedMatcher.group(1))
                    .results()
                    .map(MatchResult::group)
                    .map(Long::parseLong)
                    .toList();
        }
        throw new IllegalArgumentException("Unknown to retrieve seeds");

    }

    record MappingHolder(String name, List<Mapping> mappings) {
        long map(long toMap) {
            return mappings.stream().filter(m -> m.canMap(toMap)).map(m -> m.map(toMap)).findFirst().orElse(toMap);
        }

        long reverse(long toMap) {
            return mappings.stream()
                    .filter(m -> m.canReverse(toMap))
                    .map(m -> m.reverse(toMap))
                    .findFirst()
                    .orElse(toMap);
        }

        static MappingHolder from(final String name, final String input) {
            final Pattern pattern = Pattern.compile(name + " map:([0-9\\s]+)");

            final Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                final String numbers = matcher.group(1);
                final List<Mapping> mappings = toMappings(numbers);
                return new MappingHolder(name, mappings);
            } else {
                throw new IllegalArgumentException("Unknown map: " + name);
            }
        }

        private static List<Mapping> toMappings(final String numberString) {
            final List<Long> numbers = NUMBER_PATTERN.matcher(numberString)
                    .results()
                    .map(MatchResult::group)
                    .map(Long::parseLong)
                    .toList();

            final Collection<List<Long>> values = IntStream.range(0, numbers.size())
                    .boxed()
                    .collect(Collectors.groupingBy(partition -> (partition / 3),
                            Collectors.mapping(numbers::get, Collectors.toList())))
                    .values();

            return values.stream().map(Mapping::from).toList();
        }

    }

    record Mapping(long destination, long source, long range) {
        boolean canMap(long toMap) {
            return toMap >= source && toMap <= source + range;
        }

        boolean canReverse(long toMap) {
            return toMap >= destination && toMap <= destination + range;
        }

        long map(long toMap) {
            return toMap - source + destination;
        }

        long reverse(long toMap) {
            return toMap - destination + source;
        }

        static Mapping from(final List<Long> numbers) {
            if (numbers.size() == 3) {
                return new Mapping(numbers.get(0), numbers.get(1), numbers.get(2));
            }
            throw new IllegalArgumentException("Unknown mapping numbers: " + numbers);
        }
    }

    record Seed(long from, long range) {
        boolean exists(long possibleSeed) {
            return possibleSeed >= from && possibleSeed <= from + range;
        }
    }

}
