package ua.orlov.springcoregym.service.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.orlov.springcoregym.dao.impl.user.UserDao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void isUserNameMatchPasswordThenSuccess(){
        when(userDao.isUserNameMatchPassword(any(), any())).thenReturn(true);

        assertTrue(userService.isUserNameMatchPassword("username", "password"));
    }

    @Test
    void changeUserPasswordThenSuccess(){
        when(userDao.changeUserPassword(any(), any(), any())).thenReturn(true);

        assertTrue(userService.changeUserPassword("username", "oldPassword", "newPassword"));
    }
}
