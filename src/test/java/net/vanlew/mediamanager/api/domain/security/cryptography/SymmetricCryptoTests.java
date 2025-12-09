package net.vanlew.mediamanager.api.domain.security.cryptography;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SymmetricCryptoTests {

    @Test
    void symmetricCrypto_CanInitialize() {
        SymmetricCrypto crypto = new SymmetricCrypto();
        assertThat(crypto).isNotNull();
    }

    @Test
    void symmetricCrypto_CanEncryptAndDecrypt() {
        SymmetricCrypto crypto = new SymmetricCrypto();
        String expectedPlainText = "This is a test string.";
        String expectedEncryptedText = "vYk1KXQ2c1JtU1JrWUNkVQ==";

        String encryptedText = crypto.encrypt(expectedPlainText);
        assertThat(encryptedText).isNotNull();
        assertThat(encryptedText).isNotEqualTo(expectedEncryptedText);

        String decryptedText = crypto.decrypt(encryptedText);
        assertThat(decryptedText).isNotNull();
        assertThat(decryptedText).isEqualTo(expectedPlainText);
    }
}
