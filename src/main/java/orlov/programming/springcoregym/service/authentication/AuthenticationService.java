package orlov.programming.springcoregym.service.authentication;

import orlov.programming.springcoregym.model.user.User;

/**
 * Service interface for handling user authentication and session management.
 */
public interface AuthenticationService {

    /**
     * Authenticates a user based on their username, password.
     *
     * @param userName  the username of the user
     * @param password  the user's password
     */
    boolean authenticateUser(String userName, String password);

    /**
     * Authenticates a user based on their username, password, and role (trainee or trainer).
     *
     * @param userName  the username of the user
     * @param password  the user's password
     * @param isTrainee whether the user is a trainee (if false, the user is considered a trainer)
     */
    boolean authenticateUser(String userName, String password, boolean isTrainee);

    /**
     * Logs out the currently authenticated user.
     */
    void logOut();

    /**
     * Checks if a user is logged in. Throws an exception if no user is logged in.
     *
     * @throws IllegalArgumentException if no user is logged in
     */
    void isUserLogged();

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the logged-in {@link User}
     * @throws IllegalArgumentException if no user is logged in
     */
    User getLoggedUser();

    boolean changeLogin(String userName, String oldPassword, String newPassword);
}
