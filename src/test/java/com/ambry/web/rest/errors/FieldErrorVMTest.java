package com.ambry.web.rest.errors;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FieldErrorVM Unit Tests")
class FieldErrorVMTest {

    @Test
    @DisplayName("Should create FieldErrorVM with all properties")
    void testConstructor() {
        String objectName = "testDTO";
        String field = "testField";
        String message = "Test error message";

        FieldErrorVM fieldErrorVM = new FieldErrorVM(objectName, field, message);

        assertThat(fieldErrorVM).isNotNull();
        assertThat(fieldErrorVM.getObjectName()).isEqualTo(objectName);
        assertThat(fieldErrorVM.getField()).isEqualTo(field);
        assertThat(fieldErrorVM.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should handle null values")
    void testConstructorWithNullValues() {
        FieldErrorVM fieldErrorVM = new FieldErrorVM(null, null, null);

        assertThat(fieldErrorVM).isNotNull();
        assertThat(fieldErrorVM.getObjectName()).isNull();
        assertThat(fieldErrorVM.getField()).isNull();
        assertThat(fieldErrorVM.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle empty strings")
    void testConstructorWithEmptyStrings() {
        FieldErrorVM fieldErrorVM = new FieldErrorVM("", "", "");

        assertThat(fieldErrorVM).isNotNull();
        assertThat(fieldErrorVM.getObjectName()).isEmpty();
        assertThat(fieldErrorVM.getField()).isEmpty();
        assertThat(fieldErrorVM.getMessage()).isEmpty();
    }

    @Test
    @DisplayName("Should get object name correctly")
    void testGetObjectName() {
        String objectName = "userDTO";
        FieldErrorVM fieldErrorVM = new FieldErrorVM(objectName, "field", "message");

        assertThat(fieldErrorVM.getObjectName()).isEqualTo(objectName);
    }

    @Test
    @DisplayName("Should get field correctly")
    void testGetField() {
        String field = "email";
        FieldErrorVM fieldErrorVM = new FieldErrorVM("dto", field, "message");

        assertThat(fieldErrorVM.getField()).isEqualTo(field);
    }

    @Test
    @DisplayName("Should get message correctly")
    void testGetMessage() {
        String message = "Email is required";
        FieldErrorVM fieldErrorVM = new FieldErrorVM("dto", "field", message);

        assertThat(fieldErrorVM.getMessage()).isEqualTo(message);
    }
}

