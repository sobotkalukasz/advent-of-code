package pl.lsobotka.adventofcode.year_2020;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MonsterMessages {

    static final Pattern anyDigit = Pattern.compile(".*\\d.*");
    static final Pattern digit = Pattern.compile("\\d+");
    static final Pattern letters = Pattern.compile("[ab]");

    private String[] rules;
    private ArrayList<String> messages;

    public int validateData(List<String> input){
        parseInput(input);

        String regex = "^" + makeRegex(rules, 0) + "$";
        Pattern pattern = Pattern.compile(regex);
        return (int)messages.stream().filter(message -> pattern.matcher(message).matches()).count();
    }

    public int validateDataFixedRule(List<String> input) {
        parseInput(input);

        String r42 = makeRegex(rules, 42);
        String r31 = makeRegex(rules, 31);

        String masterRegex = "^(" + createMasterRegex(r42, r31) + ")$";
        Pattern pattern = Pattern.compile(masterRegex);

        return (int)messages.stream().filter(message -> pattern.matcher(message).matches()).count();
    }

    private void parseInput(List<String> input){
        int size = (int)input.stream().filter(line -> line.contains(":")).count();
        rules = new String[size];
        messages = new ArrayList<>();
        input.forEach(line ->{
            if (line.contains(":")) {
                int i = Integer.parseInt(line.substring(0, line.indexOf(":")));
                rules[i] = line.substring(line.indexOf(":") + 2).replace("\"", "");
            } else if (!line.equals(""))
                messages.add(line);
        });
    }

    private String makeRegex(String[] rules, int i) {
        while (anyDigit.matcher(rules[i]).matches()) {
            String[] parts = rules[i].split(" ");
            StringBuilder rep = new StringBuilder();
            for (String part : parts) {
                if (digit.matcher(part).matches()) {
                    if (letters.matcher(rules[Integer.parseInt(part)]).matches()) {
                        rep.append(rules[Integer.parseInt(part)]);
                    } else {
                        rep.append("( ").append(rules[Integer.parseInt(part)]).append(" )");
                    }
                } else {
                    rep.append(part);
                }
            }
            rules[i] = rep.toString();
        }
        return "(" + rules[i] + ")";
    }

    private String createMasterRegex(String r1, String r2){
        StringBuilder regexBuilder = new StringBuilder();
        regexBuilder.append("(").append(r1).append("+)(");
        for (int i = 1; i < 10; i++) {
            regexBuilder.append("(");
            regexBuilder.append(String.valueOf(r1).repeat(i));
            regexBuilder.append(String.valueOf(r2).repeat(i));
            regexBuilder.append(")");
            if (i < 9) {
                regexBuilder.append("|");
            }
        }
        regexBuilder.append(")");
        return regexBuilder.toString();
    }

}
