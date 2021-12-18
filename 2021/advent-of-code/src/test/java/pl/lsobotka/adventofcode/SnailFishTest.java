package pl.lsobotka.adventofcode;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SnailFishTest extends BaseTest {

    @Test
    public void parsing_simpleFish() {
        final String rawData = "[1,2]";
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().x(1).y(2).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishX() {
        final String rawData = "[[1,2],3]";
        final Snailfish.Fish child = new Snailfish.Fish.FishBuilder().x(1).y(2).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(child).y(3).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishY() {
        final String rawData = "[9,[8,7]]";
        final Snailfish.Fish child = new Snailfish.Fish.FishBuilder().x(8).y(7).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().x(9).fishY(child).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishXAndY() {
        final String rawData = "[[1,9],[8,5]]";
        final Snailfish.Fish fishX = new Snailfish.Fish.FishBuilder().x(1).y(9).build();
        final Snailfish.Fish fishY = new Snailfish.Fish.FishBuilder().x(8).y(5).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishX).fishY(fishY).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_complexExample() {
        final String rawData = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]";
        final Snailfish.Fish fishXa = new Snailfish.Fish.FishBuilder().x(1).y(2).build();
        final Snailfish.Fish fishYa = new Snailfish.Fish.FishBuilder().x(3).y(4).build();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().fishX(fishXa).fishY(fishYa).build();

        final Snailfish.Fish fishXb = new Snailfish.Fish.FishBuilder().x(5).y(6).build();
        final Snailfish.Fish fishYb = new Snailfish.Fish.FishBuilder().x(7).y(8).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().fishX(fishXb).fishY(fishYb).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().fishX(fishA).fishY(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishC).y(9).build();

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
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(0).y(9).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().fishX(fishA).y(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().fishX(fishB).y(3).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishC).y(4).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_right() {
        final String input = "[7,[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(7).y(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().x(5).fishY(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().x(6).fishY(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().x(7).fishY(fishC).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middle() {
        final String input = "[[6,[5,[4,[3,2]]]],1]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(7).y(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().x(5).fishY(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().x(6).fishY(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishC).y(3).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middleComplex() {
        final String input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(8).y(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().x(2).fishY(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().x(3).fishY(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().x(7).y(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().x(5).fishY(fishD).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().x(9).fishY(fishE).build();

        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishC).fishY(fishF).build();

        assertEquals(expected, fish);
    }

    @Test
    public void adding_simpleExample() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(1).y(1).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().x(2).y(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().x(3).y(3).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().x(4).y(4).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().fishX(fishA).fishY(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().fishX(fishE).fishY(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishF).fishY(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void adding_example() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().x(1).y(1).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().x(2).y(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().x(3).y(3).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().x(4).y(4).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().fishX(fishA).fishY(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().fishX(fishE).fishY(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().fishX(fishF).fishY(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }


}
