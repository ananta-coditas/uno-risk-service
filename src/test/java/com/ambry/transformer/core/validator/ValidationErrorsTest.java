package com.ambry.transformer.core.validator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.objex.risk.beans.RiskError;

@DisplayName("ValidationErrors Unit Tests")
class ValidationErrorsTest {

    private ValidationErrors validationErrors;

    @BeforeEach
    void setUp() {
        validationErrors = new ValidationErrors();
    }

    @Test
    @DisplayName("Should create ValidationErrors with empty errors list")
    void testConstructor() {
        assertThat(validationErrors.getErrors()).isNotNull();
        assertThat(validationErrors.getErrors()).isEmpty();
        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should add error using rejectValue")
    void testRejectValue() {
        validationErrors.rejectValue("fieldName", "Error message");

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.hasErros()).isTrue();
        RiskError error = validationErrors.getErrors().get(0);
        assertThat(error).isNotNull();
    }

    @Test
    @DisplayName("Should add multiple errors")
    void testRejectValueMultiple() {
        validationErrors.rejectValue("field1", "Error 1");
        validationErrors.rejectValue("field2", "Error 2");
        validationErrors.rejectValue("field3", "Error 3");

        assertThat(validationErrors.getErrors()).hasSize(3);
        assertThat(validationErrors.hasErros()).isTrue();
    }

    @Test
    @DisplayName("Should set and get errors list")
    void testSetAndGetErrors() {
        List<RiskError> errors = new ArrayList<>();
        errors.add(new RiskError(null, "field1", "Error 1"));
        errors.add(new RiskError(null, "field2", "Error 2"));

        validationErrors.setErrors(errors);

        assertThat(validationErrors.getErrors()).hasSize(2);
        assertThat(validationErrors.getErrors()).isEqualTo(errors);
        assertThat(validationErrors.hasErros()).isTrue();
    }

    @Test
    @DisplayName("Should return false for hasErros when errors list is empty")
    void testHasErrosWithEmptyList() {
        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should return true for hasErros when errors list has items")
    void testHasErrosWithItems() {
        validationErrors.rejectValue("fieldName", "Error message");

        assertThat(validationErrors.hasErros()).isTrue();
    }

    @Test
    @DisplayName("Should handle null field in rejectValue")
    void testRejectValueWithNullField() {
        validationErrors.rejectValue(null, "Error message");

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.getErrors().get(0)).isNotNull();
    }

    @Test
    @DisplayName("Should handle null message in rejectValue")
    void testRejectValueWithNullMessage() {
        validationErrors.rejectValue("fieldName", null);

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.getErrors().get(0)).isNotNull();
    }

    @Test
    @DisplayName("Should handle empty field in rejectValue")
    void testRejectValueWithEmptyField() {
        validationErrors.rejectValue("", "Error message");

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.getErrors().get(0)).isNotNull();
    }

    @Test
    @DisplayName("Should handle empty message in rejectValue")
    void testRejectValueWithEmptyMessage() {
        validationErrors.rejectValue("fieldName", "");

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.getErrors().get(0)).isNotNull();
    }

    @Test
    @DisplayName("Should set errors to null")
    void testSetErrorsToNull() {
        validationErrors.setErrors(null);

        assertThat(validationErrors.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should return string representation")
    void testToString() {
        validationErrors.rejectValue("field1", "Error 1");
        validationErrors.rejectValue("field2", "Error 2");

        String result = validationErrors.toString();

        assertThat(result).isNotNull();
        assertThat(result).contains("errors");
    }

    @Test
    @DisplayName("Should handle hasErros with null errors list")
    void testHasErrosWithNullErrors() {
        validationErrors.setErrors(null);

        // hasErros() uses CollectionUtils.isEmpty which returns true for null
        assertThat(validationErrors.hasErros()).isFalse();
    }

    @Test
    @DisplayName("Should replace existing errors when setErrors is called")
    void testSetErrorsReplacesExisting() {
        validationErrors.rejectValue("field1", "Error 1");
        validationErrors.rejectValue("field2", "Error 2");

        List<RiskError> newErrors = Arrays.asList(new RiskError(null, "field3", "Error 3"));
        validationErrors.setErrors(newErrors);

        assertThat(validationErrors.getErrors()).hasSize(1);
        assertThat(validationErrors.getErrors().get(0)).isNotNull();
    }
}

