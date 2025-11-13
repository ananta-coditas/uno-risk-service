package com.ambry.transformer.core.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ValidationError Unit Tests")
class ValidationErrorTest {

    @Test
    @DisplayName("Should create ValidationError with field and message")
    void testConstructorWithFieldAndMessage() {
        ValidationError error = new ValidationError("fieldName", "Error message");

        assertThat(error.getField()).isEqualTo("fieldName");
        assertThat(error.getMessage()).isEqualTo("Error message");
    }

    @Test
    @DisplayName("Should set and get field")
    void testSetAndGetField() {
        ValidationError error = new ValidationError("field1", "Message1");

        error.setField("field2");

        assertThat(error.getField()).isEqualTo("field2");
    }

    @Test
    @DisplayName("Should set and get message")
    void testSetAndGetMessage() {
        ValidationError error = new ValidationError("field1", "Message1");

        error.setMessage("Message2");

        assertThat(error.getMessage()).isEqualTo("Message2");
    }

    @Test
    @DisplayName("Should handle null field")
    void testWithNullField() {
        ValidationError error = new ValidationError(null, "Error message");

        assertThat(error.getField()).isNull();
        assertThat(error.getMessage()).isEqualTo("Error message");
    }

    @Test
    @DisplayName("Should handle null message")
    void testWithNullMessage() {
        ValidationError error = new ValidationError("fieldName", null);

        assertThat(error.getField()).isEqualTo("fieldName");
        assertThat(error.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle empty field")
    void testWithEmptyField() {
        ValidationError error = new ValidationError("", "Error message");

        assertThat(error.getField()).isEmpty();
        assertThat(error.getMessage()).isEqualTo("Error message");
    }

    @Test
    @DisplayName("Should handle empty message")
    void testWithEmptyMessage() {
        ValidationError error = new ValidationError("fieldName", "");

        assertThat(error.getField()).isEqualTo("fieldName");
        assertThat(error.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Should return string representation")
    void testToString() {
        ValidationError error = new ValidationError("fieldName", "Error message");

        String result = error.toString();

        assertThat(result).isNotNull();
        assertThat(result).contains("fieldName");
        assertThat(result).contains("Error message");
    }

    @Test
    @DisplayName("Should update field and message")
    void testUpdateFieldAndMessage() {
        ValidationError error = new ValidationError("field1", "Message1");

        error.setField("field2");
        error.setMessage("Message2");

        assertThat(error.getField()).isEqualTo("field2");
        assertThat(error.getMessage()).isEqualTo("Message2");
    }
}

