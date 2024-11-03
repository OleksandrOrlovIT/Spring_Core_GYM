package ua.orlov.springcoregym.service.security.impl;

import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.orlov.springcoregym.service.security.LoginAttemptService;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final int maxLoginAttempt;

    private final LoadingCache<String, Integer> attemptsCache;
    private final ObjectFactory<HttpServletRequest> requestFactory;

    public LoginAttemptServiceImpl(LoadingCache<String, Integer> attemptsCache,
                                   ObjectFactory<HttpServletRequest> requestFactory,
                                   @Value("${max.login.attempt}") int maxLoginAttempt) {
        this.attemptsCache = attemptsCache;
        this.requestFactory = requestFactory;
        this.maxLoginAttempt = maxLoginAttempt;
    }

    public void loginFailed(final String key) {
        int attempts = attemptsCache.getUnchecked(key) + 1;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked() {
        return attemptsCache.getUnchecked(getClientIP()) >= maxLoginAttempt;
    }

    public void loginSucceeded(final String key) {
        attemptsCache.invalidate(key);
    }

    public String getClientIP() {
        HttpServletRequest request = requestFactory.getObject();
        final String xfHeader = request.getHeader("X-Forwarded-For");
        return xfHeader != null ? xfHeader.split(",")[0] : request.getRemoteAddr();
    }
}
