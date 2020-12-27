package pl.lsobotka.adventofcode;

import java.math.BigInteger;

/*
 * https://adventofcode.com/2020/day/25
 * */
public class ComboBreaker {
    BigInteger subject = BigInteger.valueOf(7);
    BigInteger modulus = BigInteger.valueOf(20201227);

    public long getEncryptionKey(int cardKey, int doorKey){
        BigInteger value = BigInteger.ONE;
        int loop = 0;
        BigInteger key = BigInteger.valueOf(cardKey);

        while(!value.equals(key)){
            value = value.multiply(subject).mod(modulus);
            loop++;
        }
        return BigInteger.valueOf(doorKey).modPow(BigInteger.valueOf(loop), modulus).longValue();
    }

}
