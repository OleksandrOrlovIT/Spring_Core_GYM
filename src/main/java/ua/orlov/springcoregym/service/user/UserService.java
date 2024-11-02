package ua.orlov.springcoregym.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.orlov.springcoregym.model.user.User;

public interface UserService extends UserDetailsService {
    boolean isUserNameMatchPassword(String username, String password);

    boolean changeUserPassword(String username, String oldPassword, String newPassword);

    User getByUsername(String username);

    User getCurrentUser();
}
