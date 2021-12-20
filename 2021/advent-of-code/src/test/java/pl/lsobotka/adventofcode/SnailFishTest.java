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
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(child).right(3).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishY() {
        final String rawData = "[9,[8,7]]";
        final Snailfish.Fish child = new Snailfish.Fish.FishBuilder().left(8).right(7).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().left(9).rightNode(child).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_withInnerFishXAndY() {
        final String rawData = "[[1,9],[8,5]]";
        final Snailfish.Fish fishX = new Snailfish.Fish.FishBuilder().left(1).right(9).build();
        final Snailfish.Fish fishY = new Snailfish.Fish.FishBuilder().left(8).right(5).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishX).rightNode(fishY).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void parsing_complexExample() {
        final String rawData = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]";
        final Snailfish.Fish fishXa = new Snailfish.Fish.FishBuilder().left(1).right(2).build();
        final Snailfish.Fish fishYa = new Snailfish.Fish.FishBuilder().left(3).right(4).build();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().leftNode(fishXa).rightNode(fishYa).build();

        final Snailfish.Fish fishXb = new Snailfish.Fish.FishBuilder().left(5).right(6).build();
        final Snailfish.Fish fishYb = new Snailfish.Fish.FishBuilder().left(7).right(8).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().leftNode(fishXb).rightNode(fishYb).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishC).right(9).build();

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
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().leftNode(fishA).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().leftNode(fishB).right(3).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishC).right(4).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_right() {
        final String input = "[7,[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(5).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(6).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().left(7).rightNode(fishC).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middle() {
        final String input = "[[6,[5,[4,[3,2]]]],1]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(5).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(6).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishC).right(3).build();

        assertEquals(expected, fish);
    }

    @Test
    public void exploded_middleComplex() {
        final String input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(8).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(2).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(3).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(7).right(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().left(5).rightNode(fishD).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().left(9).rightNode(fishE).build();

        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishC).rightNode(fishF).build();

        assertEquals(expected, fish);
    }

    @Test
    public void adding_simpleExample() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(1).right(1).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(2).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(3).right(3).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(4).right(4).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().leftNode(fishE).rightNode(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishF).rightNode(fishD).build();

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

        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().leftNode(fishE).rightNode(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishF).rightNode(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void adding_complex() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(5).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(7).right(4).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().leftNode(fishA).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(5).right(5).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().leftNode(fishC).rightNode(fishD).build();

        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().left(6).right(6).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishE).rightNode(fishF).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void split_simpleExample() {
        final List<String> rawData = Arrays.asList("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]");
        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(0).right(7).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().leftNode(fishA).right(4).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().left(7).right(8).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(6).right(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().leftNode(fishC).rightNode(fishD).build();

        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().leftNode(fishB).rightNode(fishE).build();
        final Snailfish.Fish fishG = new Snailfish.Fish.FishBuilder().left(8).right(1).build();
        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishF).rightNode(fishG).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    public void split_largeExample() {
        final List<String> rawData = Arrays.asList("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]", "[7,[5,[[3,8],[1,4]]]]",
                "[[2,[2,2]],[8,[8,1]]]", "[2,9]", "[1,[[[9,3],9],[[9,0],[0,7]]]]", "[[[5,[7,4]],7],1]",
                "[[[[4,2],2],6],[8,7]]");

        final Snailfish.Fish fishA = new Snailfish.Fish.FishBuilder().left(8).right(7).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.FishBuilder().left(7).right(7).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.FishBuilder().leftNode(fishA).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.FishBuilder().left(8).right(6).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.FishBuilder().left(7).right(7).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.FishBuilder().leftNode(fishD).rightNode(fishE).build();

        final Snailfish.Fish fishG = new Snailfish.Fish.FishBuilder().leftNode(fishC).rightNode(fishF).build();

        final Snailfish.Fish fishH = new Snailfish.Fish.FishBuilder().left(0).right(7).build();
        final Snailfish.Fish fishI = new Snailfish.Fish.FishBuilder().left(6).right(6).build();
        final Snailfish.Fish fishJ = new Snailfish.Fish.FishBuilder().leftNode(fishH).rightNode(fishI).build();

        final Snailfish.Fish fishK = new Snailfish.Fish.FishBuilder().left(8).right(7).build();
        final Snailfish.Fish fishL = new Snailfish.Fish.FishBuilder().leftNode(fishJ).rightNode(fishK).build();

        final Snailfish.Fish expected = new Snailfish.Fish.FishBuilder().leftNode(fishG).rightNode(fishL).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

}
