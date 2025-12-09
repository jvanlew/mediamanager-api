package net.vanlew.mediamanager.api.domain.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.vanlew.mediamanager.api.domain.common.configuration.JwtSettings;
import net.vanlew.mediamanager.api.domain.models.entities.User;
import net.vanlew.mediamanager.api.domain.models.enumerations.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


@Component
public class JwtUtil {

    private final JwtSettings jwtSettings;

    @Autowired
    public JwtUtil(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    public String generateToken(User user) {
        var now = new Date();
        var expirationMs = Duration.ofMinutes(jwtSettings.getAccessTokenExpirationMinutes()).toMillis();
        Date expires = new Date(now.getTime() + expirationMs);

        Key key = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes());

        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", jwtSettings.getIssuer());
        claims.put("sub", user.getId());
        claims.put("name", user.getUserName());
        claims.put("aud", jwtSettings.getAudience());
        claims.put("exp", expires);
        claims.put("iat", now);
        claims.put("roles", user.getUserRoles().toArray());

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes(StandardCharsets.UTF_8));

            var expiration = Jwts.parser()
                    .verifyWith(key)
                    .clockSkewSeconds(10)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            // Check if the token is expired
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Instant extractExpirationDate(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


        // Get the subject from the claims
        Date expiration = claims.get("exp", Date.class);
        return expiration.toInstant();
    }

    public String extractSubject(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Get the subject from the claims
        return claims.get("name", String.class);
    }

    public List<UserRoles> extractRoles(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes());

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Get the subject from the claims
        var claimRoles = claims.get("roles", ArrayList.class);

        List<UserRoles> roles = new ArrayList<>();
        for (var role : claimRoles) {
            roles.add(UserRoles.fromValue(role.toString()));
        }

        return roles;

    }
}
