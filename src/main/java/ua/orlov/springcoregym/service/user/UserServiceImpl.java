package ua.orlov.springcoregym.service.user;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dao.impl.user.UserDao;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final Counter matchPasswordCounter;
    private final Counter changePasswordCounter;

    public UserServiceImpl(UserDao userDao, MeterRegistry meterRegistry) {
        this.userDao = userDao;
        this.matchPasswordCounter = meterRegistry.counter("userService.isUserNameMatchPassword.count");
        this.changePasswordCounter = meterRegistry.counter("userService.changeUserPassword.count");
    }

    @Override
    @Timed(value = "userService.isUserNameMatchPassword", description = "Time taken to match username and password")
    public boolean isUserNameMatchPassword(String username, String password) {
        matchPasswordCounter.increment();
        return userDao.isUserNameMatchPassword(username, password);
    }

    @Override
    @Timed(value = "userService.changeUserPassword", description = "Time taken to change the user password")
    public boolean changeUserPassword(String username, String oldPassword, String newPassword) {
        changePasswordCounter.increment();
        return userDao.changeUserPassword(username, oldPassword, newPassword);
    }
}
