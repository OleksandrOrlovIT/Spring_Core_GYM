package ua.orlov.springcoregym.service.user;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.orlov.springcoregym.dao.impl.user.UserDao;
import ua.orlov.springcoregym.service.password.PasswordService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter matchPasswordCounter;

    @Mock
    private Counter changePasswordCounter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter("userService.isUserNameMatchPassword.count"))
                .thenReturn(matchPasswordCounter);
        when(meterRegistry.counter("userService.changeUserPassword.count"))
                .thenReturn(changePasswordCounter);
        userService = new UserServiceImpl(userDao, meterRegistry, passwordEncoder, passwordService);
    }

    @Test
    void isUserNameMatchPasswordThenSuccess() {
        when(userDao.isUserNameMatchPassword(any(), any())).thenReturn(true);

        boolean result = userService.isUserNameMatchPassword("username", "password");

        verify(matchPasswordCounter).increment();
        assertTrue(result);
    }

    @Test
    void changeUserPasswordThenSuccess() {
        when(userDao.changeUserPassword(any(), any())).thenReturn(true);

        boolean result = userService.changeUserPassword("username", "oldPassword", "newPassword");

        verify(changePasswordCounter).increment();
        assertTrue(result);
    }
}
