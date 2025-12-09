package net.vanlew.mediamanager.api.domain.models.operations.responses;

import lombok.Builder;
import lombok.Data;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;
import net.vanlew.mediamanager.api.domain.security.RefreshToken;

@Data
@Builder
public class AuthenticationResult {

    private OperationStatusTypes status;

    private String message;

    private String accessToken;

    private RefreshToken refreshToken;
}
