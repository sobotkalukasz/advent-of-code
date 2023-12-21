package pl.lsobotka.adventofcode.year_2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
 * https://adventofcode.com/2020/day/23
 * */
public class CrabCups {

    int[] cups;
    int maxValue;
    HashMap<Integer, Cup> cupMap;

    CrabCups(String input) {
        this(input, input.length());
    }

    CrabCups(String input, int size) {
        String[] split = input.split("");
        cups = new int[size];
        for (int i = 0; i < split.length; i++) {
            cups[i] = Integer.parseInt(split[i]);
        }
        int max = Arrays.stream(cups).max().orElse(0);
        for (int i = split.length; i < size; i++) {
            cups[i] = ++max;
        }
        maxValue = Arrays.stream(cups).max().orElse(0);
        initCupMap();
    }

    private void initCupMap() {
        cupMap = new HashMap<>();

        Cup head = new Cup(cups[0]);
        cupMap.put(head.value, head);
        Cup tail = head;

        for (int i = 1; i < cups.length; i++) {
            Cup c = new Cup(cups[i]);
            cupMap.put(c.value, c);
            c.next = head;
            tail.next = c;
            tail = c;
        }
    }

    public String playSimpleGame(int times) {
        Cup head = cupMap.get(cups[0]);
        while (times-- > 0) {
            head = move(head);
        }
        return printCups();
    }

    public long playAdvancedGame(int times) {
        Cup head = cupMap.get(cups[0]);
        while (times-- > 0) {
            head = move(head);
        }
        Cup one = cupMap.get(1);
        return Math.multiplyExact((long) one.next.value, one.next.next.value);
    }

    private Cup move(Cup head) {
        Cup c1 = head.next;
        Cup c3 = c1.next.next;

        head.next = c3.next;

        int targetIndex = head.value == 1 ? maxValue : head.value - 1;
        while (targetIndex == c1.value || targetIndex == c1.next.value || targetIndex == c3.value) {
            targetIndex--;
            targetIndex = targetIndex < 1 ? maxValue : targetIndex;
        }
        Cup target = cupMap.get(targetIndex);
        c3.next = target.next;
        target.next = c1;
        return head.next;
    }

    private String printCups() {
        List<Integer> ints = new ArrayList<>();
        Cup cup = cupMap.get(1);
        while (cup.next.value != 1) {
            cup = cup.next;
            ints.add(cup.value);
        }
        return ints.stream().map(Object::toString).collect(Collectors.joining());
    }

    private static class Cup {
        int value;
        Cup next;

        Cup(int value) {
            this.value = value;
        }
    }

}
