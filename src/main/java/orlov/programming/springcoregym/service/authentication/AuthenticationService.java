package orlov.programming.springcoregym.service.authentication;

import orlov.programming.springcoregym.model.user.User;

public interface AuthenticationService {

    void authenticateUser(String userName, String password, boolean isTrainee);

    void logOut();

    void isUserLogged();

    User getLoggedUser();
}
