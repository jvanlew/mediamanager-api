package net.vanlew.mediamanager.api.domain.security;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RefreshToken {
    private String userName;

    private String hashId;

    private String tokenString;

    private Instant expirationDateUtc;
}
