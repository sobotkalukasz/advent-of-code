package pl.lsobotka.adventofcode.year_2020;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/*
 * https://adventofcode.com/2020/day/4
 * */

public class PassportProcessing {

    public static String BIRTH_YEAR = "byr";
    public static String ISSUE_YEAR = "iyr";
    public static String EXPIRATION_YEAR = "eyr";
    public static String HEIGHT = "hgt";
    public static String HAIR_COLOUR = "hcl";
    public static String EYE_COLOUR = "ecl";
    public static String PASSPORT_ID = "pid";

    private static final Pattern hairPattern = Pattern.compile("#[0-9a-f]{6}");
    private static final Pattern idPattern = Pattern.compile("\\d{9}");

    private static final List<String> eyes = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static final Predicate<Map<String, String>> birthPredicate = passport -> {
        int min = 1920;
        int max = 2002;
        int year = Integer.parseInt(passport.get(BIRTH_YEAR));
        return isBetweenInclusive(year, min, max);
    };

    private static final Predicate<Map<String, String>> issuePredicate = passport -> {
        int min = 2010;
        int max = 2020;
        int year = Integer.parseInt(passport.get(ISSUE_YEAR));
        return isBetweenInclusive(year, min, max);
    };

    private static final Predicate<Map<String, String>> expirationPredicate = passport -> {
        int min = 2020;
        int max = 2030;
        int year = Integer.parseInt(passport.get(EXPIRATION_YEAR));
        return isBetweenInclusive(year, min, max);
    };

    private static final Predicate<Map<String, String>> heightPredicate = passport -> {
        String height = passport.get(HEIGHT);
        if(height.contains("cm")){
            int min = 150;
            int max = 193;
            int cm = Integer.parseInt(height.replace("cm", ""));
            return isBetweenInclusive(cm, min, max);

        } else if(height.contains("in")){
            int min = 59;
            int max = 76;
            int in = Integer.parseInt(height.replace("in", ""));
            return isBetweenInclusive(in, min, max);
        }
        return false;
    };

    private static final Predicate<Map<String, String>> hairPredicate = passport -> {
        String hair = passport.get(HAIR_COLOUR);
        return hair.length() == 7 && hairPattern.matcher(hair).matches();
    };

    private static final Predicate<Map<String, String>> eyePredicate = passport -> eyes.contains(passport.get(EYE_COLOUR));

    private static final Predicate<Map<String, String>> idPredicate = passport -> {
        String id = passport.get(PASSPORT_ID);
        return id.length() == 9 && idPattern.matcher(id).matches();
    };

    private static boolean isBetweenInclusive(int test, int min, int max){
        return test >= min && test <= max;
    }

    public static Predicate<Map<String, String>> simplePredicate = passport -> {
        if(passport.size() < 7) return false;
        if(!passport.containsKey(BIRTH_YEAR)) return false;
        if(!passport.containsKey(ISSUE_YEAR)) return false;
        if(!passport.containsKey(EXPIRATION_YEAR)) return false;
        if(!passport.containsKey(HEIGHT)) return false;
        if(!passport.containsKey(HAIR_COLOUR)) return false;
        if(!passport.containsKey(EYE_COLOUR)) return false;
        return passport.containsKey(PASSPORT_ID);
    };

    public static Predicate<Map<String, String>> complexPredicate = simplePredicate.and(birthPredicate)
            .and(issuePredicate)
            .and(expirationPredicate)
            .and(heightPredicate)
            .and(hairPredicate)
            .and(eyePredicate)
            .and(idPredicate);

    public long countValidPassports(List<Map<String, String>> passports, Predicate<Map<String, String>> passportPredicate){
        return passports.stream().filter(passportPredicate).count();
    }
}
