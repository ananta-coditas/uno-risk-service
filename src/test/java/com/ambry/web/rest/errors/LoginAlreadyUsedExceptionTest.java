package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LoginAlreadyUsedException Unit Tests")
class LoginAlreadyUsedExceptionTest {

    @Test
    @DisplayName("Should create LoginAlreadyUsedException with correct properties")
    void testConstructor() {
        LoginAlreadyUsedException exception = new LoginAlreadyUsedException();

        assertThat(exception).isNotNull();
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(exception.getEntityName()).isEqualTo("userManagement");
        assertThat(exception.getErrorKey()).isEqualTo("userexists");
    }

    @Test
    @DisplayName("Should have correct problem detail")
    void testProblemDetail() {
        LoginAlreadyUsedException exception = new LoginAlreadyUsedException();
        UnoProblemDetailEx problemDetail = exception.getProblemDetailWithCause();

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getTitle()).isEqualTo("Login name already used!");
        assertThat(problemDetail.getType()).isEqualTo(ErrorConstants.LOGIN_ALREADY_USED_TYPE);
    }

    @Test
    @DisplayName("Should extend BadRequestAlertException")
    void testInheritance() {
        LoginAlreadyUsedException exception = new LoginAlreadyUsedException();

        assertThat(exception).isInstanceOf(BadRequestAlertException.class);
    }
}

