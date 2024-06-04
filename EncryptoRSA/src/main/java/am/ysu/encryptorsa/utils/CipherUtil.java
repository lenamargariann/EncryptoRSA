package am.ysu.encryptorsa.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class CipherUtil {
    private final KeyFactory keyFactory = KeyFactory.getInstance("RSA");


    public CipherUtil() throws NoSuchAlgorithmException {
    }

    public String encrypt(String text, String publicKeyString) throws InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.decodeBase64(publicKeyString);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        BigInteger m = new BigInteger(text.getBytes(StandardCharsets.ISO_8859_1));
        BigInteger encrypted = m.modPow(rsaPublicKey.getPublicExponent(), rsaPublicKey.getModulus());
        byte[] encryptedBytes = Base64.encodeBase64(encrypted.toByteArray(), false);
        return new String(encryptedBytes, StandardCharsets.ISO_8859_1);
    }

    public String encryptChunks(String text, String publicKeyString) throws InvalidKeySpecException {
        StringBuilder cipher = new StringBuilder();
        int chunkSize = 64;
        for (int i = 0; i < text.length(); i += chunkSize) {
            String chunk = text.substring(i, Math.min(text.length(), i + chunkSize));
            String encryptedChunk = encrypt(chunk, publicKeyString);
            cipher.append(encryptedChunk);
        }
        return cipher.toString();
    }

    public String decryptChunks(String cipher, String privateKeyString) throws InvalidKeySpecException {
        StringBuilder plainText = new StringBuilder();
        String[] cipherArr = cipher.split("==");
        for (String s : cipherArr) {
            String tempPlainText = decrypt(s, privateKeyString);
            plainText.append(tempPlainText);
        }

        return plainText.toString();
    }


    public String decrypt(String text, String privateKeyString) throws InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.decodeBase64(privateKeyString);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        BigInteger c = new BigInteger(Base64.decodeBase64(text));
        BigInteger decrypted = c.modPow(rsaPrivateKey.getPrivateExponent(), rsaPrivateKey.getModulus());
        byte[] bytes = decrypted.toByteArray();
        return new String(bytes, StandardCharsets.ISO_8859_1);
    }


}
