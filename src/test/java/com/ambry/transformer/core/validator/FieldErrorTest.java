package com.ambry.transformer.core.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FieldError Unit Tests")
class FieldErrorTest {

    @Test
    @DisplayName("Should create FieldError with errorCode and errorDescription")
    void testConstructorWithErrorCodeAndDescription() {
        FieldError error = new FieldError("ERROR001", "Error description");

        assertThat(error.getErrorCode()).isEqualTo("ERROR001");
        assertThat(error.getErrorDescription()).isEqualTo("Error description");
    }

    @Test
    @DisplayName("Should create FieldError with only errorDescription")
    void testConstructorWithOnlyDescription() {
        FieldError error = new FieldError("Error description only");

        assertThat(error.getErrorCode()).isNull();
        assertThat(error.getErrorDescription()).isEqualTo("Error description only");
    }

    @Test
    @DisplayName("Should set and get errorCode")
    void testSetAndGetErrorCode() {
        FieldError error = new FieldError("ERROR001", "Description");

        error.setErrorCode("ERROR002");

        assertThat(error.getErrorCode()).isEqualTo("ERROR002");
    }

    @Test
    @DisplayName("Should set and get errorDescription")
    void testSetAndGetErrorDescription() {
        FieldError error = new FieldError("ERROR001", "Description1");

        error.setErrorDescription("Description2");

        assertThat(error.getErrorDescription()).isEqualTo("Description2");
    }

    @Test
    @DisplayName("Should handle null errorCode")
    void testWithNullErrorCode() {
        FieldError error = new FieldError(null, "Error description");

        assertThat(error.getErrorCode()).isNull();
        assertThat(error.getErrorDescription()).isEqualTo("Error description");
    }

    @Test
    @DisplayName("Should handle null errorDescription")
    void testWithNullErrorDescription() {
        FieldError error = new FieldError("ERROR001", null);

        assertThat(error.getErrorCode()).isEqualTo("ERROR001");
        assertThat(error.getErrorDescription()).isNull();
    }

    @Test
    @DisplayName("Should handle empty errorCode")
    void testWithEmptyErrorCode() {
        FieldError error = new FieldError("", "Error description");

        assertThat(error.getErrorCode()).isEmpty();
        assertThat(error.getErrorDescription()).isEqualTo("Error description");
    }

    @Test
    @DisplayName("Should handle empty errorDescription")
    void testWithEmptyErrorDescription() {
        FieldError error = new FieldError("ERROR001", "");

        assertThat(error.getErrorCode()).isEqualTo("ERROR001");
        assertThat(error.getErrorDescription()).isEmpty();
    }

    @Test
    @DisplayName("Should update errorCode and errorDescription")
    void testUpdateErrorCodeAndDescription() {
        FieldError error = new FieldError("ERROR001", "Description1");

        error.setErrorCode("ERROR002");
        error.setErrorDescription("Description2");

        assertThat(error.getErrorCode()).isEqualTo("ERROR002");
        assertThat(error.getErrorDescription()).isEqualTo("Description2");
    }

    @Test
    @DisplayName("Should set errorCode to null")
    void testSetErrorCodeToNull() {
        FieldError error = new FieldError("ERROR001", "Description");

        error.setErrorCode(null);

        assertThat(error.getErrorCode()).isNull();
    }

    @Test
    @DisplayName("Should set errorDescription to null")
    void testSetErrorDescriptionToNull() {
        FieldError error = new FieldError("ERROR001", "Description");

        error.setErrorDescription(null);

        assertThat(error.getErrorDescription()).isNull();
    }
}

