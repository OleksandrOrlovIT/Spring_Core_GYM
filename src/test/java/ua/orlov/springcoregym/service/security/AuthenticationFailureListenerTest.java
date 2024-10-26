package ua.orlov.springcoregym.service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFailureListenerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private AuthenticationFailureListener listener;

    private AuthenticationFailureBadCredentialsEvent event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = mock(AuthenticationFailureBadCredentialsEvent.class);
    }

    @Test
    void onApplicationEventWhenXForwardedForHeaderIsAbsentShouldCallLoginFailedWithRemoteAddr() {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("127.0.0.1");
    }

    @Test
    void onApplicationEventWhenXForwardedForHeaderDoesNotContainRemoteAddrShouldCallLoginFailedWithRemoteAddr() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.4");
        when(request.getRemoteAddr()).thenReturn("192.168.1.5");

        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.1.5");
    }

    @Test
    void onApplicationEventXfHeaderEmpty() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("");
        when(request.getRemoteAddr()).thenReturn("192.168.1.5");

        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.1.5");
    }

    @Test
    void onApplicationEventXfHeaderContainsGetRemoteAddr() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.5");
        when(request.getRemoteAddr()).thenReturn("192.168.1.5");

        listener.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("192.168.1.5");
    }
}