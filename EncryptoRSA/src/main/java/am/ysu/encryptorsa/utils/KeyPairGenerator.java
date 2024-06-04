package am.ysu.encryptorsa.utils;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

@Component
@NoArgsConstructor
public class KeyPairGenerator {

    @SneakyThrows
    public KeyPair generateKeyPair(int bitLength) {
        BigInteger p = RandomPrimeGenerator.getPrimeNumber(bitLength / 2);
        BigInteger q = RandomPrimeGenerator.getPrimeNumber(bitLength / 2);
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = e.modInverse(phi);
        PublicKey rsaPublic = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(n, e));
        PrivateKey rsaPrivate = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(n, d));
        return new KeyPair(rsaPublic, rsaPrivate);
    }
}
