package net.vanlew.mediamanager.api.domain.common.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt-settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtSettings {
    private String secret;

    private String issuer;

    private String audience;

    private Integer accessTokenExpirationMinutes;

    private Integer refreshTokenExpirationMinutes;
}
