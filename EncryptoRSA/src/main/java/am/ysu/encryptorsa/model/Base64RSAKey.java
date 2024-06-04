package am.ysu.encryptorsa.model;

import lombok.Getter;

@Getter
public class Base64RSAKey {
    private final String privateKey;
    private final String publicKey;
    private final long generationTime;

    public Base64RSAKey(String privateKey, String publicString, long generationTime) {
        this.privateKey = privateKey;
        this.publicKey = publicString;
        this.generationTime = generationTime;
    }


    @Override
    public String toString() {
        return "Base64RSAKey{" +
                "privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", generationTime=" + generationTime +
                '}';
    }
}
