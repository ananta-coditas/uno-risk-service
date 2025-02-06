package com.ambry.transformer.core;

import java.util.List;
import java.util.Map;

import com.ambry.transformer.core.validator.FieldError;
import com.objex.risk.beans.BaseResult;

public class RiskModelErrorDto extends BaseResult{

    private List<FieldError> validationErrors;
    
    @Override
    public String getModel() {
        return null;
    }

    @Override
    public Map<String, String> getColumnToFields() {
        return null;
    }

    public List<FieldError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<FieldError> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
