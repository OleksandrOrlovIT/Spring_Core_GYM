package ua.orlov.springcoregym.service.security;

public interface AuthenticationService {

    String login(String username, String password);

    void logout(String token);
}
