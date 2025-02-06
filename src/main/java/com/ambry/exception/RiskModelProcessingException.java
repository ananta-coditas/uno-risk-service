package com.ambry.exception;

public class RiskModelProcessingException extends RuntimeException {
    public RiskModelProcessingException(String message) {
        super(message);
    }

    public RiskModelProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
