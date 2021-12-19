package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnailFishTest extends BaseTest {

    @Test
    public void parsing_simpleFish() {
        final String rawData = "[1,2]";
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().left(1).right(2).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishX() {
        final String rawData = "[[1,2],3]";
        final Snailfish.Fish child = new Snailfish.Fish.FishBuilder().left(1).right(2).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(child).right(3).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishY() {
        final String rawData = "[9,[8,7]]";
        final Snailfish.Fish child = new Snailfish.Fish.FishBuilder().left(8).right(7).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().left(9).fishRight(child).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishXAndY() {
        final String rawData = "[[1,9],[8,5]]";
        final Snailfish.Fish fishX = new Snailfish.Fish.FishBuilder().left(1).right(9).build();
        final Snailfish.Fish fishY = new Snailfish.Fish.FishBuilder().left(8).right(5).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishX).fishRight(fishY).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_complexExample() {
        final String rawData = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]";
        final Snailfish.Fish fishXa = new Snailfish.Fish.FishBuilder().left(1).right(2).build();
        final Snailfish.Fish fishYa = new Snailfish.Fish.FishBuilder().left(3).right(4).build();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().fishLeft(fishXa).fishRight(fishYa).build();

        final Snailfish.Fish fishXb = new Snailfish.Fish.FishBuilder().left(5).right(6).build();
        final Snailfish.Fish fishYb = new Snailfish.Fish.FishBuilder().left(7).right(8).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().fishLeft(fishXb).fishRight(fishYb).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().fishLeft(fishA).fishRight(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishC).right(9).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_notThrowException() {
        Snailfish.Fish.init("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]");
        Snailfish.Fish.init("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]");
    }

    @Test
    public void exploded_left() {
        final String input = "[[[[[9,8],1],2],3],4]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(0).right(9).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().fishLeft(fishA).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().fishLeft(fishB).right(3).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishC).right(4).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_right() {
        final String input = "[7,[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(5).fishRight(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(6).fishRight(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().left(7).fishRight(fishC).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middle() {
        final String input = "[[6,[5,[4,[3,2]]]],1]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(5).fishRight(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(6).fishRight(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishC).right(3).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middleComplex() {
        final String input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(8).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(2).fishRight(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(3).fishRight(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().left(5).fishRight(fishD).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().left(9).fishRight(fishE).build();

        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishC).fishRight(fishF).build();

        assertEquals(expected, fish);
    }

    @Test
    public void adding_simpleExample() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(1).right(1).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(2).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(3).right(3).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(4).right(4).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().fishLeft(fishA).fishRight(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().fishLeft(fishE).fishRight(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishF).fishRight(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void adding_example() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(3).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(5).right(3).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(4).right(4).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(5).right(5).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().fishLeft(fishA).fishRight(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().fishLeft(fishE).fishRight(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishF).fishRight(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void adding_complex() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(5).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(7).right(4).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().fishLeft(fishA).fishRight(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(5).right(5).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().fishLeft(fishC).fishRight(fishD).build();

        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().left(6).right(6).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishLeft(fishE).fishRight(fishF).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

}
