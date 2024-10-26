package ua.orlov.springcoregym.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ua.orlov.springcoregym.model.user.User;
import ua.orlov.springcoregym.service.token.InvalidTokenService;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private InvalidTokenService invalidTokenService;

    private final String testSigningKey = "63B74F4E1D5F0B2C4A3F2E685F7A1C534D7E357E1D7F5C3B495C734B64327844";

    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        Field signingKeyField = JwtService.class.getDeclaredField("jwtSigningKey");
        signingKeyField.setAccessible(true);
        signingKeyField.set(jwtService, testSigningKey);

        userDetails = mock(UserDetails.class);
    }

    @Test
    void extractUserName() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("testUser", extractedUsername, "The extracted username should match the userDetails username.");
    }

    @Test
    void generateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token, "Token should not be null.");
        assertTrue(token.startsWith("ey"), "Token should be a valid JWT format.");

        Claims claims = Jwts.parser().setSigningKey(jwtService.getSigningKey()).build().parseClaimsJws(token).getBody();
        assertEquals("testUser", claims.getSubject(), "The subject in the token should match the username.");
    }

    @Test
    void isTokenValid() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        when(invalidTokenService.isTokenBlacklisted(token)).thenReturn(false);

        assertTrue(jwtService.isTokenValid(token, userDetails), "Token should be valid for matching username and not expired.");

        when(invalidTokenService.isTokenBlacklisted(token)).thenReturn(true);
        assertFalse(jwtService.isTokenValid(token, userDetails), "Token should be invalid if blacklisted.");
    }

    @Test
    void isTokenExpired() {
        when(userDetails.getUsername()).thenReturn("testUser");
        Map<String, Object> claims = new HashMap<>();
        String validToken = jwtService.generateToken(claims, userDetails);

        assertFalse(jwtService.isTokenExpired(validToken), "Token should not be expired immediately after generation.");

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(jwtService.getSigningKey())
                .compact();

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.isTokenExpired(expiredToken), "Token with past expiration date should be expired.");
    }

    @Test
    void extractExpiration() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration, "The expiration date should not be null.");
        assertTrue(expiration.after(new Date()), "The expiration date should be in the future.");
    }

    @Test
    void extractAllClaims() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        Claims claims = jwtService.extractAllClaims(token);

        assertNotNull(claims, "Claims should not be null.");
        assertEquals("testUser", claims.getSubject(), "Claims should contain the username as subject.");
    }

    @Test
    void getSigningKey() {
        Key signingKey = jwtService.getSigningKey();

        assertNotNull(signingKey, "Signing key should not be null.");
    }

    @Test
    void generateTokenThenSuccess() {
        User user = User.builder()
                .id(1L)
                .username("us")
                .build();

        assertNotNull(jwtService.generateToken(user));
    }

    @Test
    void isTokenValidThenUsernameDoesntEquals() {
        User user = User.builder()
                .id(1L)
                .username("us")
                .build();

        String token = jwtService.generateToken(user);

        User user1 = User.builder()
                .id(1L)
                .username("usasd")
                .build();

        assertFalse(jwtService.isTokenValid(token, user1));
    }

    @Test
    void isTokenValidThenExpiredToken() throws InterruptedException {
        User user = User.builder()
                .id(1L)
                .username("usasd")
                .build();

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100))
                .signWith(jwtService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        Thread.sleep(150);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, user));
    }
}
