package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpaceImageFormat {

    private final int wide;
    private final int tall;
    private List<String> layers;

    public SpaceImageFormat(String encodedData, int wide, int tall) {
        this.wide = wide;
        this.tall = tall;
        initLayers(encodedData);
    }

    private void initLayers(String encodedData) {
        layers = new ArrayList<>();
        int pixels = wide * tall;
        int layerCount = encodedData.length() / pixels;
        for (int i = 0; i < layerCount; i++) {
            layers.add(encodedData.substring(i * pixels, i * pixels + pixels));
        }
    }

    public long getLayerCode() {
        Map<String, Long> collect = layers.stream().collect(Collectors.toMap(Function.identity(), layer -> countChar(layer, '0')));
        String layer = collect.entrySet().stream().min(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("");
        return countChar(layer, '1') * countChar(layer, '2');
    }

    private long countChar(CharSequence test, char c) {
        return test.chars().filter(ch -> ch == c).count();
    }
}
