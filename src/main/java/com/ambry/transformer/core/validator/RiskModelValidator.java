package com.ambry.transformer.core.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.objex.risk.beans.uno.Model.ModelName;

public abstract class RiskModelValidator<R> {
    private static final Logger logger = LoggerFactory.getLogger(RiskModelValidator.class);

    public abstract void validate(R request, ValidationErrors validationErros);

    /**
     * Retrieves the name of the model.
     *
     * @return The name of the model.
     */
    public abstract ModelName getModelName();
}