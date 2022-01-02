package pl.lsobotka.adventofcode.transparentorigami;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransparentOrigamiTest extends BaseTest {

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("TransparentOrigami_example", 1, 17), Arguments.of("TransparentOrigami", 1, 621));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    public void testResourceFileTest(final String fileName, final int qtyOfFold, final int expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final TransparentOrigami transparentOrigami = new TransparentOrigami(input);
        final long actual = transparentOrigami.foldAndCountDots(qtyOfFold);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> codeFile() {
        return Stream.of( Arguments.of("TransparentOrigami", "HKUJGAJZ"));
    }

    @ParameterizedTest
    @MethodSource("codeFile")
    public void codeFileTest(final String fileName, final String expected) throws Exception {
        final List<String> input = getFileInput(fileName);

        final TransparentOrigami transparentOrigami = new TransparentOrigami(input);
        transparentOrigami.foldAndPrintCode();
        assertEquals(expected, "HKUJGAJZ");
    }


}
