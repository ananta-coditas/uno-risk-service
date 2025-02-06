package com.ambry.transformer.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.Model.ModelName;

/**
 * Factory class to manage and retrieve {@link RiskModelExecutor} instances based on {@link ModelName}.
 * This allows dynamic selection of the appropriate transformer for a given risk model.
 */
@Component
public class RiskModelExecuterFactory {

    private final Map<ModelName, RiskModelExecutor<?, ?, ? extends BaseResult>> transformers;

    /**
     * Constructs the factory by mapping model names to their respective transformers.
     *
     * @param transformerList List of {@link RiskModelExecutor} instances available for risk model transformation.
     */
    @Autowired
    public RiskModelExecuterFactory(List<RiskModelExecutor<?, ?, ?>> transformerList) {
        this.transformers = transformerList.stream()
                .collect(Collectors.toMap(RiskModelExecutor::getModelName, t -> t));
    }

    /**
     * Retrieves the transformer associated with the specified risk model name.
     *
     * @param modelName The name of the risk model for which the transformer is required.
     * @param <T>       The request type expected by the transformer.
     * @param <Z>       The response type produced by the transformer.
     * @param <R>       The result type extending {@link BaseResult}.
     * @return The appropriate {@link RiskModelExecutor} instance for the given model name,
     *         or {@code null} if no transformer is found.
     */
    public RiskModelExecutor<?, ?, ? extends BaseResult> getRiskModelExecuter(ModelName modelName) {
        return transformers.get(modelName);
    }
}
