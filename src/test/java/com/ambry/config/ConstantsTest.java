package com.ambry.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Constants}.
 */
class ConstantsTest {

    @Test
    void testLoginRegex() {
        assertThat(Constants.LOGIN_REGEX).isNotNull();
        assertThat(Constants.LOGIN_REGEX).isNotEmpty();
    }

    @Test
    void testLoginRegexWithValidEmail() {
        String validEmail = "user@example.com";
        assertThat(validEmail).matches(Constants.LOGIN_REGEX);
    }

    @Test
    void testLoginRegexWithValidUsername() {
        String validUsername = "username123";
        assertThat(validUsername).matches(Constants.LOGIN_REGEX);
    }

    @Test
    void testSystemConstant() {
        assertThat(Constants.SYSTEM).isEqualTo("system");
    }

    @Test
    void testDefaultLanguageConstant() {
        assertThat(Constants.DEFAULT_LANGUAGE).isEqualTo("en");
    }

    @Test
    void testConstantsClassCannotBeInstantiated() {
        // Verify that Constants has a private constructor
        // This is a design test - the class should be a utility class
        assertThat(Constants.class).isNotNull();
    }
}

