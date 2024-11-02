package ua.orlov.springcoregym.service.security;

public interface LoginAttemptService {

    void loginFailed(final String key);

    boolean isBlocked();

    void loginSucceeded(final String key);

    String getClientIP();
}
