package net.vanlew.mediamanager.api.domain.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class CustomAuthenticationEntryPointTests {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    CustomAuthenticationEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        entryPoint = new CustomAuthenticationEntryPoint();
        request = org.mockito.Mockito.mock(HttpServletRequest.class);
        response = org.mockito.Mockito.mock(HttpServletResponse.class);
    }

    @Test
    void commence_withAuthenticationException_sends401() throws Exception {
        BadCredentialsException authEx = new BadCredentialsException("invalid");

        entryPoint.commence(request, response, authEx);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());

        assertThat(statusCaptor.getValue()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(messageCaptor.getValue()).isEqualTo("Unauthorized: Authentication failed");
    }

    @Test
    void commence_withAccessDeniedException_sends403() throws Exception {
        AccessDeniedException accessEx = new AccessDeniedException("no permission");

        entryPoint.commence(request, response, accessEx);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());

        assertThat(statusCaptor.getValue()).isEqualTo(HttpServletResponse.SC_FORBIDDEN);
        assertThat(messageCaptor.getValue()).isEqualTo("Authorization Failed: {}" + accessEx.getMessage());
    }

    @Test
    void commence_withGenericException_sends500() throws Exception {
        Exception ex = new Exception("something went wrong");

        entryPoint.commence(request, response, ex);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(response).sendError(statusCaptor.capture(), messageCaptor.capture());

        assertThat(statusCaptor.getValue()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertThat(messageCaptor.getValue()).isEqualTo("Internal Server Error : " + ex.getMessage());
    }
}
