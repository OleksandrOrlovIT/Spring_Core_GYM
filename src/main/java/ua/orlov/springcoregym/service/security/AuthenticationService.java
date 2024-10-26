package ua.orlov.springcoregym.service.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.exception.TooManyAttemptsException;
import ua.orlov.springcoregym.service.token.InvalidTokenService;
import ua.orlov.springcoregym.service.user.UserService;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    private final InvalidTokenService invalidTokenService;

    public String login(String username, String password) {
        try {
            if (loginAttemptService.isBlocked()) {
                throw new TooManyAttemptsException("Too many attempts");
            }

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(authenticationToken);

            loginAttemptService.loginSucceeded(loginAttemptService.getClientIP());

            return jwtService.generateToken(userService.getByUsername(username));

        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            throw ex;
        }
    }

    public void logout(String token) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        invalidTokenService.invalidateToken(jwt);
    }

}
