package com.ambry.transformer.core.validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objex.risk.beans.uno.Model.ModelName;

/**
 * Factory class to manage and retrieve {@link RiskModelValidator} instances based on {@link ModelName}.
 * This allows dynamic selection of the appropriate validator for a given risk model.
 */
@Component
public class RiskModelValidatorFactory {

    private final Map<ModelName, RiskModelValidator<?>> validators;

    /**
     * Constructs the factory by mapping model names to their respective validators.
     *
     * @param validatorList List of {@link RiskModelValidator} instances available for risk model validation.
     */
    @Autowired
    public RiskModelValidatorFactory(List<RiskModelValidator<?>> validatorList) {
        this.validators = validatorList.stream()
                .collect(Collectors.toMap(RiskModelValidator::getModelName, v -> v));
    }

    /**
     * Retrieves the validator associated with the specified risk model name.
     *
     * @param modelName The name of the risk model for which the validator is required.
     * @return The appropriate {@link RiskModelValidator} instance for the given model name,
     *         or {@code null} if no validator is found.
     */
    public RiskModelValidator<?> getValidator(ModelName modelName) {
        return validators.get(modelName);
    }
}