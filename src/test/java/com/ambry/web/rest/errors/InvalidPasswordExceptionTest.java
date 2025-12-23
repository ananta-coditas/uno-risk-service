package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidPasswordException Unit Tests")
class InvalidPasswordExceptionTest {

    @Test
    @DisplayName("Should create InvalidPasswordException with correct status")
    void testConstructor() {
        InvalidPasswordException exception = new InvalidPasswordException();

        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should have correct problem detail")
    void testProblemDetail() {
        InvalidPasswordException exception = new InvalidPasswordException();
        UnoProblemDetailEx problemDetail = (UnoProblemDetailEx) exception.getBody();

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getTitle()).isEqualTo("Incorrect password");
        assertThat(problemDetail.getType()).isEqualTo(ErrorConstants.INVALID_PASSWORD_TYPE);
        assertThat(problemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Should extend ErrorResponseException")
    void testInheritance() {
        InvalidPasswordException exception = new InvalidPasswordException();

        assertThat(exception).isInstanceOf(org.springframework.web.ErrorResponseException.class);
    }
}

