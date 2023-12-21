package pl.lsobotka.adventofcode.year_2021;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

/*
 * https://adventofcode.com/2021/day/16
 * */
public class PacketDecoder {

    private final String binary;
    private int index = 0;

    public PacketDecoder(String hexadecimal) {
        this.binary = toBinaryWithLeadingZero(hexadecimal);
    }

    private String toBinaryWithLeadingZero(final String hexadecimal) {
        final int binaryLength = hexadecimal.length() * 4;
        final String tempBinary = new BigInteger(hexadecimal, 16).toString(2);
        final int diff = binaryLength - tempBinary.length();
        return "0".repeat(diff).concat(tempBinary);
    }

    public Packet encodePacket() {
        final int version = encodeVersion();
        final PacketType type = PacketType.getTypeByValue(encodeType());

        return switch (type) {
            case LITERAL -> encodeLiteral(version, type);
            case SUM, PRODUCT, MIN, MAX, GREATER_THAN, LESS_THAN, EQUAL_TO -> encodeOperator(version, type);
        };
    }

    private int encodeVersion() {
        int start = index;
        int end = index + 3;
        index = end;
        return encode(start, end);
    }

    private int encodeType() {
        int start = index;
        int end = index + 3;
        index = end;
        return encode(start, end);
    }

    private int encode(final int start, final int end) {
        final String binary = this.binary.substring(start, end);
        return new BigInteger(binary, 2).intValue();
    }

    private Packet encodeLiteral(final int version, final PacketType type) {
        final StringBuilder literalBuilder = new StringBuilder();
        char nextBit;
        do {
            int start = index;
            int end = index + 5;
            index = end;
            final String tempBinary = this.binary.substring(start, end);
            nextBit = tempBinary.charAt(0);
            literalBuilder.append(tempBinary.substring(1));
        } while (nextBit == '1');

        final BigInteger value = new BigInteger(literalBuilder.toString(), 2);
        return Packet.literal(version, type, value);
    }

    private Packet encodeOperator(final int version, final PacketType type) {
        final char lengthTypeId = binary.charAt(index);
        index++;

        final List<Packet> subPacket = new ArrayList<>();
        if (lengthTypeId == '0') {
            int start = index;
            int end = index + 15;
            index = end;
            final int subPacketLength = encode(start, end);
            final int endOfSubPackages = index + subPacketLength;
            while (index < endOfSubPackages) {
                final Packet packet = encodePacket();
                subPacket.add(packet);
            }

        } else {
            int start = index;
            int end = index + 11;
            index = end;
            int numberOfSubPacket = encode(start, end);
            do {
                final Packet packet = encodePacket();
                subPacket.add(packet);
                numberOfSubPacket--;
            } while (numberOfSubPacket > 0);
        }

        return Packet.operator(version, type, subPacket);
    }

    record Packet(int version, PacketType type, BigInteger value, List<Packet> subPacket) {

        public static Packet literal(final int version, final PacketType type, final BigInteger value) {
            return new Packet(version, type, value, Collections.emptyList());
        }

        public static Packet operator(final int version, final PacketType type, final List<Packet> subPacket) {
            return new Packet(version, type, BigInteger.ZERO, subPacket);
        }

        public int sumVersions() {
            return subPacket.stream().map(Packet::sumVersions).reduce(Integer::sum).orElse(0) + version;
        }

        public BigInteger getValue() {
            return switch (type) {
                case LITERAL -> value;
                case SUM -> subPacket.stream().map(Packet::getValue).reduce(BigInteger::add).orElse(BigInteger.ZERO);
                case PRODUCT -> subPacket.stream()
                        .map(Packet::getValue)
                        .reduce(BigInteger::multiply)
                        .orElse(BigInteger.ZERO);
                case MIN -> subPacket.stream().map(Packet::getValue).min(BigInteger::compareTo).orElse(BigInteger.ZERO);
                case MAX -> subPacket.stream().map(Packet::getValue).max(BigInteger::compareTo).orElse(BigInteger.ZERO);
                case GREATER_THAN -> checkValue((a, b) -> a.compareTo(b) > 0);
                case LESS_THAN -> checkValue((a, b) -> a.compareTo(b) < 0);
                case EQUAL_TO -> checkValue((a, b) -> a.compareTo(b) == 0);
            };
        }

        private BigInteger checkValue(final BiPredicate<BigInteger, BigInteger> check) {
            final Packet a = subPacket.get(0);
            final Packet b = subPacket.get(1);
            if (check.test(a.getValue(), b.getValue())) {
                return BigInteger.ONE;
            } else {
                return BigInteger.ZERO;
            }
        }
    }

    private enum PacketType {
        LITERAL(4), SUM(0), PRODUCT(1), MIN(2), MAX(3), GREATER_THAN(5), LESS_THAN(6), EQUAL_TO(7);

        private final int value;

        PacketType(int value) {
            this.value = value;
        }

        public static PacketType getTypeByValue(final int value) {
            return Arrays.stream(PacketType.values())
                    .filter(t -> t.value == value)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

    }
}
