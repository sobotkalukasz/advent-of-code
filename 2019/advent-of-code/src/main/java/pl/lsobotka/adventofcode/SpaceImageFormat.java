package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2019/day/8
 * */
public class SpaceImageFormat {

    private final char BLACK = '0';
    private final char WHITE = '1';
    private final char TRANSPARENT = '2';

    private final int wide;
    private final int tall;
    private List<String> layers;

    public SpaceImageFormat(String encodedData, int wide, int tall) {
        this.wide = wide;
        this.tall = tall;
        initLayers(encodedData);
    }

    private void initLayers(String encodedData) {
        String spliterator = String.format("(?<=\\G.{%d})", wide * tall);
        layers = new ArrayList<>(List.of(encodedData.split(spliterator)));
    }

    public long getLayerCode() {
        Map<String, Long> collect = layers.stream().collect(Collectors.toMap(Function.identity(), layer -> countChar(layer, BLACK)));
        String layer = collect.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("");
        return countChar(layer, WHITE) * countChar(layer, TRANSPARENT);
    }

    private long countChar(CharSequence test, char c) {
        return test.chars().filter(ch -> ch == c).count();
    }

    public List<String> decodeImage() {
        StringBuilder layerBuilder = new StringBuilder();

        for (int i = 0; i < wide * tall; i++) {
            for (String layer : layers) {
                if (layer.charAt(i) != TRANSPARENT) {
                    layerBuilder.append(layer.charAt(i));
                    break;
                }
            }
        }
        String spliterator = String.format("(?<=\\G.{%d})", wide);
        return Stream.of(layerBuilder.toString().split(spliterator))
                .map(s -> s.replaceAll(String.valueOf(BLACK), "\s"))
                .collect(Collectors.toList());
    }
}
