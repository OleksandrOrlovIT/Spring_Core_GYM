package orlov.programming.springcoregym.service.authentication.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import orlov.programming.springcoregym.TestConfig;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class AuthenticationServiceImplTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void givenNull_whenAuthenticateUser_thenNothing(){
        assertDoesNotThrow(() -> authenticationService.authenticateUser(null, null, true));
        assertDoesNotThrow(() -> authenticationService.authenticateUser(null, null, false));
    }
}