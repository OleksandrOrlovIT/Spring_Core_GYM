package ua.orlov.springcoregym.service.security.impl;

import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.service.security.LoginAttemptService;

import java.util.concurrent.ExecutionException;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final int MAX_LOGIN_ATTEMPT;

    private final LoadingCache<String, Integer> attemptsCache;
    private final ObjectFactory<HttpServletRequest> requestFactory;

    public LoginAttemptServiceImpl(LoadingCache<String, Integer> attemptsCache,
                                   ObjectFactory<HttpServletRequest> requestFactory,
                                   @Value("${max.login.attempt}") int maxLoginAttempt) {
        this.attemptsCache = attemptsCache;
        this.requestFactory = requestFactory;
        MAX_LOGIN_ATTEMPT = maxLoginAttempt;
    }

    public void loginFailed(final String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked() {
        try {
            return attemptsCache.get(getClientIP()) >= MAX_LOGIN_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    public void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    public String getClientIP() {
        HttpServletRequest request = requestFactory.getObject();
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
