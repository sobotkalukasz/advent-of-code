package pl.lsobotka.adventofcode.year_2021;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SnailFishTest extends BaseTest {

    @Test
    void parsing_simpleFish() {
        final String rawData = "[1,2]";
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().left(1).right(2).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void parsing_withInnerFishX() {
        final String rawData = "[[1,2],3]";
        final Snailfish.Fish child = new Snailfish.Fish.Builder().left(1).right(2).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(child).right(3).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void parsing_withInnerFishY() {
        final String rawData = "[9,[8,7]]";
        final Snailfish.Fish child = new Snailfish.Fish.Builder().left(8).right(7).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().left(9).rightNode(child).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void parsing_withInnerFishXAndY() {
        final String rawData = "[[1,9],[8,5]]";
        final Snailfish.Fish leftNode = new Snailfish.Fish.Builder().left(1).right(9).build();
        final Snailfish.Fish rightNode = new Snailfish.Fish.Builder().left(8).right(5).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(leftNode).rightNode(rightNode).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void parsing_complexExample() {
        final String rawData = "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]";
        final Snailfish.Fish fishXa = new Snailfish.Fish.Builder().left(1).right(2).build();
        final Snailfish.Fish fishYa = new Snailfish.Fish.Builder().left(3).right(4).build();
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().leftNode(fishXa).rightNode(fishYa).build();

        final Snailfish.Fish fishXb = new Snailfish.Fish.Builder().left(5).right(6).build();
        final Snailfish.Fish fishYb = new Snailfish.Fish.Builder().left(7).right(8).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().leftNode(fishXb).rightNode(fishYb).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishC).right(9).build();

        final Snailfish.Fish actual = Snailfish.Fish.init(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void parsing_notThrowException() {
        assertDoesNotThrow(() -> Snailfish.Fish.init("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]"));
        assertDoesNotThrow(() -> Snailfish.Fish.init("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"));
    }

    @Test
    void exploded_left() {
        final String input = "[[[[[9,8],1],2],3],4]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(0).right(9).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().leftNode(fishA).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().leftNode(fishB).right(3).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishC).right(4).build();

        assertEquals(expected, fish);
    }

    @Test
    void exploded_right() {
        final String input = "[7,[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(5).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(6).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().left(7).rightNode(fishC).build();

        assertEquals(expected, fish);
    }

    @Test
    void exploded_middle() {
        final String input = "[[6,[5,[4,[3,2]]]],1]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(7).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(5).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(6).rightNode(fishB).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishC).right(3).build();

        assertEquals(expected, fish);
    }

    @Test
    void exploded_middleComplex() {
        final String input = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]";
        final Snailfish.Fish fish = Snailfish.Fish.init(input);
        fish.validate();

        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(8).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(2).rightNode(fishA).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(3).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(7).right(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().left(5).rightNode(fishD).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().left(9).rightNode(fishE).build();

        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishC).rightNode(fishF).build();

        assertEquals(expected, fish);
    }

    @Test
    void adding_simpleExample() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]");
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(1).right(1).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(2).right(2).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(3).right(3).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(4).right(4).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().leftNode(fishE).rightNode(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishF).rightNode(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void adding_example() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]");
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(3).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(5).right(3).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(4).right(4).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(5).right(5).build();

        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().leftNode(fishA).rightNode(fishB).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().leftNode(fishE).rightNode(fishC).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishF).rightNode(fishD).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void adding_complex() {
        final List<String> rawData = Arrays.asList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]");
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(5).right(0).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(7).right(4).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().leftNode(fishA).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(5).right(5).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().leftNode(fishC).rightNode(fishD).build();

        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().left(6).right(6).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishE).rightNode(fishF).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void split_simpleExample() {
        final List<String> rawData = Arrays.asList("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]");
        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(0).right(7).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().leftNode(fishA).right(4).build();

        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().left(7).right(8).build();
        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(6).right(0).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().leftNode(fishC).rightNode(fishD).build();

        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().leftNode(fishB).rightNode(fishE).build();
        final Snailfish.Fish fishG = new Snailfish.Fish.Builder().left(8).right(1).build();
        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishF).rightNode(fishG).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    @Test
    void split_largeExample() {
        final List<String> rawData = Arrays.asList("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]", "[7,[5,[[3,8],[1,4]]]]",
                "[[2,[2,2]],[8,[8,1]]]", "[2,9]", "[1,[[[9,3],9],[[9,0],[0,7]]]]", "[[[5,[7,4]],7],1]",
                "[[[[4,2],2],6],[8,7]]");

        final Snailfish.Fish fishA = new Snailfish.Fish.Builder().left(8).right(7).build();
        final Snailfish.Fish fishB = new Snailfish.Fish.Builder().left(7).right(7).build();
        final Snailfish.Fish fishC = new Snailfish.Fish.Builder().leftNode(fishA).rightNode(fishB).build();

        final Snailfish.Fish fishD = new Snailfish.Fish.Builder().left(8).right(6).build();
        final Snailfish.Fish fishE = new Snailfish.Fish.Builder().left(7).right(7).build();
        final Snailfish.Fish fishF = new Snailfish.Fish.Builder().leftNode(fishD).rightNode(fishE).build();

        final Snailfish.Fish fishG = new Snailfish.Fish.Builder().leftNode(fishC).rightNode(fishF).build();

        final Snailfish.Fish fishH = new Snailfish.Fish.Builder().left(0).right(7).build();
        final Snailfish.Fish fishI = new Snailfish.Fish.Builder().left(6).right(6).build();
        final Snailfish.Fish fishJ = new Snailfish.Fish.Builder().leftNode(fishH).rightNode(fishI).build();

        final Snailfish.Fish fishK = new Snailfish.Fish.Builder().left(8).right(7).build();
        final Snailfish.Fish fishL = new Snailfish.Fish.Builder().leftNode(fishJ).rightNode(fishK).build();

        final Snailfish.Fish expected = new Snailfish.Fish.Builder().leftNode(fishG).rightNode(fishL).build();

        final Snailfish.Fish actual = Snailfish.add(rawData);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> magnitudeExample() {
        return Stream.of(Arguments.of("[[9,1],[1,9]]", 129), //
                Arguments.of("[[1,2],[[3,4],5]]", 143),//
                Arguments.of("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 1384),//
                Arguments.of("[[[[1,1],[2,2]],[3,3]],[4,4]]", 445),//
                Arguments.of("[[[[3,0],[5,3]],[4,4]],[5,5]]", 791),//
                Arguments.of("[[[[5,0],[7,4]],[5,5]],[6,6]]", 1137),//
                Arguments.of("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", 3488));
    }

    @ParameterizedTest
    @MethodSource("magnitudeExample")
    void magnitude_example(final String rawData, final long expected) {
        final Snailfish.Fish fish = Snailfish.Fish.init(rawData);
        final long actual = fish.getMagnitude();
        assertEquals(expected, actual);
    }

    @Test
    void magnitude_complexExample() {
        final List<String> rawData = Arrays.asList("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                "[[[5,[2,8]],4],[5,[[9,9],0]]]", "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]", "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]", "[[[[5,4],[7,7]],8],[[8,3],8]]", "[[9,3],[[9,9],[6,[4,9]]]]",
                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]", "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");

        final Snailfish.Fish fish = Snailfish.add(rawData);
        final long actual = fish.getMagnitude();
        assertEquals(4140, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/SnailFish", 4057));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void magnitude_testResourceFile(final String fileName, final long expected) {
        final List<String> rawData = getFileInput(fileName);

        final Snailfish.Fish fish = Snailfish.add(rawData);
        final long actual = fish.getMagnitude();
        assertEquals(expected, actual);
    }

    @Test
    void magnitude_findBiggestMagnitudeExample() {
        final List<String> rawData = Arrays.asList("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
                "[[[5,[2,8]],4],[5,[[9,9],0]]]", "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]", "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]", "[[[[5,4],[7,7]],8],[[8,3],8]]", "[[9,3],[[9,9],[6,[4,9]]]]",
                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]", "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");

        final long actual = Snailfish.findBiggestMagnitude(rawData);
        assertEquals(3993, actual);
    }

    private static Stream<Arguments> findBiggestMagnitudeFile() {
        return Stream.of(Arguments.of("2021/SnailFish", 4683));
    }

    @ParameterizedTest
    @MethodSource("findBiggestMagnitudeFile")
    void magnitude_findBiggestMagnitude(final String fileName, final long expected) {
        final List<String> rawData = getFileInput(fileName);

        final long actual = Snailfish.findBiggestMagnitude(rawData);
        assertEquals(expected, actual);
    }

}
