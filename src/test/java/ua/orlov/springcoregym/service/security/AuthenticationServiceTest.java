package ua.orlov.springcoregym.service.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import ua.orlov.springcoregym.exception.TooManyAttemptsException;
import ua.orlov.springcoregym.service.token.InvalidTokenService;
import ua.orlov.springcoregym.service.user.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private InvalidTokenService invalidTokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void loginThenTooManyAttemptsException() {
        when(loginAttemptService.isBlocked()).thenReturn(true);

        var e = assertThrows(TooManyAttemptsException.class, () -> authenticationService.login("", ""));

        assertEquals("Too many attempts", e.getMessage());
        verify(loginAttemptService, times(1)).isBlocked();
    }

    @Test
    void loginThenSuccess() {
        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken("", ""));
        when(jwtService.generateToken(any())).thenReturn("");

        assertNotNull(authenticationService.login("", ""));

        verify(loginAttemptService, times(1)).isBlocked();
        verify(authenticationManager, times(1)).authenticate(any());
        verify(loginAttemptService, times(1)).loginSucceeded(any());
        verify(jwtService, times(1)).generateToken(any());
    }

    @Test
    void loginThenException() {
        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException(""){});

        assertThrows(Exception.class, () -> authenticationService.login("", ""));

        verify(loginAttemptService, times(1)).isBlocked();
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void logoutThenSuccessWithBearer() {
        String token = "Bearer asdasdasd";

        assertDoesNotThrow(() -> authenticationService.logout(token));

        verify(invalidTokenService, times(1)).invalidateToken(any());
    }

    @Test
    void logoutThenSuccessWithoutBearer() {
        String token = "asdasdasd";

        assertDoesNotThrow(() -> authenticationService.logout(token));

        verify(invalidTokenService, times(1)).invalidateToken(any());
    }
}
