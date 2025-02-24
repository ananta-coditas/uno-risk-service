package com.ambry.transformer.core.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

import com.objex.risk.beans.RiskError;

public class ValidationErrors {
    private List<RiskError> errors = new ArrayList<>();

    public ValidationErrors() {
    }

    public List<RiskError> getErrors() {
        return this.errors;
    }

    public void setErrors(List<RiskError> errors) {
        this.errors = errors;
    }

    public void rejectValue(String field, String message) {
        this.errors.add(new RiskError(null, field, message));
    }

    public boolean hasErros() {
        return !CollectionUtils.isEmpty(this.errors);
    }

    public String toString() {
        return (new ToStringBuilder(this)).append(" errors", this.errors).toString();
    }
}
