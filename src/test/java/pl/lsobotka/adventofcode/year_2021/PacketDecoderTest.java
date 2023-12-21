package pl.lsobotka.adventofcode.year_2021;

import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.lsobotka.adventofcode.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacketDecoderTest extends BaseTest {

    private static Stream<Arguments> exampleResource() {
        return Stream.of(Arguments.of("8A004A801A8002F478", 16), Arguments.of("620080001611562C8802118E34", 12),
                Arguments.of("C0015000016115A2E0802F182340", 23), Arguments.of("A0016C880162017C3686B18A3D4780", 31));
    }

    @ParameterizedTest
    @MethodSource("exampleResource")
    void exampleResourceTest(final String hexadecimal, final long expected) {
        final PacketDecoder packetDecoder = new PacketDecoder(hexadecimal);
        final PacketDecoder.Packet packet = packetDecoder.encodePacket();
        final int actual = packet.sumVersions();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testResourceFile() {
        return Stream.of(Arguments.of("2021/PacketDecoder", 934));
    }

    @ParameterizedTest
    @MethodSource("testResourceFile")
    void testResourceFileTest(final String fileName, final long expected) {
        final String hexadecimal = String.join("", getFileInput(fileName));

        final PacketDecoder packetDecoder = new PacketDecoder(hexadecimal);
        final PacketDecoder.Packet packet = packetDecoder.encodePacket();
        final int actual = packet.sumVersions();
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> exampleValueResource() {
        return Stream.of(Arguments.of("C200B40A82", 3), Arguments.of("04005AC33890", 54),
                Arguments.of("880086C3E88112", 7), Arguments.of("CE00C43D881120", 9), Arguments.of("D8005AC2A8F0", 1),
                Arguments.of("F600BC2D8F", 0), Arguments.of("9C005AC2F8F0", 0),
                Arguments.of("9C0141080250320F1802104A08", 1));
    }

    @ParameterizedTest
    @MethodSource("exampleValueResource")
    void exampleValueResourceTest(final String hexadecimal, final long expected) {
        final PacketDecoder packetDecoder = new PacketDecoder(hexadecimal);
        final PacketDecoder.Packet packet = packetDecoder.encodePacket();
        final BigInteger actual = packet.getValue();
        assertEquals(expected, actual.longValue());
    }

    private static Stream<Arguments> testValueResourceFile() {
        return Stream.of(Arguments.of("2021/PacketDecoder", 912901337844L));
    }

    @ParameterizedTest
    @MethodSource("testValueResourceFile")
    void testValueResourceFileTest(final String fileName, final long expected) {
        final String hexadecimal = String.join("", getFileInput(fileName));

        final PacketDecoder packetDecoder = new PacketDecoder(hexadecimal);
        final PacketDecoder.Packet packet = packetDecoder.encodePacket();
        final BigInteger actual = packet.getValue();
        assertEquals(expected, actual.longValue());
    }

}
