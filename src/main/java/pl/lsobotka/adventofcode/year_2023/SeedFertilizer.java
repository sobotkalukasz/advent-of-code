package pl.lsobotka.adventofcode.year_2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        this.seedNumbers = parseSeedNumbers(inputAsSingleRow);
        this.mappingHolders = MAPPING_NAMES.stream().map(name -> MappingHolder.from(name, inputAsSingleRow)).toList();
    }

    long findLowestLocation() {
        return seedNumbers.stream().map(this::evaluateSeed).reduce(Long.MAX_VALUE, Long::min);
    }

    private long evaluateSeed(final long seed) {
        long actual = seed;
        for (MappingHolder mappingHolder : mappingHolders) {
            actual = mappingHolder.map(actual);
        }
        return actual;
    }

    long findLowestLocationForSeedRange() {
        final List<SeedRange> seeds = SeedRange.ofRanges(seedNumbers);

        final List<SeedRange> evaluatedRanges = new ArrayList<>();
        for (SeedRange seed : seeds) {
            evaluatedRanges.addAll(evaluateSeedRange(seed));
        }

        return evaluatedRanges.stream().map(SeedRange::start).reduce(Long.MAX_VALUE, Long::min);
    }

    private Set<SeedRange> evaluateSeedRange(final SeedRange seed) {
        final Set<SeedRange> nextIterationRanges = new HashSet<>(Collections.singletonList(seed));
        for (MappingHolder mappingHolder : mappingHolders) {
            final List<SeedRange> mappedRanges = new ArrayList<>();
            for (Mapping mapping : mappingHolder.mappings) {
                final List<SeedRange> leftToRemap = new ArrayList<>();
                for (SeedRange seedToMap : nextIterationRanges) {
                    mappedRanges.addAll(mapping.remapRange(seedToMap));
                    leftToRemap.addAll(mapping.getRangesLeftToRemap(seedToMap));
                }
                nextIterationRanges.clear();
                nextIterationRanges.addAll(leftToRemap);
            }
            nextIterationRanges.addAll(mappedRanges);
        }
        return nextIterationRanges;
    }

    private List<Long> parseSeedNumbers(final String inputAsSingleRow) {

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

    record Mapping(long destinationStart, long destinationEnd, long sourceStart, long sourceEnd) {
        boolean canMap(long toMap) {
            return toMap >= sourceStart && toMap <= sourceEnd;
        }

        long map(long toMap) {
            return toMap - sourceStart + destinationStart;
        }

        List<SeedRange> remapRange(final SeedRange seed) {
            final List<SeedRange> newSeeds = new ArrayList<>();

            if (seed.end < sourceStart || sourceEnd < seed.start) {
                // do nothing - nothing to remap
            } else if (seed.start >= sourceStart && seed.end <= sourceEnd) {
                final long offSet = seed.start - sourceStart;
                final long range = seed.end - seed.start;
                newSeeds.add(new SeedRange(destinationStart + offSet, destinationStart + offSet + range));
            } else if (seed.start >= sourceStart) {
                newSeeds.add(new SeedRange(destinationEnd - (sourceEnd - seed.start), destinationEnd));
            } else if (seed.end <= sourceEnd) {
                newSeeds.add(new SeedRange(destinationStart, destinationEnd - (sourceEnd - seed.end)));
            } else {
                newSeeds.add(new SeedRange(destinationStart, destinationEnd));
            }

            return newSeeds;
        }

        List<SeedRange> getRangesLeftToRemap(final SeedRange seed) {
            final List<SeedRange> newSeeds = new ArrayList<>();

            if (seed.end < sourceStart || sourceEnd < seed.start) {
                newSeeds.add(seed);
            } else if (seed.start >= sourceStart && seed.end <= sourceEnd) {
                // do nothing - fully evaluated
            } else if (seed.start >= sourceStart) {
                newSeeds.add(new SeedRange(sourceEnd + 1, seed.end));
            } else if (seed.end <= sourceEnd) {
                newSeeds.add(new SeedRange(seed.start, sourceStart - 1));
            } else {
                newSeeds.add(new SeedRange(seed.start, sourceStart - 1));
                newSeeds.add(new SeedRange(sourceEnd + 1, seed.end));
            }

            return newSeeds;
        }

        static Mapping from(final List<Long> numbers) {
            if (numbers.size() == 3) {
                final long destinationStart = numbers.get(0);
                final long sourceStart = numbers.get(1);
                final long range = numbers.get(2);
                return new Mapping(destinationStart, destinationStart + range, sourceStart, sourceStart + range);
            }
            throw new IllegalArgumentException("Unknown mapping numbers: " + numbers);
        }
    }

    record SeedRange(long start, long end) {

        static List<SeedRange> ofRanges(List<Long> ranges) {
            final List<SeedRange> seeds = new ArrayList<>();
            for (int i = 0; i < ranges.size() - 1; i = i + 2) {
                final long from = ranges.get(i);
                final long to = from + ranges.get(i + 1) - 1;
                seeds.add(new SeedRange(from, to));
            }
            return seeds;
        }

    }

}
