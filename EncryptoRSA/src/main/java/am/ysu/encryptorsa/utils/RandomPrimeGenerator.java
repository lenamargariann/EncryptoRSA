package am.ysu.encryptorsa.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomPrimeGenerator {

    private static final int[] FIRST_PRIMES_LIST = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349};

    private static BigInteger nBitRandom(int n) {
        BigInteger p = BigInteger.probablePrime(n, new SecureRandom());
        p = p.setBit(n - 1).setBit(0);
        return p;
    }

    private static BigInteger getLowLevelPrime(int n) {
        while (true) {
            BigInteger primeCandidate = nBitRandom(n);
            for (int divisor : FIRST_PRIMES_LIST) {
                if (primeCandidate.remainder(BigInteger.valueOf(divisor)).equals(BigInteger.ZERO) && BigInteger.valueOf(divisor).pow(2).compareTo(primeCandidate) <= 0) {
                    break;
                }
            }

            return primeCandidate;
        }
    }

    private static boolean isMillerRabinPassed(BigInteger primeNum) {
        BigInteger ec = primeNum.subtract(BigInteger.ONE);
        while (ec.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            ec = ec.divide(BigInteger.TWO);
        }
        int numberOfRabinTrials = 20;
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < numberOfRabinTrials; i++) {
            BigInteger roundTester = new BigInteger(primeNum.bitLength(), random);
            if (roundTester.compareTo(BigInteger.TWO) < 0 || roundTester.compareTo(primeNum) >= 0) {
                roundTester = BigInteger.TWO.add(roundTester.mod(primeNum.subtract(BigInteger.TWO)));
            }

            if (trialComposite(roundTester, ec, primeNum)) {
                return false;
            }
        }

        return true;
    }

    private static boolean trialComposite(BigInteger roundTester, BigInteger ec, BigInteger mrc) {
        if (roundTester.modPow(ec, mrc).equals(BigInteger.ONE)) {
            return false;
        }

        for (int i = 0; i < ec.bitLength(); i++) {
            if (roundTester.modPow(BigInteger.TWO.pow(i).multiply(ec), mrc).equals(mrc.subtract(BigInteger.ONE))) {
                return false;
            }
        }

        return true;
    }

    public static BigInteger getPrimeNumber(int bitLength) {
        while (true) {
            BigInteger primeCandidate = getLowLevelPrime(bitLength);
            if (isMillerRabinPassed(primeCandidate)) {
                return primeCandidate;
            }
        }
    }

}

