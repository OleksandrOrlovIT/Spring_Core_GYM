package ua.orlov.springcoregym.service.user;

public interface UserService {
    boolean isUserNameMatchPassword(String username, String password);

    boolean changeUserPassword(String username, String oldPassword, String newPassword);
}
