package com.ambry.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ambry.transformer.core.validator.ValidationErrors;

@DisplayName("ValidationException Unit Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Should create ValidationException with message only")
    void testConstructorWithMessage() {
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getValidationErrors()).isNotNull();
        assertThat(exception.getValidationErrors().hasErros()).isTrue();
    }

    @Test
    @DisplayName("Should create ValidationException with message and ValidationErrors")
    void testConstructorWithMessageAndValidationErrors() {
        String message = "Validation failed";
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.rejectValue("field1", "Error message 1");
        validationErrors.rejectValue("field2", "Error message 2");

        ValidationException exception = new ValidationException(message, validationErrors);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getValidationErrors()).isEqualTo(validationErrors);
        assertThat(exception.getValidationErrors().getErrors()).hasSize(2);
    }

    @Test
    @DisplayName("Should create ValidationException with ValidationErrors only")
    void testConstructorWithValidationErrorsOnly() {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.rejectValue("field1", "Error message");

        ValidationException exception = new ValidationException(validationErrors);

        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getValidationErrors()).isEqualTo(validationErrors);
        assertThat(exception.getValidationErrors().hasErros()).isTrue();
    }

    @Test
    @DisplayName("Should set and get ValidationErrors")
    void testSetAndGetValidationErrors() {
        ValidationException exception = new ValidationException("Test");
        ValidationErrors newValidationErrors = new ValidationErrors();
        newValidationErrors.rejectValue("newField", "New error");

        exception.setValidationErrors(newValidationErrors);

        assertThat(exception.getValidationErrors()).isEqualTo(newValidationErrors);
        assertThat(exception.getValidationErrors().getErrors()).hasSize(1);
    }

    @Test
    @DisplayName("Should initialize ValidationErrors when created with message only")
    void testValidationErrorsInitialization() {
        String message = "Test validation error";
        ValidationException exception = new ValidationException(message);

        ValidationErrors errors = exception.getValidationErrors();
        assertThat(errors).isNotNull();
        assertThat(errors.hasErros()).isTrue();
        assertThat(errors.getErrors()).hasSize(1);
        assertThat(errors.getErrors().get(0)).isNotNull();
    }
}

