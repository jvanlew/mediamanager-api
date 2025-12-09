// java
package net.vanlew.mediamanager.api.domain.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RefreshTokenTests {

    @Test
    void builder_setsAllFields_and_gettersReturnValues() {
        Instant expires = Instant.now().plusSeconds(3600);

        RefreshToken token = RefreshToken.builder()
                .userName("alice")
                .hashId("hash-1")
                .tokenString("token-abc")
                .expirationDateUtc(expires)
                .build();

        assertThat(token.getUserName()).isEqualTo("alice");
        assertThat(token.getHashId()).isEqualTo("hash-1");
        assertThat(token.getTokenString()).isEqualTo("token-abc");
        assertThat(token.getExpirationDateUtc()).isEqualTo(expires);
    }

    @Test
    void equals_and_hashCode_and_toString() {
        Instant exp = Instant.parse("2025-01-01T00:00:00Z");

        RefreshToken a = RefreshToken.builder()
                .userName("u")
                .hashId("h")
                .tokenString("t")
                .expirationDateUtc(exp)
                .build();

        RefreshToken b = RefreshToken.builder()
                .userName("u")
                .hashId("h")
                .tokenString("t")
                .expirationDateUtc(exp)
                .build();

        RefreshToken c = RefreshToken.builder()
                .userName("other")
                .hashId("h")
                .tokenString("t")
                .expirationDateUtc(exp)
                .build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isNotEqualTo(c);
        assertThat(a.toString()).contains("userName=u", "hashId=h", "tokenString=t");
    }
}
