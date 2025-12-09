package net.vanlew.mediamanager.api.domain.security.cryptography;

import lombok.SneakyThrows;
import net.vanlew.mediamanager.api.domain.common.Ensure;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SymmetricCrypto {

    private final String _transformation = "AES/CBC/PKCS5PADDING";
    private final String _algorithm = "AES";

    private final String _encryptionKey;

    private final String _initializationVector;

    public SymmetricCrypto() {
        _encryptionKey = "qPKJZtjSJkaRry7R5BZIVw==";
        _initializationVector =  "57aC74Qu9KHjpQiYrXGuKQ==";
    }

    @SneakyThrows
    public String encrypt(String plainText) {
        Ensure.notNull(plainText, "plainText");

        // key
        SecretKey key = new SecretKeySpec(_encryptionKey.getBytes(), _algorithm);

        // iv
        var ivSpec = new IvParameterSpec(Base64.getDecoder().decode(_initializationVector.getBytes()));

        // cipher
        Cipher cipher = Cipher.getInstance(_transformation);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(encrypted);
    }

    @SneakyThrows
    public String decrypt(String encryptedText) {
        // key
        SecretKey key = new SecretKeySpec(_encryptionKey.getBytes(), _algorithm);

        // iv
        var ivSpec = new IvParameterSpec(Base64.getDecoder().decode(_initializationVector.getBytes()));

        // cipher
        Cipher cipher = Cipher.getInstance(_transformation);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decrypted);

    }

}
