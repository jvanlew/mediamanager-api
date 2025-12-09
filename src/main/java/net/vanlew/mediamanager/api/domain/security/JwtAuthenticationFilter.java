package net.vanlew.mediamanager.api.domain.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.vanlew.mediamanager.api.domain.models.enumerations.UserRoles;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger LOG = LogManager.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOG.debug("checking authorization header");
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            LOG.debug("validating auth token");
            if (jwtUtil.validateToken(token)) {

                LOG.debug("token is valid, securing context");
                String username = jwtUtil.extractSubject(token);
                var roles = jwtUtil.extractRoles(token).stream().map(UserRoles::toString).toList();
                var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

                // Create an authentication token with the username and roles and set it in the security context
                // Note: The password is null because we are not using it for authentication here
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
