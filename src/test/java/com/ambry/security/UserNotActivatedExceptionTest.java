package com.ambry.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

@DisplayName("UserNotActivatedException Unit Tests")
class UserNotActivatedExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void testExceptionWithMessage() {
        String message = "User is not activated";
        UserNotActivatedException exception = new UserNotActivatedException(message);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void testExceptionWithMessageAndCause() {
        String message = "User is not activated";
        Throwable cause = new RuntimeException("Underlying error");
        UserNotActivatedException exception = new UserNotActivatedException(message, cause);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should extend AuthenticationException")
    void testExtendsAuthenticationException() {
        UserNotActivatedException exception = new UserNotActivatedException("test");
        assertThat(exception).isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("Should handle null message")
    void testExceptionWithNullMessage() {
        UserNotActivatedException exception = new UserNotActivatedException(null);
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle null cause")
    void testExceptionWithNullCause() {
        String message = "User is not activated";
        UserNotActivatedException exception = new UserNotActivatedException(message, null);
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should handle empty message")
    void testExceptionWithEmptyMessage() {
        UserNotActivatedException exception = new UserNotActivatedException("");
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Should have serialVersionUID")
    void testSerialVersionUID() throws NoSuchFieldException {
        java.lang.reflect.Field field = UserNotActivatedException.class.getDeclaredField("serialVersionUID");
        field.setAccessible(true);
        assertThat(field.getType()).isEqualTo(long.class);
    }
}

