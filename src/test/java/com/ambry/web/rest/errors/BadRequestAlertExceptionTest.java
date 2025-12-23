package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BadRequestAlertException Unit Tests")
class BadRequestAlertExceptionTest {

    @Test
    @DisplayName("Should create BadRequestAlertException with default type")
    void testConstructorWithDefaultType() {
        String defaultMessage = "Test error message";
        String entityName = "testEntity";
        String errorKey = "testError";

        BadRequestAlertException exception = new BadRequestAlertException(defaultMessage, entityName, errorKey);

        assertThat(exception).isNotNull();
        assertThat(exception.getEntityName()).isEqualTo(entityName);
        assertThat(exception.getErrorKey()).isEqualTo(errorKey);
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(exception.getProblemDetailWithCause()).isNotNull();
        assertThat(exception.getProblemDetailWithCause().getTitle()).isEqualTo(defaultMessage);
    }

    @Test
    @DisplayName("Should create BadRequestAlertException with custom URI type")
    void testConstructorWithCustomType() {
        URI customType = URI.create("https://www.ambrygen.com/problem/custom-error");
        String defaultMessage = "Custom error message";
        String entityName = "customEntity";
        String errorKey = "customError";

        BadRequestAlertException exception = new BadRequestAlertException(customType, defaultMessage, entityName, errorKey);

        assertThat(exception).isNotNull();
        assertThat(exception.getEntityName()).isEqualTo(entityName);
        assertThat(exception.getErrorKey()).isEqualTo(errorKey);
        assertThat(exception.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(exception.getProblemDetailWithCause()).isNotNull();
        assertThat(exception.getProblemDetailWithCause().getTitle()).isEqualTo(defaultMessage);
        assertThat(exception.getProblemDetailWithCause().getType()).isEqualTo(customType);
    }

    @Test
    @DisplayName("Should have correct problem detail properties")
    void testProblemDetailProperties() {
        String defaultMessage = "Test error";
        String entityName = "entity";
        String errorKey = "error";

        BadRequestAlertException exception = new BadRequestAlertException(defaultMessage, entityName, errorKey);
        UnoProblemDetailEx problemDetail = exception.getProblemDetailWithCause();

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getStatus()).isEqualTo(400);
        assertThat(problemDetail.getProperties()).isNotNull();
        assertThat(problemDetail.getProperties().get("message")).isEqualTo("error." + errorKey);
        assertThat(problemDetail.getProperties().get("params")).isEqualTo(entityName);
    }

    @Test
    @DisplayName("Should get entity name correctly")
    void testGetEntityName() {
        String entityName = "testEntity";
        BadRequestAlertException exception = new BadRequestAlertException("Message", entityName, "error");

        assertThat(exception.getEntityName()).isEqualTo(entityName);
    }

    @Test
    @DisplayName("Should get error key correctly")
    void testGetErrorKey() {
        String errorKey = "testError";
        BadRequestAlertException exception = new BadRequestAlertException("Message", "entity", errorKey);

        assertThat(exception.getErrorKey()).isEqualTo(errorKey);
    }

    @Test
    @DisplayName("Should get problem detail with cause correctly")
    void testGetProblemDetailWithCause() {
        BadRequestAlertException exception = new BadRequestAlertException("Message", "entity", "error");

        UnoProblemDetailEx problemDetail = exception.getProblemDetailWithCause();

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail).isInstanceOf(UnoProblemDetailEx.class);
    }
}

