package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ErrorConstants Unit Tests")
class ErrorConstantsTest {

    @Test
    @DisplayName("Should have correct error concurrency failure constant")
    void testErrConcurrencyFailure() {
        assertThat(ErrorConstants.ERR_CONCURRENCY_FAILURE).isEqualTo("error.concurrencyFailure");
    }

    @Test
    @DisplayName("Should have correct error validation constant")
    void testErrValidation() {
        assertThat(ErrorConstants.ERR_VALIDATION).isEqualTo("error.validation");
    }

    @Test
    @DisplayName("Should have correct problem base URL")
    void testProblemBaseUrl() {
        assertThat(ErrorConstants.PROBLEM_BASE_URL).isEqualTo("https://www.ambrygen.com/problem");
    }

    @Test
    @DisplayName("Should have correct default type URI")
    void testDefaultType() {
        URI expected = URI.create("https://www.ambrygen.com/problem/problem-with-message");
        assertThat(ErrorConstants.DEFAULT_TYPE).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should have correct constraint violation type URI")
    void testConstraintViolationType() {
        URI expected = URI.create("https://www.ambrygen.com/problem/constraint-violation");
        assertThat(ErrorConstants.CONSTRAINT_VIOLATION_TYPE).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should have correct invalid password type URI")
    void testInvalidPasswordType() {
        URI expected = URI.create("https://www.ambrygen.com/problem/invalid-password");
        assertThat(ErrorConstants.INVALID_PASSWORD_TYPE).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should have correct email already used type URI")
    void testEmailAlreadyUsedType() {
        URI expected = URI.create("https://www.ambrygen.com/problem/email-already-used");
        assertThat(ErrorConstants.EMAIL_ALREADY_USED_TYPE).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should have correct login already used type URI")
    void testLoginAlreadyUsedType() {
        URI expected = URI.create("https://www.ambrygen.com/problem/login-already-used");
        assertThat(ErrorConstants.LOGIN_ALREADY_USED_TYPE).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should not be instantiable")
    void testPrivateConstructor() throws Exception {
        // ErrorConstants has a private constructor, so it cannot be instantiated
        // This test verifies the class design
        assertThat(ErrorConstants.class.getDeclaredConstructors()).hasSize(1);
        assertThat(ErrorConstants.class.getDeclaredConstructors()[0].getModifiers())
            .isEqualTo(java.lang.reflect.Modifier.PRIVATE);
    }
}

