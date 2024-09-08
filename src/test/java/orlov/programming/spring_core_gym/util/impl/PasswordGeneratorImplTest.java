package orlov.programming.spring_core_gym.util.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorImplTest {
    @Test
    void whenGeneratePassword_thenPassword() {
        PasswordGeneratorImpl passwordGenerator = new PasswordGeneratorImpl();
        String password = passwordGenerator.generatePassword();
        assertEquals(10, password.length());
    }
}