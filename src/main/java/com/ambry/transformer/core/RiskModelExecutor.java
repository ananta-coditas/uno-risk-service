package com.ambry.transformer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ambry.exception.RiskModelProcessingException;
import com.ambry.exception.ValidationException;
import com.ambry.transformer.core.validator.RiskModelValidator;
import com.ambry.transformer.core.validator.ValidationErrors;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Model.ModelName;
import com.objex.risk.beans.uno.Risk;

/**
 * Interface to transform and invoke risk models.
 * <p>
 * This interface defines methods to transform a common request format into a model-specific format,
 * invoke the model, and then transform the model's response back into a common response format.
 *
 * @param <T> The type of the Risk Model Core Request object. i.e. NCCNCoreRequest, com.objex.risk.beans.gail.Gail
 * @param <Z> The type of the model-specific response object i.e. NCCNCoreResponse.
 * @param <R> The type of the Common Response Object(UNO Response) for Risk Model i.e. NCCNResult, TyrerResult.
 */
public interface RiskModelExecutor<T, Z, R extends BaseResult> {
    
    Logger LOG = LoggerFactory.getLogger(RiskModelExecutor.class);

    /**
     * Transforms the common request format to a model-specific request format.
     *
     * @param commonRequest The common request object containing the data.
     * @param model The model for which the request is being transformed.
     * @return The transformed request object for the model.
     */
    T transformRequest(Risk commonRequest, Model model);

    /**
     * Invokes the risk model with the formatted request object.
     *
     * @param formattedRequest The transformed request object.
     * @return The model-specific response object.
     */
    Z invokeRiskModel(T formattedRequest);

    /**
     * Transforms the model-specific response into a common response format.
     *
     * @param modelResponse The model-specific response object.
     * @return The transformed common response object.
     */
    R transformResponse(Z modelResponse);

    /**
     * Retrieves the name of the model.
     *
     * @return The name of the model.
     */
    ModelName getModelName();
    
    /**
     * Returns the validator
     * @return
     */
    RiskModelValidator<T> getRiskModelValidator();

    /**
     * Executes the full process of transforming a request, invoking a risk model,
     * and transforming the response.
     *
     * @param risk The common risk object containing the data.
     * @param model The model for which the risk is being calculated.
     * @return The transformed response object.
     * @throws RiskModelProcessingException If an error occurs during risk model processing.
     */
    default R execute(Risk risk, Model model) {
        R finalResult = null;
        Z modelResult = null;
        T transformedRequest = null;
        try {
            LOG.info("Starting transformation for model: {}", getModelName());
            transformedRequest = transformRequest(risk, model);
            RiskModelValidator<T> riskModelValidator = getRiskModelValidator();
            if(riskModelValidator != null) {
                ValidationErrors validationErrors = new ValidationErrors();
                riskModelValidator.validate(transformedRequest, validationErrors);
                if(validationErrors.hasErros()) {
                    throw new ValidationException("Error while risk model execution", validationErrors);
                }
            }
            LOG.info("Invoking risk model: {}", getModelName());
            modelResult = invokeRiskModel(transformedRequest); 
            LOG.info("Transforming model response to common response format for model: {}", getModelName());
            finalResult = transformResponse(modelResult); 
        } 
        catch(ValidationException validationException) {
            throw validationException;
        }
        catch (Exception exc) {
            LOG.error("Error while processing risk model for model: {}. Exception: {}", getModelName(), exc.getMessage(), exc);
            throw new RiskModelProcessingException("Error while calculation of risk model for model: " + getModelName(), exc);
        }
        return finalResult;
    }
}
