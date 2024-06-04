package am.ysu.encryptorsa.service;

import am.ysu.encryptorsa.model.Base64RSAKey;
import am.ysu.encryptorsa.utils.KeyPairGenerator;
import lombok.RequiredArgsConstructor;
import am.ysu.encryptorsa.utils.CipherUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainService {
    private final KeyPairGenerator keyPairGenerator;
    private final CipherUtil cipherUtil;

    public Base64RSAKey generateKeyPair(int bitLength) {
        long startTime = System.currentTimeMillis();
        KeyPair keyPair = keyPairGenerator.generateKeyPair(bitLength);
        long endTime = System.currentTimeMillis();
        long durationMs = endTime - startTime;
        return new Base64RSAKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()), Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()), durationMs);
    }

    public Map<String, String> encryptText(String message, String publicKey) {
        try {
            long startTime = System.currentTimeMillis();
            String cipher = cipherUtil.encryptChunks(message, publicKey);
            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;
            return Map.of("cipher", cipher, "duration", String.valueOf(durationMs));
        } catch (InvalidKeySpecException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid public key.");
        }
    }

    public Map<String, String> decryptText(String cipher, String privateKey) {
        try {
            long startTime = System.currentTimeMillis();
            String message = cipherUtil.decryptChunks(cipher, privateKey);
            long endTime = System.currentTimeMillis();
            long durationMs = endTime - startTime;
            return Map.of("message", message, "duration", String.valueOf(durationMs));

        } catch (InvalidKeySpecException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid private key.");

        }
    }

    public String convertToPEM(Boolean isPublic, String key) {
        String encodedKey = insertNewlines(key, 64);
        String keyType;
        if (!isPublic) {
            keyType = "PRIVATE KEY";
        } else {
            keyType = "PUBLIC KEY";
        }
        System.out.println("Encoded");
        System.out.println(encodedKey);
        return "-----BEGIN " + keyType + "-----\n" + encodedKey + "\n" + "-----END " + keyType + "-----\n";
    }

    public static String insertNewlines(String input, int interval) {
        StringBuilder sb = new StringBuilder(input);

        for (int i = interval; i < sb.length(); i += interval + 1) {
            sb.insert(i, '\n');
        }

        return sb.toString();
    }
}
