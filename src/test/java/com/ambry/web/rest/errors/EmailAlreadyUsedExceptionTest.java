package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EmailAlreadyUsedException Unit Tests")
class EmailAlreadyUsedExceptionTest {

    @Test
    @DisplayName("Should create EmailAlreadyUsedException with correct properties")
    void testConstructor() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();

        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(exception.getEntityName()).isEqualTo("userManagement");
        assertThat(exception.getErrorKey()).isEqualTo("emailexists");
    }

    @Test
    @DisplayName("Should have correct problem detail")
    void testProblemDetail() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();
        UnoProblemDetailEx problemDetail = exception.getProblemDetailWithCause();

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getTitle()).isEqualTo("Email is already in use!");
        assertThat(problemDetail.getType()).isEqualTo(ErrorConstants.EMAIL_ALREADY_USED_TYPE);
    }

    @Test
    @DisplayName("Should extend BadRequestAlertException")
    void testInheritance() {
        EmailAlreadyUsedException exception = new EmailAlreadyUsedException();

        assertThat(exception).isInstanceOf(BadRequestAlertException.class);
    }
}

