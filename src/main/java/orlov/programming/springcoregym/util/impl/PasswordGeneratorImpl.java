package orlov.programming.springcoregym.util.impl;

import org.springframework.stereotype.Component;
import orlov.programming.springcoregym.util.PasswordGenerator;

import java.security.SecureRandom;

@Component
public class PasswordGeneratorImpl implements PasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int PASSWORD_LENGTH = 10;

    @Override
    public String generatePassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        while (sb.length() < PASSWORD_LENGTH) {
            char nextChar = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
            if (sb.indexOf(String.valueOf(nextChar)) == -1) {
                sb.append(nextChar);
            }
        }
        return sb.toString();
    }
}
