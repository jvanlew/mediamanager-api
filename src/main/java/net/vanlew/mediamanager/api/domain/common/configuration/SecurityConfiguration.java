package net.vanlew.mediamanager.api.domain.common.configuration;

import net.vanlew.mediamanager.api.domain.security.CustomAuthenticationEntryPoint;
//import net.vanlew.api.domain.security.JwtAuthenticationFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    //private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Logger LOG = LogManager.getLogger(SecurityConfiguration.class);


    //private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // other public endpoints of your API may be appended to this array
            "/api-docs/**"
    };

    public SecurityConfiguration(/*JwtAuthenticationFilter jwtAuthenticationFilter,*/ CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOG.debug("Configuring security filter chain");
        http
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(AUTH_WHITELIST).permitAll();
                    authorize.requestMatchers("/api/auth", "/actuator/**").permitAll();
                    authorize.anyRequest().authenticated(); // Require authentication for other endpoints
                })
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(customAuthenticationEntryPoint);
                });
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
