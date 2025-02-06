package com.ambry.transformer.core.validator;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ValidationError {
    private String field;
    private String message;

    public ValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("field", this.field).append("message", this.message).toString();
    }
}
