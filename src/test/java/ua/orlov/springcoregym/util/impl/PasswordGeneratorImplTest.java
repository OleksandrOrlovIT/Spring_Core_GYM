package ua.orlov.springcoregym.util.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorImplTest {
    @Test
    void generatePasswordThenSuccess() {
        PasswordGeneratorImpl passwordGenerator = new PasswordGeneratorImpl();
        String password = passwordGenerator.generatePassword();
        assertEquals(10, password.length());
    }
}
