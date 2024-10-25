package ua.orlov.springcoregym.service.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.service.user.UserService;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String login(String username, String password) {
        try {
            if(!userService.isUserNameMatchPassword(username, password)){
                throw new IllegalArgumentException("Invalid username or password");
            }

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(username, password);

            authenticationManager.authenticate(authenticationToken);

            return jwtService.generateToken(userService.getByUsername(username));

        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            throw ex;
        }
    }
}
