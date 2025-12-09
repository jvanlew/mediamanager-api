// java
package net.vanlew.mediamanager.api.domain.security;

import net.vanlew.mediamanager.api.domain.common.configuration.JwtSettings;
import net.vanlew.mediamanager.api.domain.models.entities.User;
import net.vanlew.mediamanager.api.domain.models.enumerations.UserRoles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private static final String SECRET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXabcdefghijklmnopqrstuVWXYZ012345"; // long enough

    @Mock
    JwtSettings jwtSettings;

    @Mock
    User user;

    @InjectMocks
    JwtUtil jwtUtil;

    public JwtUtilTest() {
        jwtSettings = new JwtSettings();
        jwtSettings.setSecret(SECRET);
        jwtSettings.setIssuer("test-issuer");
        jwtSettings.setAudience("test-audience");
        jwtSettings.setAccessTokenExpirationMinutes(60);
        jwtUtil = new JwtUtil(jwtSettings);
    }

    @Test
    void generateToken_shouldProduceValidToken_and_extractClaims() {
        when(user.getId()).thenReturn("user-123");
        when(user.getUserName()).thenReturn("alice");
        when(user.getUserRoles()).thenReturn(EnumSet.of(UserRoles.SUBSCRIBER, UserRoles.CONTRIBUTOR));

        String token = jwtUtil.generateToken(user);

        // token should validate
        boolean valid = jwtUtil.validateToken(token);
        assertThat(valid).isTrue();

        // subject (name) should be extractable
        String subject = jwtUtil.extractSubject(token);
        assertThat(subject).isEqualTo("alice");

        // roles should be extractable and match the user's roles
        List<UserRoles> roles = jwtUtil.extractRoles(token);
        assertThat(roles).containsExactlyInAnyOrder(UserRoles.SUBSCRIBER, UserRoles.CONTRIBUTOR);

        // expiration should be in the future (at least a few seconds from now)
        Instant expiration = jwtUtil.extractExpirationDate(token);
        assertThat(expiration).isAfter(Instant.now().minusSeconds(5));
    }

    @Test
    void validateToken_withInvalidToken_returnsFalse() {
        String invalidToken = "this.is.not.a.valid.token";
        boolean valid = jwtUtil.validateToken(invalidToken);
        assertThat(valid).isFalse();
    }

    @Test
    void generateToken_withNoRoles_extractRolesReturnsEmptyList() {
        when(user.getId()).thenReturn("user-456");
        when(user.getUserName()).thenReturn("bob");
        when(user.getUserRoles()).thenReturn(EnumSet.noneOf(UserRoles.class));

        String token = jwtUtil.generateToken(user);

        assertThat(jwtUtil.validateToken(token)).isTrue();
        String subject = jwtUtil.extractSubject(token);
        assertThat(subject).isEqualTo("bob");

        List<UserRoles> roles = jwtUtil.extractRoles(token);
        assertThat(roles).isEmpty();
    }
}