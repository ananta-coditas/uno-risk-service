package com.ambry.transformer.core.validator;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

public class ValidationErrors {
    private List<ValidationError> errors = new ArrayList<>();

    public ValidationErrors() {
    }

    public List<ValidationError> getErrors() {
        return this.errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    public void rejectValue(String field, String message) {
        this.errors.add(new ValidationError(field, message));
    }

    public boolean hasErros() {
        return !CollectionUtils.isEmpty(this.errors);
    }

    public String toString() {
        return (new ToStringBuilder(this)).append(" errors", this.errors).toString();
    }
}
