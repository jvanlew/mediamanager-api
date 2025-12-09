package net.vanlew.mediamanager.api.domain.models.operations.responses;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TokenCheckResult {

    private boolean isValid;

    private Instant expirationDateUtc;
}