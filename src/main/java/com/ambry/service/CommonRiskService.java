package com.ambry.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.micrometer.context.ContextExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ambry.exception.ValidationException;
import com.ambry.transformer.core.RiskModelErrorDto;
import com.ambry.transformer.core.RiskModelExecuterFactory;
import com.ambry.transformer.core.RiskModelExecutor;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Risk;

@Service
public class CommonRiskService {

    private static Logger log = LoggerFactory.getLogger(CommonRiskService.class);

    private final RiskModelExecuterFactory riskModelTransformerFactory;
    private static final ExecutorService EXECUTOR = ContextExecutorService.wrap(Executors.newFixedThreadPool(5));

    public CommonRiskService(RiskModelExecuterFactory riskModelTransformerFactory) {
        this.riskModelTransformerFactory = riskModelTransformerFactory;
    }

    /**
     * Main method to invoke the requested risk models in parallel
     * @param risk
     * @return
     */
    public CombinedResult calculateRiskModels(Risk risk) {
        CombinedResult result = new CombinedResult();
        List<Model> requestedModels = risk.getRequestedModels();

        List<CompletableFuture<Void>> futures = requestedModels.stream()
            .map(model -> CompletableFuture.runAsync(() -> processModel(risk, model, result), EXECUTOR))
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return result;
    }

    /**
     * Method to process the Risk Model invocation
     * @param risk
     * @param model
     * @param result
     */
    private void processModel(Risk risk, Model model, CombinedResult result) {
        try {
            RiskModelExecutor<?, ?, ? extends BaseResult> riskModelTransformer =
                riskModelTransformerFactory.getRiskModelExecuter(model.getName());
            BaseResult modelResult = riskModelTransformer.execute(risk, model);
            result.addModelResult(model.getName(), modelResult);
        } catch (ValidationException validationException) {
            log.error("Validation error while invoking Risk models: {}", model.getName());
            RiskModelErrorDto riskModelErrorDto = createValidationErrorDto(validationException);
            addErrorToResult(result, model, riskModelErrorDto);
        } catch (Exception exc) {
            log.error("Error while invoking Risk model: {}", model.getName(), exc);
            RiskModelErrorDto riskModelErrorDto = new RiskModelErrorDto();
            riskModelErrorDto.addError(new RiskError(null, "", exc.getMessage()));
            addErrorToResult(result, model, riskModelErrorDto);
        }
    }

    private RiskModelErrorDto createValidationErrorDto(ValidationException validationException) {
        RiskModelErrorDto riskModelErrorDto = new RiskModelErrorDto();
        List<RiskError> validationErrors = validationException.getValidationErrors().getErrors()
                .stream()
                .map(error -> new RiskError(null, error.getCode(), error.getDescription()))
                .toList();

        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(riskModelErrorDto::addError);
        } else {
            riskModelErrorDto.addError(new RiskError(null, "", validationException.getMessage()));
        }
        return riskModelErrorDto;
    }

    private void addErrorToResult(CombinedResult result, Model model, RiskModelErrorDto errorDto) {
        result.addModelResult(model.getName(), errorDto);
    }

}
