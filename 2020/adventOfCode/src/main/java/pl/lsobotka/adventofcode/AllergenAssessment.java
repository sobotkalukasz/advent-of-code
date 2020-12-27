package pl.lsobotka.adventofcode;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * https://adventofcode.com/2020/day/21
 * */
public class AllergenAssessment {

    List<Food> foods;
    List<String> ingredients;
    Set<String> allergens;
    Map<String, Set<String>> allergensMap;

    public AllergenAssessment(List<String> recipe) {
        foods = recipe.stream().map(Food::new).collect(Collectors.toList());
        ingredients = foods.stream().flatMap(food -> food.ingredients.stream()).distinct().collect(Collectors.toList());
        allergens = foods.stream().flatMap(food -> food.allergens.stream()).collect(Collectors.toSet());

        allergensMap = new HashMap<>();
        allergens.forEach(alle -> {
            if (!allergensMap.containsKey(alle)) {
                allergensMap.put(alle, new HashSet<>());
            }
            Set<String> strings = allergensMap.get(alle);
            foods.stream().filter(food -> food.allergens.contains(alle)).forEach(food -> {
                if (strings.isEmpty()) {
                    strings.addAll(food.ingredients);
                } else strings.retainAll(food.ingredients);
            });

        });
    }

    public long countTimes() {
        ingredients.removeAll(allergensMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        return ingredients.stream().mapToLong(ing -> foods.stream().filter(f -> f.ingredients.contains(ing)).count()).sum();
    }

    public String getAllergenicIngredients() {
        ingredients.removeAll(allergensMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet()));
        Map<String, String> match = new HashMap<>();
        while (!allergensMap.isEmpty()) {
            allergensMap.forEach((key, value) -> {
                if (value.size() == 1) {
                    match.put(key, value.stream().findFirst().orElse(""));
                }
            });
            match.keySet().forEach(k -> allergensMap.remove(k));
            match.values().forEach(v -> allergensMap.forEach((key, value) -> value.remove(v)));
        }
        return match.keySet().stream().sorted().map(match::get).collect(Collectors.joining(","));
    }
}

class Food {
    Set<String> allergens;
    Set<String> ingredients;

    Food(String input) {
        String[] split = input.split("contains");
        initIngredients(split[0]);
        initAllergens(split[1]);
    }

    private void initIngredients(String ing) {
        ingredients = Stream.of(ing.replace("(", "").split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    private void initAllergens(String alg) {
        allergens = Stream.of(alg.replace(")", "").split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

}
