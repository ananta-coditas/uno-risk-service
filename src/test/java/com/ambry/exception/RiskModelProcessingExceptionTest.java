package com.ambry.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RiskModelProcessingException Unit Tests")
class RiskModelProcessingExceptionTest {

    @Test
    @DisplayName("Should create RiskModelProcessingException with message only")
    void testConstructorWithMessage() {
        String message = "Risk model processing failed";
        RiskModelProcessingException exception = new RiskModelProcessingException(message);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should create RiskModelProcessingException with message and cause")
    void testConstructorWithMessageAndCause() {
        String message = "Risk model processing failed";
        Throwable cause = new RuntimeException("Root cause");
        RiskModelProcessingException exception = new RiskModelProcessingException(message, cause);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getCause().getMessage()).isEqualTo("Root cause");
    }

    @Test
    @DisplayName("Should preserve exception chain")
    void testExceptionChaining() {
        String message = "Outer exception";
        RuntimeException innerException = new RuntimeException("Inner exception");
        IllegalArgumentException middleException = new IllegalArgumentException("Middle exception", innerException);
        RiskModelProcessingException outerException = new RiskModelProcessingException(message, middleException);

        assertThat(outerException.getMessage()).isEqualTo(message);
        assertThat(outerException.getCause()).isEqualTo(middleException);
        assertThat(outerException.getCause().getCause()).isEqualTo(innerException);
        assertThat(outerException.getCause().getCause().getMessage()).isEqualTo("Inner exception");
    }

    @Test
    @DisplayName("Should handle null cause")
    void testConstructorWithNullCause() {
        String message = "Test message";
        RiskModelProcessingException exception = new RiskModelProcessingException(message, null);

        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }
}

