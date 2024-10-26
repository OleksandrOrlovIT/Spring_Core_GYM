package ua.orlov.springcoregym.service.token;

import ua.orlov.springcoregym.model.token.InvalidToken;

public interface InvalidTokenService {

    InvalidToken getByToken(String token);

    boolean isTokenBlacklisted(String token);

    void invalidateToken(String token);
}
