package ua.orlov.springcoregym.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.dao.impl.user.UserDao;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public boolean isUserNameMatchPassword(String username, String password) {
        return userDao.isUserNameMatchPassword(username, password);
    }

    @Override
    public boolean changeUserPassword(String username, String oldPassword, String newPassword) {
        return userDao.changeUserPassword(username, oldPassword, newPassword);
    }
}
