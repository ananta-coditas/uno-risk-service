package com.ambry.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ambry.exception.ValidationException;
import com.ambry.transformer.core.RiskModelErrorDto;
import com.ambry.transformer.core.RiskModelExecuterFactory;
import com.ambry.transformer.core.RiskModelExecutor;
import com.ambry.transformer.core.validator.FieldError;
import com.ambry.transformer.core.validator.ValidationError;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.uno.CombinedResult;
import com.objex.risk.beans.uno.Model;
import com.objex.risk.beans.uno.Risk;

@Service
public class CommonRiskService {

    private static Logger log = LoggerFactory.getLogger(CommonRiskService.class);

    private final RiskModelExecuterFactory riskModelTransformerFactory;

    public CommonRiskService(RiskModelExecuterFactory riskModelTransformerFactory) {
        this.riskModelTransformerFactory = riskModelTransformerFactory;
    }

    /**
     * Main method to invoke the requested risk models
     * @param risk
     * @return
     */
    public CombinedResult calculateRiskModels(Risk risk) {
        CombinedResult result = new CombinedResult();
        for (Model model : risk.getRequestedModels()) {
            try {
                RiskModelExecutor<?, ?, ? extends BaseResult> riskModelTransformer = riskModelTransformerFactory
                        .getRiskModelExecuter(model.getName());
                BaseResult modelResult = riskModelTransformer.execute(risk, model);
                result.addModelResult(model.getName(), modelResult);
            } catch (ValidationException validationException) {
                log.error("validation Error while invoking the Risk models");
                RiskModelErrorDto riskModelErrorDto = new RiskModelErrorDto();
                List<ValidationError> validationErrors = validationException.getValidationErrors().getErrors(). stream()
                        .map(error -> new ValidationError(error.getField(), error.getMessage()))
                        .collect(Collectors.toList());
                if(validationErrors != null && !CollectionUtils.isEmpty(validationErrors)) {
                    riskModelErrorDto.setValidationErrors(validationErrors.stream().map(x -> new FieldError(x.getField(), x.getMessage())).collect(Collectors.toList()));
                }
                else {
                    riskModelErrorDto.setValidationErrors(List.of(new FieldError("", validationException.getMessage())));
                }
                result.addModelResult(model.getName(), riskModelErrorDto);
            }catch (Exception exc) {
                RiskModelErrorDto riskModelErrorDto = new RiskModelErrorDto();
                riskModelErrorDto.setValidationErrors(List.of(new FieldError("", exc.getMessage())));
                log.error("Error while invoking the Risk models", exc);
            }
        }
        return result;
    }

}
